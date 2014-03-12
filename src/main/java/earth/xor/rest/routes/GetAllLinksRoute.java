package earth.xor.rest.routes;

import earth.xor.db.DatastoreFacade;
import spark.Request;
import spark.Response;
import spark.Route;

public class GetAllLinksRoute extends Route {

    private DatastoreFacade facade;

    public GetAllLinksRoute(DatastoreFacade facade) {
        super(Routes.GET_ALL_LINKS);
        this.facade = facade;
    }

    @Override
    public Object handle(Request request, Response response) {
        facade.getLinks();
        return null;
    }

}
