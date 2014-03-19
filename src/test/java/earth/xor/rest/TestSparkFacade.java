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
import earth.xor.rest.routes.DeleteBookmarkByIdRoute;
import earth.xor.rest.routes.GetBookmarkByIdRoute;
import earth.xor.rest.routes.OptionsRoute;
import earth.xor.rest.routes.PostBookmarkRoute;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Spark.class })
public class TestSparkFacade {
    @Mock
    GetBookmarkByIdRoute getBookmarkByIdRoute;
    @Mock
    PostBookmarkRoute postBookmarkRoute;
    @Mock
    OptionsRoute optionsRoute;
    @Mock
    DeleteBookmarkByIdRoute deleteRoute;

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
        facade.setGetRoute(getBookmarkByIdRoute);
        PowerMockito.verifyStatic();
        Spark.get(getBookmarkByIdRoute);
    }

    @Test
    public void canSetPostRoute() {
        facade.setGetRoute(postBookmarkRoute);
        PowerMockito.verifyStatic();
        Spark.get(postBookmarkRoute);
    }

    @Test
    public void canSetOptionsRoute() {
        facade.setOptionsRoute(optionsRoute);
        PowerMockito.verifyStatic();
        Spark.options(optionsRoute);
    }

    @Test
    public void canSetDeleteRoute() {
        facade.setDeleteRoute(deleteRoute);
        PowerMockito.verifyStatic();
        Spark.delete(deleteRoute);
    }
}
