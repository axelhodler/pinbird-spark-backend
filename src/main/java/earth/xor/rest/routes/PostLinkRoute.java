package earth.xor.rest.routes;

import org.xorrr.util.EnvironmentVars;

import earth.xor.db.DatastoreFacade;
import spark.Request;
import spark.Response;
import spark.Route;

public class PostLinkRoute extends Route{

    private DatastoreFacade facade;

    public PostLinkRoute(String path, DatastoreFacade facade) {
        super(path);
        this.facade = facade;
    }

    @Override
    public Object handle(Request request, Response response) {
        String  pw = request.queryParams("pw");
        if (!pw.equals(System.getenv(EnvironmentVars.PW)))
            halt(401, "Authentication failed");
        return request.body();
    }
}
