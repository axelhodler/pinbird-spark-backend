package earth.xor;

import java.net.UnknownHostException;

import org.xorrr.util.EnvironmentVars;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import earth.xor.db.DatastoreFacade;
import earth.xor.db.LinksDatastore;
import earth.xor.db.MongoLinksDatastore;
import earth.xor.rest.RestApi;
import earth.xor.rest.SparkFacade;
import earth.xor.rest.SparkRestApi;
import earth.xor.rest.routes.GetAllLinksRoute;
import earth.xor.rest.routes.GetLinkByIdRoute;
import earth.xor.rest.routes.PostLinkRoute;
import earth.xor.rest.transformation.JSONTransformator;

public class Main {

    public static void main(String args[]) throws UnknownHostException {
        MongoClientURI mongoUri = new MongoClientURI(
                System.getenv(EnvironmentVars.URI));
        MongoClient client = new MongoClient(mongoUri);

        LinksDatastore ds = new MongoLinksDatastore(client);
        DatastoreFacade facade = new DatastoreFacade(ds);
        JSONTransformator transformator = new JSONTransformator();

        RestApi rest = new SparkRestApi(new SparkFacade());
        rest.setPort(Integer.valueOf(System.getenv(EnvironmentVars.PORT)));
        rest.createPOSTlinksRoute(new PostLinkRoute(facade, transformator));
        rest.createGETlinkByIdRoute(new GetLinkByIdRoute(facade, transformator));
        rest.createGETlinksRoute(new GetAllLinksRoute(facade, transformator));
    }
}
