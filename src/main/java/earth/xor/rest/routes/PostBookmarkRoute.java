package earth.xor.rest.routes;

import org.xorrr.util.EnvironmentVars;
import org.xorrr.util.HttpHeaderKeys;
import org.xorrr.util.HttpResponseErrorMessages;

import spark.Request;
import spark.Response;
import spark.Route;
import earth.xor.db.DatastoreFacade;
import earth.xor.rest.transformation.JSONTransformator;

public class PostBookmarkRoute extends Route {

    private DatastoreFacade facade;
    private JSONTransformator transformator;

    public PostBookmarkRoute(DatastoreFacade facade, JSONTransformator transformator) {
        super(Routes.POST_BOOKMARK);
        this.facade = facade;
        this.transformator = transformator;
    }

    @Override
    public Object handle(Request request, Response response) {
        checkPassword(request);
        checkIfPayloadMissing(request);

        facade.addLink(transformator.jsonToLink(request.body()));

        response.header(HttpHeaderKeys.ACAOrigin, "*");

        return request.body();
    }

    private void checkIfPayloadMissing(Request request) {
        if (payloadMissing(request))
            halt(400, HttpResponseErrorMessages.MISSING_PAYLOAD);
    }

    private boolean payloadMissing(Request request) {
        return request.body().equals("");
    }

    private void checkPassword(Request request) {
        String pw = request.headers(HttpHeaderKeys.Authorization);
        if (pw == null)
            halt(400, HttpResponseErrorMessages.MISSING_PW);
        else if (passwardIncorrect(pw))
            halt(401, HttpResponseErrorMessages.AUTH_FAIL);
    }

    private boolean passwardIncorrect(String pw) {
        return !pw.equals(System.getenv(EnvironmentVars.PW));
    }
}
