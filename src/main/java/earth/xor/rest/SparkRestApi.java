package earth.xor.rest;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.setPort;

import org.xorrr.util.EnvironmentVars;

import earth.xor.db.DatastoreFacade;
import earth.xor.rest.routes.GetAllLinksRoute;
import earth.xor.rest.routes.GetLinkByIdRoute;
import earth.xor.rest.routes.PostLinkRoute;
import earth.xor.rest.transformation.Transformator;

public class SparkRestApi {
    private DatastoreFacade facade;

    public SparkRestApi(DatastoreFacade facade) {
        this.facade = facade;
    }

    public void startApi() {
        setPort(Integer.parseInt(System.getenv(EnvironmentVars.PORT)));

        createRoutes();
    }

    private void createRoutes() {
        createPOSTlinksRoute();
        createGETlinksRoute();
        createGETlinkByIdRoute();
    }

    private void createPOSTlinksRoute() {
        post(new PostLinkRoute(facade, new Transformator()));
    }

    private void createGETlinksRoute() {
        get(new GetAllLinksRoute(facade, new Transformator()));
    }

    private void createGETlinkByIdRoute() {
        get(new GetLinkByIdRoute(facade, new Transformator()));
    }
}
