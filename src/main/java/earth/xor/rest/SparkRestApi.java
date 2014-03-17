package earth.xor.rest;

import earth.xor.rest.routes.GetAllBookmarksRoute;
import earth.xor.rest.routes.GetBookmarkByIdRoute;
import earth.xor.rest.routes.OptionsRoute;
import earth.xor.rest.routes.PostBookmarkRoute;

public class SparkRestApi implements RestApi {
    private SparkFacade sparkFacade;

    public SparkRestApi(SparkFacade sparkFacade) {
        this.sparkFacade = sparkFacade;
    }

    public void setPort(int port) {
        sparkFacade.setPort(port);
    }

    public void createPOSTlinksRoute(PostBookmarkRoute route) {
        sparkFacade.setPostRoute(route);
    }

    public void createGETlinksRoute(GetAllBookmarksRoute route) {
        sparkFacade.setGetRoute(route);
    }

    public void createGETlinkByIdRoute(GetBookmarkByIdRoute route) {
        sparkFacade.setGetRoute(route);
    }

    @Override
    public void createOPTIONSlinksRoute(OptionsRoute route) {
        sparkFacade.setOptionsRoute(route);
    }
}
