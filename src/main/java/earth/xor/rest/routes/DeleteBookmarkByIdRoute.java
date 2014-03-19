package earth.xor.rest.routes;

import org.xorrr.util.EnvironmentVars;
import org.xorrr.util.HttpHeaderKeys;
import org.xorrr.util.HttpResponseErrorMessages;

import spark.Request;
import spark.Response;
import spark.Route;
import earth.xor.db.DatastoreFacade;

public class DeleteBookmarkByIdRoute extends Route {
    private DatastoreFacade facade;

    public DeleteBookmarkByIdRoute(DatastoreFacade facade) {
        super(Routes.DELETE_BOOKMARK);
        this.facade = facade;
    }

    @Override
    public Object handle(Request request, Response response) {
        checkPassword(request);

        facade.deleteBookmarkById(request.params(":id"));;

        response.header(HttpHeaderKeys.ACAOrigin, "*");

        return request.body();
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
