package earth.xor.rest.routes;

import org.xorrr.util.HttpHeaderKeys;

import spark.Request;
import spark.Response;
import spark.Route;

public class OptionsForIdRoute extends Route{

    public OptionsForIdRoute() {
        super(Routes.GET_BOOKMARKS_BY_ID);
    }

    @Override
    public Object handle(Request request, Response response) {
        response.header(HttpHeaderKeys.ACAMethods, "GET, DELETE");
        response.header(HttpHeaderKeys.ACAOrigin, "*");
        response.header(HttpHeaderKeys.ACAHeaders,
                "Origin, X-Requested-With, Content-Type, Accept, "
                        + HttpHeaderKeys.Authorization);

        return request.body();
    }

}
