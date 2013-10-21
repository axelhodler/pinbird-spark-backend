package earth.xor.learning;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import earth.xor.EmbeddedMongo;
import earth.xor.db.DbProperties;
import earth.xor.db.LinksDatastore;

public class TestEmbeddedMongo {

    private static int port = 12345;
    private MongoClient mongo;
    private LinksDatastore linksData;

    @BeforeClass
    public static void setUpEmbeddedMongo() throws UnknownHostException,
            IOException {
        EmbeddedMongo.getInstance();
    }

    @Before
    public void setUpTests() throws UnknownHostException {
        this.mongo = new MongoClient("localhost", port);

        this.linksData = new LinksDatastore(mongo);
    }

    @Test
    public void testGettingCount() {

        DBCollection col = mongo.getDB(DbProperties.DATABASE_NAME)
                .getCollection("test");
        col.insert(new BasicDBObject("name", "pete"));

        assertEquals(1, col.getCount());
    }
}
