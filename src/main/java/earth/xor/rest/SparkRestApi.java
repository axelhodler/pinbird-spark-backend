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
import earth.xor.db.Url;
import earth.xor.db.UrlsDatastore;

public class SparkRestApi {

    private MongoClient mongoClient;
    private UrlsDatastore urlsData;

    public SparkRestApi(MongoClient mongoClient) {
	this.mongoClient = mongoClient;
	this.urlsData = new UrlsDatastore(mongoClient);
    }

    public void launchServer() {
	createUrlsPostRoute();
	createUrlsGetRoute();
	createUrlsGetRouteForId();
    }

    public void createUrlsPostRoute() {
	post(new Route("/urls") {

	    @Override
	    public Object handle(Request request, Response response) {

		JSONObject obj = (JSONObject) JSONValue.parse(request.body());

		urlsData.addUrl(new Url(obj.get("url").toString(), obj.get(
			"title").toString(), obj.get("user").toString()));

		addAccessControlAllowOriginHeader(response);

		return request.body();
	    }
	});
    }

    public void createUrlsGetRoute() {
	get(new Route("/urls") {

	    @Override
	    public Object handle(Request request, Response response) {
		JSONArray array = new JSONArray();

		DBCursor curs = urlsData.getUrls();

		while (curs.hasNext()) {
		    DBObject dbobj = curs.next();

		    array.add(addDBObjectKeysToJsonObject(dbobj));
		}

		JSONObject object = new JSONObject();
		object.put("urls", array);

		addAccessControlAllowOriginHeader(response);

		return object.toJSONString();
	    }

	});
    }

    private void createUrlsGetRouteForId() {

	get(new Route("/urls/:id") {

	    @Override
	    public Object handle(Request request, Response response) {

		DBObject foundUrl = urlsData.getUrlById(request.params(":id"));
		
		JSONObject mainObject = new JSONObject();
		
		JSONObject innerObject = addDBObjectKeysToJsonObject(foundUrl);

		mainObject.put("url", innerObject);

		addAccessControlAllowOriginHeader(response);

		return mainObject.toJSONString();
	    }
	});
    }

    private JSONObject addDBObjectKeysToJsonObject(DBObject dbObject) {
	JSONObject jsonObject = new JSONObject();

	jsonObject.put("_id", dbObject.get("_id").toString());
	jsonObject.put("url", dbObject.get("url"));
	jsonObject.put("title", dbObject.get("title"));
	jsonObject.put("user", dbObject.get("user"));
	jsonObject.put(DbProperties.URLS_TIMESTAMP,
		formatDate((Date) dbObject.get(DbProperties.URLS_TIMESTAMP)));

	return jsonObject;
    }

    private String formatDate(Date date) {
	SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
	return sdf.format(date);
    }

    private void addAccessControlAllowOriginHeader(Response response) {
	response.header("Access-Control-Allow-Origin", "*");
    }

    public void stopServer() {
	// TODO Auto-generated method stub

    }

}
