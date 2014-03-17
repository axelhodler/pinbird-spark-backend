package earth.xor.rest.routes;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.xorrr.util.EnvironmentVars;
import org.xorrr.util.HttpHeaderKeys;
import org.xorrr.util.JsonAccessor;

import spark.Request;
import spark.Response;
import earth.xor.db.DatastoreFacade;
import earth.xor.model.Bookmark;
import earth.xor.rest.transformation.JSONTransformator;

@RunWith(MockitoJUnitRunner.class)
public class TestGetLinkByIdRoute {
    @Mock
    Request req;
    @Mock
    Response resp;
    @Mock
    DatastoreFacade facade;
    @Mock
    JSONTransformator transformator;

    private GetLinkByIdRoute route;
    private final String id = "12345";

    @Before
    public void setUp() {
        route = new GetLinkByIdRoute(facade, transformator);
    }

    @Test
    public void canGetLinkById() {
        when(req.params(":id")).thenReturn(id);
        when(transformator.linkToJson(any(Bookmark.class))).thenReturn(
                JsonAccessor.getExampleLink());

        Object json = route.handle(req, resp);

        verify(facade, times(1)).getLinkById(anyString());
        verify(transformator, times(1)).linkToJson(any(Bookmark.class));
        assertEquals(json, JsonAccessor.getExampleLink());
    }

    @Test
    public void accessControlAllowOriginHeaderUsed() {
        when(req.queryParams("pw")).thenReturn(
                System.getenv(EnvironmentVars.PW));
        when(req.body()).thenReturn("something");

        route.handle(req, resp);

        verify(resp, times(1)).header(HttpHeaderKeys.ACAOrigin, "*");
    }
}
