package earth.xor.rest;

import org.xorrr.util.EnvironmentVars;

import earth.xor.rest.routes.GetAllLinksRoute;
import earth.xor.rest.routes.GetLinkByIdRoute;
import earth.xor.rest.routes.PostLinkRoute;

public class RestApi {
    private SparkFacade sparkFacade;

    public RestApi(SparkFacade sparkFacade) {
        this.sparkFacade = sparkFacade;
    }

    public void setPort() {
        sparkFacade.setPort(Integer.parseInt(System
                .getenv(EnvironmentVars.PORT)));
    }

    public void createPOSTlinksRoute(PostLinkRoute route) {
        sparkFacade.setPostRoute(route);
    }

    public void createGETlinksRoute(GetAllLinksRoute route) {
        sparkFacade.setGetRoute(route);
    }

    public void createGETlinkByIdRoute(GetLinkByIdRoute route) {
        sparkFacade.setGetRoute(route);
    }
}
