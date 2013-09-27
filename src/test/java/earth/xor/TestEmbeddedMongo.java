package earth.xor;

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

public class TestEmbeddedMongo {

    private static int port = 12345;
    private static EmbeddedMongo embeddedMongo;
    
    private MongoClient mongo;
    private UrlDB urlDb;
    

    @BeforeClass
    public static void setUpEmbeddedMongo() throws UnknownHostException,
	    IOException {
	embeddedMongo = new EmbeddedMongo();
	embeddedMongo.launchEmbeddedMongo(port);
    }

    @Before
    public void setUpTests() throws UnknownHostException {
	this.mongo = new MongoClient("localhost", port);

	this.urlDb = new UrlDB(mongo, "test");
    }

    @Test
    public void testGettingCount() {
	
	DBCollection col = mongo.getDB("test").getCollection("test");
	col.insert(new BasicDBObject("name", "pete"));

	assertEquals(1, col.getCount());
    }

    @Test
    public void testAccessingUrlDB() {
	assertNotNull(urlDb.getMongoClient());
	assertTrue(urlDb.getMongoClient() instanceof MongoClient);
    }

    @Test
    public void testAddingAUrl() {
	UrlDB urlDB = new UrlDB(mongo, "test");

	urlDB.addUrl("http://www.foo.org", "foo", "user");
	assertEquals(1, urlDB.getUrls().size());
    }

    @AfterClass
    public static void stopEmbeddedMongo() {
	embeddedMongo.stopEmbeddedMongo();
    }

}
