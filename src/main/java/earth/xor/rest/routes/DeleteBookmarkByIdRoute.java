package earth.xor.rest.routes;

import org.xorrr.util.HttpHeaderKeys;

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
        facade.deleteBookmarkById(request.params(":id"));;

        response.header(HttpHeaderKeys.ACAOrigin, "*");

        return request.body();
    }
}
