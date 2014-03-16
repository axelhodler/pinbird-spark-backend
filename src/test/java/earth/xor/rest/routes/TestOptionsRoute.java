package earth.xor.rest.routes;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import spark.Request;
import spark.Response;

@RunWith(MockitoJUnitRunner.class)
public class TestOptionsRoute {
    @Mock
    Request req;
    @Mock
    Response resp;

    private OptionsRoute route;

    @Before
    public void setUp() {
        route = new OptionsRoute();
    }

    @Test
    public void accessControlAllowMethodsUsed() {
        when(req.body()).thenReturn("something");

        route.handle(req, resp);

        verify(resp, times(1)).header("Access-Control-Allow-Methods",
                "GET, POST");
    }

    @Test
    public void accessControlAllowOriginHeaderUsed() {
        when(req.body()).thenReturn("something");

        route.handle(req, resp);

        verify(resp, times(1)).header("Access-Control-Allow-Origin", "*");
    }

    @Test
    public void accessControlAllowHeadersUsed() {
        when(req.body()).thenReturn("something");

        route.handle(req, resp);

        verify(resp, times(1)).header("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept");
    }
}
