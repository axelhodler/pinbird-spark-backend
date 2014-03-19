package earth.xor.rest;

import spark.Route;
import spark.Spark;

public class SparkFacade {

    public void setPort(int port) {
        Spark.setPort(port);
    }

    public void setGetRoute(Route route) {
        Spark.get(route);
    }

    public void setPostRoute(Route route) {
        Spark.post(route);
    }

    public void setOptionsRoute(Route route) {
        Spark.options(route);
    }

    public void setDeleteRoute(Route route) {
        Spark.delete(route);
    }
}
