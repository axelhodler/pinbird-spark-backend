package earth.xor.rest;

import static spark.Spark.*;

import com.mongodb.MongoClient;

import spark.Request;
import spark.Response;
import spark.Route;

public class SparkRestApi {

    public SparkRestApi(MongoClient mongoClient) {
	// TODO Auto-generated constructor stub
    }

    public void launchServer() {
	post(new Route("/urls") {
	   @Override
	   public Object handle(Request request, Response response) {
	       return request.body();
	   }
	});	
    }

    public void stopServer() {
	// TODO Auto-generated method stub
	
    }

}
