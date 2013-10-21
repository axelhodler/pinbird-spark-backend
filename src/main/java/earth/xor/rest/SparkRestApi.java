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
    private LinksDatastore urlsData;

    private SparkRestApi() {}

    public static SparkRestApi getInstance() {
        if (uniqueInstance == null)
            uniqueInstance = new SparkRestApi();
        return uniqueInstance; 
    }

    public void launchServer(MongoClient mongoClient) {
        this.urlsData = new LinksDatastore(mongoClient);
        createUrlsPostRoute();
        createUrlsGetRoute();
        createUrlsGetRouteForId();
    }

    public void createUrlsPostRoute() {
        post(new Route(DbProperties.LINKS_ROUTE) {

            @Override
            public Object handle(Request request, Response response) {

                JSONObject obj = (JSONObject) JSONValue.parse(request.body());

                urlsData.addLink(new Link(obj.get(DbProperties.LINK_URL)
                        .toString(), obj.get(DbProperties.LINK_TITLE)
                        .toString(), obj.get(DbProperties.LINK_USER).toString()));

                addAccessControlAllowOriginHeader(response);

                return request.body();
            }
        });
    }

    public void createUrlsGetRoute() {
        get(new Route(DbProperties.LINKS_ROUTE) {

            @Override
            public Object handle(Request request, Response response) {
                JSONArray array = new JSONArray();

                DBCursor curs = urlsData.getLinks();

                while (curs.hasNext()) {
                    DBObject dbobj = curs.next();

                    array.add(addDBObjectKeysToJsonObject(dbobj));
                }

                JSONObject object = new JSONObject();
                object.put(DbProperties.LINKS_NAME, array);

                addAccessControlAllowOriginHeader(response);

                return object.toJSONString();
            }

        });
    }

    private void createUrlsGetRouteForId() {

        get(new Route(DbProperties.LINKS_ROUTE + "/:id") {

            @Override
            public Object handle(Request request, Response response) {

                DBObject foundLink = urlsData
                        .getLinkById(request.params(":id"));

                JSONObject mainObject = new JSONObject();

                JSONObject innerObject = addDBObjectKeysToJsonObject(foundLink);

                mainObject.put(DbProperties.LINK_URL, innerObject);

                addAccessControlAllowOriginHeader(response);

                return mainObject.toJSONString();
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
