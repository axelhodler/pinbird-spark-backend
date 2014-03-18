package earth.xor.rest.routes;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.xorrr.util.HttpHeaderKeys;

import spark.Request;
import spark.Response;
import earth.xor.db.DatastoreFacade;

@RunWith(MockitoJUnitRunner.class)
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

}
