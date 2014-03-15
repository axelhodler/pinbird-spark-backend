package earth.xor.rest;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.xorrr.util.EnvironmentVars;

import earth.xor.rest.routes.GetAllLinksRoute;
import earth.xor.rest.routes.GetLinkByIdRoute;
import earth.xor.rest.routes.PostLinkRoute;

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

    private SparkRestApi restApi;

    @Before
    public void setUp() {
        restApi = new SparkRestApi(sparkFacade);
    }

    @Test
    public void portIsSet() {
        restApi.setPort(Integer.parseInt(System.getenv(EnvironmentVars.PORT)));
        verify(sparkFacade, times(1)).setPort(anyInt());
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
