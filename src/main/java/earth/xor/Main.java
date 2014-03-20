package earth.xor;

import java.net.UnknownHostException;

import org.xorrr.util.EnvironmentVars;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import earth.xor.db.BookmarksDatastore;
import earth.xor.db.DatastoreFacade;
import earth.xor.db.MongoBookmarksDatastore;
import earth.xor.rest.RestApi;
import earth.xor.rest.SparkFacade;
import earth.xor.rest.SparkRestApi;
import earth.xor.rest.routes.DeleteBookmarkByIdRoute;
import earth.xor.rest.routes.GetAllBookmarksRoute;
import earth.xor.rest.routes.GetBookmarkByIdRoute;
import earth.xor.rest.routes.OptionsForIdRoute;
import earth.xor.rest.routes.OptionsRoute;
import earth.xor.rest.routes.PostBookmarkRoute;
import earth.xor.rest.transformation.JSONTransformator;

public class Main {

    public static void main(String args[]) throws UnknownHostException {
        MongoClientURI mongoUri = new MongoClientURI(
                System.getenv(EnvironmentVars.URI));
        MongoClient client = new MongoClient(mongoUri);

        BookmarksDatastore ds = new MongoBookmarksDatastore(client);
        DatastoreFacade facade = new DatastoreFacade(ds);
        JSONTransformator transformator = new JSONTransformator();

        RestApi rest = new SparkRestApi(new SparkFacade());
        rest.setPort(Integer.valueOf(System.getenv(EnvironmentVars.PORT)));
        rest.createPOSTbookmarksRoute(new PostBookmarkRoute(facade, transformator));
        rest.createGETbookmarkByIdRoute(new GetBookmarkByIdRoute(facade, transformator));
        rest.createGETbookmarksRoute(new GetAllBookmarksRoute(facade, transformator));
        rest.createOPTIONSbookmarksRoute(new OptionsRoute());
        rest.createDELETEbookmarkByIdRoute(new DeleteBookmarkByIdRoute(facade));
        rest.createOPTIONSForBookmarkIdRoute(new OptionsForIdRoute());
    }
}
