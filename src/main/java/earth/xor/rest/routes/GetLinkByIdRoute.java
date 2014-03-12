package earth.xor.rest.routes;

import spark.Request;
import spark.Response;
import spark.Route;
import earth.xor.db.DatastoreFacade;
import earth.xor.rest.transformation.Transformator;

public class GetLinkByIdRoute extends Route{

    private DatastoreFacade facade;
    private Transformator transformator;

    public GetLinkByIdRoute(DatastoreFacade facade, Transformator transformator) {
        super(Routes.GET_LINK_BY_ID);
        this.facade = facade;
        this.transformator = transformator;
    }

    @Override
    public Object handle(Request request, Response response) {
        return transformator.linkToJson(facade.getLinkById(request.params(":id")));
    }

}
