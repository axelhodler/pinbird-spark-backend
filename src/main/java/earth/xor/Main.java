package earth.xor;

import java.net.UnknownHostException;

import org.xorrr.util.EnvironmentVars;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import earth.xor.db.DatastoreFacade;
import earth.xor.db.BookmarkDatastore;
import earth.xor.db.MongoBookmarkDatastore;
import earth.xor.rest.RestApi;
import earth.xor.rest.SparkFacade;
import earth.xor.rest.SparkRestApi;
import earth.xor.rest.routes.GetAllBookmarksRoute;
import earth.xor.rest.routes.GetBookmarkByIdRoute;
import earth.xor.rest.routes.OptionsRoute;
import earth.xor.rest.routes.PostBookmarkRoute;
import earth.xor.rest.transformation.JSONTransformator;

public class Main {

    public static void main(String args[]) throws UnknownHostException {
        MongoClientURI mongoUri = new MongoClientURI(
                System.getenv(EnvironmentVars.URI));
        MongoClient client = new MongoClient(mongoUri);

        BookmarkDatastore ds = new MongoBookmarkDatastore(client);
        DatastoreFacade facade = new DatastoreFacade(ds);
        JSONTransformator transformator = new JSONTransformator();

        RestApi rest = new SparkRestApi(new SparkFacade());
        rest.setPort(Integer.valueOf(System.getenv(EnvironmentVars.PORT)));
        rest.createPOSTlinksRoute(new PostBookmarkRoute(facade, transformator));
        rest.createGETlinkByIdRoute(new GetBookmarkByIdRoute(facade, transformator));
        rest.createGETlinksRoute(new GetAllBookmarksRoute(facade, transformator));
        rest.createOPTIONSlinksRoute(new OptionsRoute());
    }
}
