package earth.xor.rest.routes;

import org.xorrr.util.HttpHeaderKeys;

import spark.Request;
import spark.Response;
import spark.Route;
import earth.xor.db.DatastoreFacade;
import earth.xor.rest.transformation.JSONTransformator;

public class GetAllBookmarksRoute extends Route {

    private DatastoreFacade facade;
    private JSONTransformator transformator;

    public GetAllBookmarksRoute(DatastoreFacade facade, JSONTransformator transformator) {
        super(Routes.GET_ALL_BOOKMARKS);
        this.facade = facade;
        this.transformator = transformator;
    }

    @Override
    public Object handle(Request request, Response response) {
        response.header(HttpHeaderKeys.ACAOrigin, "*");

        return transformator.listOfLinksToJson(facade.getLinks());
    }

}
