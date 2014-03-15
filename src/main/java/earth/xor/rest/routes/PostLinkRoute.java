package earth.xor.rest.routes;

import org.xorrr.util.EnvironmentVars;

import earth.xor.db.DatastoreFacade;
import earth.xor.model.LinkFields;
import earth.xor.rest.transformation.Transformator;
import spark.Request;
import spark.Response;
import spark.Route;

public class PostLinkRoute extends Route{

    private DatastoreFacade facade;
    private Transformator transformator;

    public PostLinkRoute(DatastoreFacade facade, Transformator transformator) {
        super(LinkFields.LINKS_NAME);
        this.facade = facade;
        this.transformator = transformator;
    }

    @Override
    public Object handle(Request request, Response response) {
        checkPassword(request);
        facade.addLink(transformator.toLink(request.body()));

        return request.body();
    }

    private void checkPassword(Request request) {
        String  pw = request.queryParams("pw");
        if (passwardIncorrect(pw))
            halt(401, "Authentication failed");
    }

    private boolean passwardIncorrect(String pw) {
        return !pw.equals(System.getenv(EnvironmentVars.PW));
    }
}
