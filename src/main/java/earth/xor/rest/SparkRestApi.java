package earth.xor.rest;

import static spark.Spark.post;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import spark.Request;
import spark.Response;
import spark.Route;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import earth.xor.Url;
import earth.xor.UrlsCollection;

public class SparkRestApi {

    private MongoClient mongoClient;
    
    public SparkRestApi(MongoClient mongoClient) {
	this.mongoClient = mongoClient;
    }

    public void launchServer() {
	post(new Route("/urls") {
	   @Override
	   public Object handle(Request request, Response response) {
	       UrlsCollection urls = new UrlsCollection(mongoClient, "test");
	       
	       JSONObject obj = (JSONObject) JSONValue.parse(request.body());
	       
	       urls.addUrl(new Url(null, obj.get("title").toString(), null));
	       
	       return request.body();
	   }
	});	
    }

    public void stopServer() {
	// TODO Auto-generated method stub
	
    }

}
