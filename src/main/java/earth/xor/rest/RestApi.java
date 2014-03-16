package earth.xor.rest;

import earth.xor.rest.routes.GetAllLinksRoute;
import earth.xor.rest.routes.GetLinkByIdRoute;
import earth.xor.rest.routes.OptionsRoute;
import earth.xor.rest.routes.PostLinkRoute;

public interface RestApi {
    void setPort(int port);

    void createPOSTlinksRoute(PostLinkRoute route);

    void createGETlinksRoute(GetAllLinksRoute route);

    void createGETlinkByIdRoute(GetLinkByIdRoute route);

    void createOPTIONSlinksRoute(OptionsRoute route);
}
