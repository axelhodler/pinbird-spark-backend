package earth.xor.learning;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.AfterClass;
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
    private static EmbeddedMongo embeddedMongo;
    
    private MongoClient mongo;
    private LinksDatastore urlDb;
    

    @BeforeClass
    public static void setUpEmbeddedMongo() throws UnknownHostException,
	    IOException {
	embeddedMongo = new EmbeddedMongo();
	embeddedMongo.launchEmbeddedMongo(port);
    }

    @Before
    public void setUpTests() throws UnknownHostException {
	this.mongo = new MongoClient("localhost", port);

	this.urlDb = new LinksDatastore(mongo);
    }

    @Test
    public void testGettingCount() {
	
	DBCollection col = mongo.getDB(DbProperties.DATABASE_NAME).getCollection("test");
	col.insert(new BasicDBObject("name", "pete"));

	assertEquals(1, col.getCount());
    }

    @AfterClass
    public static void stopEmbeddedMongo() {
	embeddedMongo.stopEmbeddedMongo();
    }

}
