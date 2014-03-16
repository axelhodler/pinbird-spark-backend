package earth.xor.rest.routes;

import spark.Request;
import spark.Response;
import spark.Route;

public class OptionsRoute extends Route {

    public OptionsRoute() {
        super(Routes.OPTIONS_LINKS);
    }

    @Override
    public Object handle(Request request, Response response) {
        response.header("Access-Control-Allow-Methods", "GET, POST");
        response.header("Access-Control-Allow-Origin", "*");
        response.header("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept");

        return request.body();
    }

}
