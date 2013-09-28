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

import com.mongodb.MongoClient;

public class TestUrlCollectionInteraction {

    private static int port = 12345;
    private static EmbeddedMongo embeddedMongo;

    private MongoClient mongoClient;
    private UrlsCollection urlsCollection;
    
    @BeforeClass
    public static void setUpEmbeddedMongo() throws UnknownHostException,
	    IOException {
	embeddedMongo = new EmbeddedMongo();
	embeddedMongo.launchEmbeddedMongo(port);
    }
    
    @Before
    public void setUpTests() throws UnknownHostException {
	this.mongoClient = new MongoClient("localhost", port);
	
	this.urlsCollection = new UrlsCollection(mongoClient, "test");
    }
    
    @Test
    public void testAccessingUrlDB() {
	assertNotNull(urlsCollection.getMongoClient());
	assertTrue(urlsCollection.getMongoClient() instanceof MongoClient);
    }

    @Test
    public void testAddingAUrl() {
	urlsCollection = new UrlsCollection(mongoClient, "test");

	urlsCollection.addUrl("http://www.foo.org", "foo", "user");
	assertEquals(1, urlsCollection.getUrls().size());
    }
    
    @AfterClass
    public static void stopEmbeddedMongo() {
	embeddedMongo.stopEmbeddedMongo();
    }
}
