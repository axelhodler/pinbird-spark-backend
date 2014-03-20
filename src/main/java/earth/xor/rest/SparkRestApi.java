package earth.xor.rest;

import earth.xor.rest.routes.DeleteBookmarkByIdRoute;
import earth.xor.rest.routes.GetAllBookmarksRoute;
import earth.xor.rest.routes.GetBookmarkByIdRoute;
import earth.xor.rest.routes.OptionsForIdRoute;
import earth.xor.rest.routes.OptionsRoute;
import earth.xor.rest.routes.PostBookmarkRoute;

public class SparkRestApi implements RestApi {
    private SparkFacade sparkFacade;

    public SparkRestApi(SparkFacade sparkFacade) {
        this.sparkFacade = sparkFacade;
    }

    @Override
    public void setPort(int port) {
        sparkFacade.setPort(port);
    }

    @Override
    public void createPOSTbookmarksRoute(PostBookmarkRoute route) {
        sparkFacade.setPostRoute(route);
    }

    @Override
    public void createGETbookmarksRoute(GetAllBookmarksRoute route) {
        sparkFacade.setGetRoute(route);
    }

    @Override
    public void createGETbookmarkByIdRoute(GetBookmarkByIdRoute route) {
        sparkFacade.setGetRoute(route);
    }

    @Override
    public void createOPTIONSbookmarksRoute(OptionsRoute route) {
        sparkFacade.setOptionsRoute(route);
    }

    @Override
    public void createDELETEbookmarkByIdRoute(DeleteBookmarkByIdRoute route) {
        sparkFacade.setDeleteRoute(route);
    }

    @Override
    public void createOPTIONSForBookmarkIdRoute(OptionsForIdRoute route) {
        sparkFacade.setOptionsForIdRoute(route);
    }
}
