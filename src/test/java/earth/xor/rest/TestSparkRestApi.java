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

import earth.xor.rest.routes.DeleteBookmarkByIdRoute;
import earth.xor.rest.routes.GetAllBookmarksRoute;
import earth.xor.rest.routes.GetBookmarkByIdRoute;
import earth.xor.rest.routes.OptionsForIdRoute;
import earth.xor.rest.routes.OptionsRoute;
import earth.xor.rest.routes.PostBookmarkRoute;

@RunWith(MockitoJUnitRunner.class)
public class TestSparkRestApi {
    @Mock
    SparkFacade sparkFacade;
    @Mock
    PostBookmarkRoute postBookmarkRoute;
    @Mock
    GetBookmarkByIdRoute getBookmarkByIdRoute;
    @Mock
    GetAllBookmarksRoute getAllBookmarksRoute;
    @Mock
    OptionsRoute optionsRoute;
    @Mock
    DeleteBookmarkByIdRoute deleteRoute;
    @Mock
    OptionsForIdRoute optionsForIdRoute;

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
    public void postBookmarkRouteIsSet() {
        restApi.createPOSTbookmarksRoute(postBookmarkRoute);

        verify(sparkFacade, times(1)).setPostRoute(postBookmarkRoute);
    }

    @Test
    public void getBookmarkByIdRouteIsSet() {
        restApi.createGETbookmarkByIdRoute(getBookmarkByIdRoute);

        verify(sparkFacade, times(1)).setGetRoute(getBookmarkByIdRoute);
    }

    @Test
    public void getBookmarksRouteIsSet() {
        restApi.createGETbookmarksRoute(getAllBookmarksRoute);

        verify(sparkFacade, times(1)).setGetRoute(getAllBookmarksRoute);
    }

    @Test
    public void optionsRouteIsSet() {
        restApi.createOPTIONSbookmarksRoute(optionsRoute);

        verify(sparkFacade, times(1)).setOptionsRoute(optionsRoute);
    }

    @Test
    public void deleteBookmarkByIdRouteIsSet() {
        restApi.createDELETEbookmarkByIdRoute(deleteRoute);

        verify(sparkFacade, times(1)).setDeleteRoute(deleteRoute);
    }

    @Test
    public void optionsForIdRouteIsSet() {
        restApi.createOPTIONSForBookmarkIdRoute(optionsForIdRoute);

        verify(sparkFacade, times(1)).setOptionsForIdRoute(optionsForIdRoute);
    }
}
