package earth.xor.rest;

import static spark.Spark.get;
import static spark.Spark.post;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import spark.Request;
import spark.Response;
import spark.Route;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import earth.xor.db.DbProperties;
import earth.xor.db.Link;
import earth.xor.db.LinksDatastore;

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
        createPOSTlinksRoute();
        createGETlinksRoute();
        createGETlinkByIdRoute();
    }

    public void createPOSTlinksRoute() {
        post(new Route(DbProperties.LINKS_ROUTE) {

            @Override
            public Object handle(Request request, Response response) {
                JSONObject obj = (JSONObject) JSONValue.parse(request.body());
                linksDs.addLink(createLinkFromJSONObject(obj));
                addAccessControlAllowOriginHeader(response);
                return request.body();
            }

            private Link createLinkFromJSONObject(JSONObject obj) {
                return new Link(obj.get(DbProperties.LINK_URL).toString(), obj
                        .get(DbProperties.LINK_TITLE).toString(), obj.get(
                        DbProperties.LINK_USER).toString());
            }
        });
    }

    public void createGETlinksRoute() {
        get(new Route(DbProperties.LINKS_ROUTE) {

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
                object.put(DbProperties.LINKS_NAME, array);
                return object;
            }

            @SuppressWarnings("unchecked")
            private void iterateCursorToAddObjectsToArray(JSONArray array,
                    DBCursor curs) {
                while (curs.hasNext()) {
                    array.add(addDBObjectKeysToJsonObject(curs.next()));
                }
            }
        });
    }

    private void createGETlinkByIdRoute() {
        get(new Route(DbProperties.LINKS_ROUTE + "/:id") {

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
                JSONObject innerObject = addDBObjectKeysToJsonObject(foundLink);
                mainObject.put(DbProperties.LINK_URL, innerObject);
                return mainObject;
            }
        });
    }

    private JSONObject addDBObjectKeysToJsonObject(DBObject dbObject) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(DbProperties.LINK_ID, dbObject.get(DbProperties.LINK_ID)
                .toString());
        jsonObject.put(DbProperties.LINK_URL,
                dbObject.get(DbProperties.LINK_URL));
        jsonObject.put(DbProperties.LINK_TITLE,
                dbObject.get(DbProperties.LINK_TITLE));
        jsonObject.put(DbProperties.LINK_USER,
                dbObject.get(DbProperties.LINK_USER));
        jsonObject.put(DbProperties.LINK_TIMESTAMP,
                formatDate((Date) dbObject.get(DbProperties.LINK_TIMESTAMP)));

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
