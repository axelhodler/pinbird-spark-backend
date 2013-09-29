package earth.xor.rest;

import static spark.Spark.*;

import spark.Request;
import spark.Response;
import spark.Route;

public class SparkRestApi {

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
