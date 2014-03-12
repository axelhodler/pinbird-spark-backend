package earth.xor.rest;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.setPort;

import org.json.simple.JSONObject;

import spark.Request;
import spark.Response;
import spark.Route;
import earth.xor.db.DatastoreFacade;
import earth.xor.db.Link;
import earth.xor.db.LinkFields;
import earth.xor.rest.routes.GetAllLinksRoute;
import earth.xor.rest.routes.PostLinkRoute;
import earth.xor.rest.routes.Routes;
import earth.xor.rest.transformation.Transformator;

public class SparkRestApi {

    private DatastoreFacade facade;

    public SparkRestApi(DatastoreFacade facade) {
        this.facade = facade;
    }

    public void startApi() {
        setPort(Integer.parseInt(System.getenv("PORT")));

        setRoutes();
    }

    private void setRoutes() {
        createPOSTlinksRoute();
        createGETlinksRoute();
        createGETlinkByIdRoute();
    }

    private void createPOSTlinksRoute() {
        post(new PostLinkRoute(facade, new Transformator()));
    }

    private void createGETlinksRoute() {
        get(new GetAllLinksRoute(facade, new Transformator()));
    }

    private void createGETlinkByIdRoute() {
        get(new Route(Routes.POST_LINK + "/:id") {

            @Override
            public Object handle(Request request, Response response) {
                Link foundLink = facade.getLinkById(request.params(":id"));
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
