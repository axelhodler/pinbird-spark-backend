package earth.xor.rest;

import static org.mockito.Matchers.anyInt;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import spark.Spark;
import earth.xor.rest.routes.GetLinkByIdRoute;
import earth.xor.rest.routes.PostLinkRoute;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Spark.class })
public class TestSparkFacade {
    @Mock
    GetLinkByIdRoute getLinkByIdRoute;
    @Mock
    PostLinkRoute postLinkRoute;

    private SparkFacade facade;

    @Before
    public void setUp() {
        facade = new SparkFacade();

        PowerMockito.mockStatic(Spark.class);
    }

    @Test
    public void canSetPort() {
        facade.setPort(anyInt());
        PowerMockito.verifyStatic();
        Spark.setPort(anyInt());
    }

    @Test
    public void canSetGetRoute() {
        facade.setGetRoute(getLinkByIdRoute);
        PowerMockito.verifyStatic();
        Spark.get(getLinkByIdRoute);
    }

    @Test
    public void canSetPostRoute() {
        facade.setGetRoute(postLinkRoute);
        PowerMockito.verifyStatic();
        Spark.get(postLinkRoute);
    }
}
