package earth.xor.rest;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static spark.Spark.setPort;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.xorrr.util.EnvironmentVars;

import spark.Route;
import spark.Spark;
import earth.xor.db.DatastoreFacade;
import earth.xor.rest.routes.GetAllLinksRoute;
import earth.xor.rest.routes.GetLinkByIdRoute;
import earth.xor.rest.routes.PostLinkRoute;
import earth.xor.rest.transformation.Transformator;

@RunWith(MockitoJUnitRunner.class)
public class TestSparkRestApi {
    @Mock
    SparkFacade sparkFacade;
    @Mock
    PostLinkRoute postLinkRoute;
    @Mock
    GetLinkByIdRoute getLinkByIdRoute;
    @Mock
    GetAllLinksRoute getAllLinksRoute;

    private RestApi restApi;

    @Before
    public void setUp() {
        restApi = new RestApi(sparkFacade);
    }

    @Test
    public void portIsSet() {
        restApi.setPort();
        verify(sparkFacade, times(1)).setPort(
                Integer.parseInt(System.getenv(EnvironmentVars.PORT)));
    }

    @Test
    public void postLinkRouteIsSet() {
        restApi.createPOSTlinksRoute(postLinkRoute);

        verify(sparkFacade, times(1)).setPostRoute(postLinkRoute);
    }

    @Test
    public void getLinkByIdRouteIsSet() {
        restApi.createGETlinkByIdRoute(getLinkByIdRoute);

        verify(sparkFacade, times(1)).setGetRoute(getLinkByIdRoute);
    }

    @Test
    public void getLinksRouteIsSet() {
        restApi.createGETlinksRoute(getAllLinksRoute);

        verify(sparkFacade, times(1)).setGetRoute(getAllLinksRoute);
    }
}
