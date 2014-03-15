package earth.xor.rest;

import earth.xor.rest.routes.GetAllLinksRoute;
import earth.xor.rest.routes.GetLinkByIdRoute;
import earth.xor.rest.routes.PostLinkRoute;

public class SparkRestApi implements RestApi {
    private SparkFacade sparkFacade;

    public SparkRestApi(SparkFacade sparkFacade) {
        this.sparkFacade = sparkFacade;
    }

    public void setPort(int port) {
        sparkFacade.setPort(port);
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
