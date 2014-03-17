package earth.xor.rest;

import earth.xor.rest.routes.GetAllBookmarksRoute;
import earth.xor.rest.routes.GetBookmarkByIdRoute;
import earth.xor.rest.routes.OptionsRoute;
import earth.xor.rest.routes.PostBookmarkRoute;

public interface RestApi {
    void setPort(int port);

    void createPOSTlinksRoute(PostBookmarkRoute route);

    void createGETlinksRoute(GetAllBookmarksRoute route);

    void createGETlinkByIdRoute(GetBookmarkByIdRoute route);

    void createOPTIONSlinksRoute(OptionsRoute route);
}
