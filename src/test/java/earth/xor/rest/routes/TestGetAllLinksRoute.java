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
public class TestGetAllLinksRoute {
    @Mock
    Request req;
    @Mock
    Response resp;
    @Mock
    DatastoreFacade facade;

    private GetAllLinksRoute route;

    @Before
    public void setUp() {
        route = new GetAllLinksRoute(facade);
    }

    @Test
    public void canGetLink() {
        route.handle(req, resp);

        verify(facade, times(1)).getLinks();
    }
}
