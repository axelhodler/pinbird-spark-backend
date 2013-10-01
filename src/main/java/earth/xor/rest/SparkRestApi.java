package earth.xor.rest;

import static spark.Spark.get;
import static spark.Spark.post;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import spark.Request;
import spark.Response;
import spark.Route;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

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
    }
    
    public void createUrlsPostRoute() {
	post(new Route("/urls") {
	    
	    @Override
	    public Object handle(Request request, Response response) {

		JSONObject obj = (JSONObject) JSONValue.parse(request.body());

		urlsData.addUrl(new Url(obj.get("url").toString(), obj.get("title")
			.toString(), obj.get("user").toString()));

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
		    JSONObject obj = new JSONObject();
		    
		    DBObject dbobj = curs.next();
		    
		    obj.put("url", dbobj.get("url"));
		    obj.put("title", dbobj.get("title"));
		    obj.put("user", dbobj.get("user"));
		    
		    array.add(obj);
		}
		return array.toJSONString();
	    }
	});
    }

    public void stopServer() {
	// TODO Auto-generated method stub

    }

}
