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

    public void createPOSTbookmarksRoute(PostBookmarkRoute route) {
        sparkFacade.setPostRoute(route);
    }

    public void createGETbookmarksRoute(GetAllBookmarksRoute route) {
        sparkFacade.setGetRoute(route);
    }

    public void createGETbookmarkByIdRoute(GetBookmarkByIdRoute route) {
        sparkFacade.setGetRoute(route);
    }

    @Override
    public void createOPTIONSbookmarksRoute(OptionsRoute route) {
        sparkFacade.setOptionsRoute(route);
    }
}
