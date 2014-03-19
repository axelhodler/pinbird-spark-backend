package earth.xor.rest.routes;

import static org.mockito.Matchers.anyString;
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
import org.xorrr.util.HttpHeaderKeys;
import org.xorrr.util.HttpResponseErrorMessages;

import spark.AbstractRoute;
import spark.Request;
import spark.Response;
import earth.xor.db.DatastoreFacade;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ AbstractRoute.class })
public class TestDeleteBookmarkByIdRoute {
    @Mock
    Request req;
    @Mock
    Response resp;
    @Mock
    DatastoreFacade facade;

    private DeleteBookmarkByIdRoute route;
    private String id = "12345";

    @Before
    public void setUp() {
        PowerMockito.mockStatic(AbstractRoute.class);

        route = new DeleteBookmarkByIdRoute(facade);
    }

    @Test
    public void canDeleteBookmark() {
        when(req.params(":id")).thenReturn(id);

        route.handle(req, resp);

        verify(facade, times(1)).deleteBookmarkById(anyString());
    }

    @Test
    public void accessControlAllowOriginHeaderUsed() {
        when(req.body()).thenReturn("something");

        route.handle(req, resp);

        verify(resp, times(1)).header(HttpHeaderKeys.ACAOrigin, "*");
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
}
