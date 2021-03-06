package earth.xor.rest.routes;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.xorrr.util.EnvironmentVars;
import org.xorrr.util.HttpHeaderKeys;
import org.xorrr.util.HttpResponseErrorMessages;
import org.xorrr.util.JSONExamples;
import org.xorrr.util.BookmarkObjects;

import spark.AbstractRoute;
import spark.Request;
import spark.Response;
import earth.xor.db.DatastoreFacade;
import earth.xor.model.Bookmark;
import earth.xor.rest.transformation.JSONTransformator;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ AbstractRoute.class })
public class TestPostBookmarkRoute {
    @Mock
    Request req;
    @Mock
    Response resp;
    @Mock
    DatastoreFacade facade;
    @Mock
    JSONTransformator transformator;
    @Mock
    Bookmark testBookmark;

    private PostBookmarkRoute route;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(AbstractRoute.class);
        route = new PostBookmarkRoute(facade, transformator);

        testBookmark = BookmarkObjects.testBookmark1;
    }

    @Test
    public void bookmarkCanBePosted() {
        String json = JSONExamples.getPostRequestBody();
        when(req.headers(HttpHeaderKeys.Authorization)).thenReturn(
                System.getenv(EnvironmentVars.PW));
        when(req.body()).thenReturn(json);
        when(transformator.jsonToBookmark(json)).thenReturn(testBookmark);

        Object returned = route.handle(req, resp);

        verify(req, times(1)).headers("Authorization");
        verify(facade, times(1)).addBookmark(testBookmark);
        verify(req, times(3)).body();
        assertEquals(json, returned.toString());
    }

    @Test
    public void failRequestWithNoPassword() throws Exception {
        when(req.headers(HttpHeaderKeys.Authorization)).thenReturn(null);
        when(req.body()).thenReturn("");

        route.handle(req, resp);

        PowerMockito.verifyPrivate(AbstractRoute.class).invoke(400,
                HttpResponseErrorMessages.MISSING_PW);
    }

    @Test
    public void dontAuthWithWrongPassword() throws Exception {
        when(req.headers(HttpHeaderKeys.Authorization)).thenReturn("wrong");
        when(req.body()).thenReturn("");

        route.handle(req, resp);

        PowerMockito.verifyPrivate(AbstractRoute.class).invoke(401,
                HttpResponseErrorMessages.AUTH_FAIL);
    }

    @Test
    public void haltIfPayloadMissing() throws Exception {
        when(req.headers(HttpHeaderKeys.Authorization)).thenReturn(
                System.getenv(EnvironmentVars.PW));
        when(req.body()).thenReturn("");

        route.handle(req, resp);

        PowerMockito.verifyPrivate(AbstractRoute.class).invoke(400,
                HttpResponseErrorMessages.MISSING_PAYLOAD);
    }

    @Test
    public void accessControlAllowOriginHeaderUsed() {
        when(req.headers(HttpHeaderKeys.Authorization)).thenReturn(
                System.getenv(EnvironmentVars.PW));
        when(req.body()).thenReturn("something");

        route.handle(req, resp);

        verify(resp, times(1)).header(HttpHeaderKeys.ACAOrigin, "*");
    }
}
