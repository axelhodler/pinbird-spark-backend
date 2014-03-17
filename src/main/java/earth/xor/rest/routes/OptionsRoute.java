package earth.xor.rest.routes;

import org.xorrr.util.HttpHeaderKeys;

import spark.Request;
import spark.Response;
import spark.Route;

public class OptionsRoute extends Route {

    public OptionsRoute() {
        super(Routes.OPTIONS_LINKS);
    }

    @Override
    public Object handle(Request request, Response response) {
        response.header(HttpHeaderKeys.ACAMethods, "GET, POST");
        response.header(HttpHeaderKeys.ACAOrigin, "*");
        response.header(HttpHeaderKeys.ACAHeaders,
                "Origin, X-Requested-With, Content-Type, Accept, "
                        + HttpHeaderKeys.Authorization);

        return request.body();
    }

}
