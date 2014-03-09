package earth.xor.rest;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.setPort;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import spark.Request;
import spark.Response;
import spark.Route;
import earth.xor.db.DatastoreFacade;
import earth.xor.db.Link;
import earth.xor.db.LinkFields;

public class SparkRestApi {

    private DatastoreFacade linksDs;

    public void startApi(DatastoreFacade ds) {
        this.linksDs = ds;

        setPort(Integer.parseInt(System.getenv("PORT")));

        setRoutes();
    }

    private void setRoutes() {
        createPOSTlinksRoute();
        createGETlinksRoute();
        createGETlinkByIdRoute();
    }

    private void createPOSTlinksRoute() {
        post(new Route(LinkFields.LINKS_ROUTE) {

            @Override
            public Object handle(Request request, Response response) {
                String pw = request.queryParams("pw");

                if (!(pw != null && pw.equals(System.getenv("PASS"))))
                    halt(401, "Authentication failed");

                JSONObject obj = (JSONObject) JSONValue.parse(request.body());
                linksDs.addLink(createLinkFromJSONObject(obj));
                addAccessControlAllowOriginHeader(response);
                return request.body();
            }

            private Link createLinkFromJSONObject(JSONObject obj) {
                return new Link.Builder()
                        .url(obj.get(LinkFields.URL).toString())
                        .title(obj.get(LinkFields.TITLE).toString())
                        .user(obj.get(LinkFields.USER).toString()).build();
            }
        });
    }

    private void createGETlinksRoute() {
        get(new Route(LinkFields.LINKS_ROUTE) {

            @Override
            public Object handle(Request request, Response response) {
                JSONArray array = new JSONArray();
                List<Link> links = linksDs.getLinks();

                iterateCursorToAddObjectsToArray(array, links);
                JSONObject object = createEmberJsCompliantJSONObject(array);
                addAccessControlAllowOriginHeader(response);
                return object.toJSONString();
            }

            @SuppressWarnings("unchecked")
            private JSONObject createEmberJsCompliantJSONObject(JSONArray array) {
                JSONObject object = new JSONObject();
                object.put(LinkFields.LINKS_NAME, array);
                return object;
            }

            @SuppressWarnings("unchecked")
            private void iterateCursorToAddObjectsToArray(JSONArray array,
                    List<Link> links) {
                for (Link l : links)
                    array.add(linkToJson(l));
            }
        });
    }

    private void createGETlinkByIdRoute() {
        get(new Route(LinkFields.LINKS_ROUTE + "/:id") {

            @Override
            public Object handle(Request request, Response response) {
                Link foundLink = linksDs.getLinkById(request.params(":id"));
                JSONObject mainObject = createEmberJsConformJsonObject(foundLink);
                addAccessControlAllowOriginHeader(response);
                return mainObject.toJSONString();
            }

            @SuppressWarnings("unchecked")
            private JSONObject createEmberJsConformJsonObject(Link foundLink) {
                JSONObject mainObject = new JSONObject();
                JSONObject innerObject = linkToJson(foundLink);
                mainObject.put(LinkFields.URL, innerObject);
                return mainObject;
            }
        });
    }

    @SuppressWarnings("unchecked")
    private JSONObject linkToJson(Link link) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(LinkFields.ID, link.getObjectId());
        jsonObject.put(LinkFields.URL, link.getUrl());
        jsonObject.put(LinkFields.TITLE, link.getTitle());
        jsonObject.put(LinkFields.USER, link.getUser());
        jsonObject.put(LinkFields.TIMESTAMP, link.getTimeStamp());

        return jsonObject;
    }

    private void addAccessControlAllowOriginHeader(Response response) {
        response.header("Access-Control-Allow-Origin", "*");
    }
}
