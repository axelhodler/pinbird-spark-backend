package earth.xor.rest.routes;

import earth.xor.db.DatastoreFacade;
import spark.Request;
import spark.Response;
import spark.Route;

public class GetLinkRoute extends Route {

    private DatastoreFacade facade;

    public GetLinkRoute(DatastoreFacade facade) {
        super(Routes.GET_LINK);
        this.facade = facade;
    }

    @Override
    public Object handle(Request request, Response response) {
        facade.getLinks();
        return null;
    }

}
