package earth.xor.rest;

import earth.xor.rest.routes.DeleteBookmarkByIdRoute;
import earth.xor.rest.routes.GetAllBookmarksRoute;
import earth.xor.rest.routes.GetBookmarkByIdRoute;
import earth.xor.rest.routes.OptionsForIdRoute;
import earth.xor.rest.routes.OptionsRoute;
import earth.xor.rest.routes.PostBookmarkRoute;

public interface RestApi {
    void setPort(int port);

    void createPOSTbookmarksRoute(PostBookmarkRoute route);

    void createGETbookmarksRoute(GetAllBookmarksRoute route);

    void createGETbookmarkByIdRoute(GetBookmarkByIdRoute route);

    void createOPTIONSbookmarksRoute(OptionsRoute route);

    void createDELETEbookmarkByIdRoute(DeleteBookmarkByIdRoute route);

    void createOPTIONSForBookmarkIdRoute(OptionsForIdRoute route);
}
