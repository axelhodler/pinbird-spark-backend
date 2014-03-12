package earth.xor.rest.routes;

import spark.Request;
import spark.Response;
import spark.Route;
import earth.xor.db.DatastoreFacade;
import earth.xor.rest.transformation.Transformator;

public class GetAllLinksRoute extends Route {

    private DatastoreFacade facade;
    private Transformator transformator;

    public GetAllLinksRoute(DatastoreFacade facade, Transformator transformator) {
        super(Routes.GET_ALL_LINKS);
        this.facade = facade;
        this.transformator = transformator;
    }

    @Override
    public Object handle(Request request, Response response) {
        return transformator.toJson(facade.getLinks());
    }

}