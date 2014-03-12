package earth.xor.rest.routes;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import spark.Request;
import spark.Response;
import earth.xor.db.DatastoreFacade;

@RunWith(PowerMockRunner.class)
public class TestGetLinkRoute {
    @Mock
    Request req;
    @Mock
    Response resp;
    @Mock
    DatastoreFacade facade;

    private GetLinkRoute route;

    @Before
    public void setUp() {
        route = new GetLinkRoute(facade);
    }

    @Test
    public void canGetLink() {
        route.handle(req, resp);

        verify(facade, times(1)).getLinks();
    }
}
