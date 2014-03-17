package earth.xor.rest;

import earth.xor.rest.routes.GetAllBookmarksRoute;
import earth.xor.rest.routes.GetBookmarkByIdRoute;
import earth.xor.rest.routes.OptionsRoute;
import earth.xor.rest.routes.PostBookmarkRoute;

public interface RestApi {
    void setPort(int port);

    void createPOSTbookmarksRoute(PostBookmarkRoute route);

    void createGETbookmarksRoute(GetAllBookmarksRoute route);

    void createGETbookmarkByIdRoute(GetBookmarkByIdRoute route);

    void createOPTIONSbookmarksRoute(OptionsRoute route);
}
