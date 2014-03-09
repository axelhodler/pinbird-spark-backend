package earth.xor.rest.routes;

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

import spark.AbstractRoute;
import spark.Request;
import spark.Response;
import earth.xor.db.LinkFields;
import earth.xor.rest.routes.PostLinkRoute;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ AbstractRoute.class })
public class TestPostLinkRoute {
    @Mock
    Request req;
    @Mock
    Response resp;

    private PostLinkRoute route;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(AbstractRoute.class);
        route = new PostLinkRoute(LinkFields.LINKS_ROUTE);
    }

    @Test
    public void linkCanBePosted() {
        when(req.queryParams("pw")).thenReturn(System.getenv(EnvironmentVars.PW));

        route.handle(req, resp);

        verify(req, times(1)).queryParams("pw");
    }

    @Test
    public void dontAuthWithWrongPassword() throws Exception {
        when(req.queryParams("pw")).thenReturn("wrong");

        route.handle(req, resp);

        PowerMockito.verifyPrivate(AbstractRoute.class).invoke(401,
                "Authentication failed");
    }
}
