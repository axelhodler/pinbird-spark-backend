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
    private Transformator transformator;

    public SparkRestApi(DatastoreFacade facade, Transformator transformator) {
        this.facade = facade;
        this.transformator = transformator;
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
        post(new PostLinkRoute(facade, transformator));
    }

    private void createGETlinksRoute() {
        get(new GetAllLinksRoute(facade, transformator));
    }

    private void createGETlinkByIdRoute() {
        get(new GetLinkByIdRoute(facade, transformator));
    }
}
