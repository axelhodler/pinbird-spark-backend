package earth.xor.rest;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.setPort;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Route;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import earth.xor.db.Link;
import earth.xor.db.LinksDatastore;
import earth.xor.db.LinksProp;

public class SparkRestApi {

    private static SparkRestApi uniqueInstance = null;
    private LinksDatastore linksDs;

    private SparkRestApi() {
    }

    public static SparkRestApi getInstance() {
        if (uniqueInstance == null)
            uniqueInstance = new SparkRestApi();
        return uniqueInstance;
    }

    public void launchServer(MongoClient mongoClient) {
        this.linksDs = new LinksDatastore(mongoClient);

        setPort(Integer.parseInt(System.getenv("PORT")));

        before(new Filter() {

            @Override
            public void handle(Request request, Response response) {
                String pw = request.queryParams("pw");

                if (!(pw != null && pw.equals(System.getenv("PASS"))))
                    halt(401, "Authentication failed");
            }
        });

        createPOSTlinksRoute();
        createGETlinksRoute();
        createGETlinkByIdRoute();
    }

    public void createPOSTlinksRoute() {
        post(new Route(LinksProp.LINKS_ROUTE) {

            @Override
            public Object handle(Request request, Response response) {
                JSONObject obj = (JSONObject) JSONValue.parse(request.body());
                linksDs.addLink(createLinkFromJSONObject(obj));
                addAccessControlAllowOriginHeader(response);
                return request.body();
            }

            private Link createLinkFromJSONObject(JSONObject obj) {
                return new Link(obj.get(LinksProp.URL).toString(), obj.get(
                        LinksProp.TITLE).toString(), obj.get(LinksProp.USER)
                        .toString());
            }
        });
    }

    public void createGETlinksRoute() {
        get(new Route(LinksProp.LINKS_ROUTE) {

            @Override
            public Object handle(Request request, Response response) {
                JSONArray array = new JSONArray();
                DBCursor curs = linksDs.getLinks();
                iterateCursorToAddObjectsToArray(array, curs);
                JSONObject object = createEmberJsCompliantJSONObject(array);
                addAccessControlAllowOriginHeader(response);
                return object.toJSONString();
            }

            @SuppressWarnings("unchecked")
            private JSONObject createEmberJsCompliantJSONObject(JSONArray array) {
                JSONObject object = new JSONObject();
                object.put(LinksProp.LINKS_NAME, array);
                return object;
            }

            @SuppressWarnings("unchecked")
            private void iterateCursorToAddObjectsToArray(JSONArray array,
                    DBCursor curs) {
                while (curs.hasNext()) {
                    array.add(dbObjectToJsonObject(curs.next()));
                }
            }
        });
    }

    private void createGETlinkByIdRoute() {
        get(new Route(LinksProp.LINKS_ROUTE + "/:id") {

            @Override
            public Object handle(Request request, Response response) {
                DBObject foundLink = linksDs.getLinkById(request.params(":id"));
                JSONObject mainObject = createEmberJsConformJsonObject(foundLink);
                addAccessControlAllowOriginHeader(response);
                return mainObject.toJSONString();
            }

            @SuppressWarnings("unchecked")
            private JSONObject createEmberJsConformJsonObject(DBObject foundLink) {
                JSONObject mainObject = new JSONObject();
                JSONObject innerObject = dbObjectToJsonObject(foundLink);
                mainObject.put(LinksProp.URL, innerObject);
                return mainObject;
            }
        });
    }

    @SuppressWarnings("unchecked")
    private JSONObject dbObjectToJsonObject(DBObject dbObject) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(LinksProp.ID, dbObject.get(LinksProp.ID).toString());
        jsonObject.put(LinksProp.URL, dbObject.get(LinksProp.URL));
        jsonObject.put(LinksProp.TITLE, dbObject.get(LinksProp.TITLE));
        jsonObject.put(LinksProp.USER, dbObject.get(LinksProp.USER));
        jsonObject.put(LinksProp.TIMESTAMP,
                formatDate((Date) dbObject.get(LinksProp.TIMESTAMP)));

        return jsonObject;
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
        return sdf.format(date);
    }

    private void addAccessControlAllowOriginHeader(Response response) {
        response.header("Access-Control-Allow-Origin", "*");
    }
}
