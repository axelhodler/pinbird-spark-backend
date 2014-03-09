package earth.xor.rest.routes;

import org.xorrr.util.EnvironmentVars;

import spark.Request;
import spark.Response;
import spark.Route;

public class PostLinkRoute extends Route{

    public PostLinkRoute(String path) {
        super(path);
    }

    @Override
    public Object handle(Request request, Response response) {
        String  pw = request.queryParams("pw");
        if (!pw.equals(System.getenv(EnvironmentVars.PW)))
            halt(401, "Authentication failed");
        return null;
    }

}
