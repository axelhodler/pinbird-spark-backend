package earth.xor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import earth.xor.db.DbProperties;
import earth.xor.db.Url;
import earth.xor.db.UrlsDatastore;

public class TestUrlsDatastore {

    private static int port = 12345;
    private static EmbeddedMongo embeddedMongo;

    private MongoClient mongoClient;
    private UrlsDatastore urlsCollection;

    @BeforeClass
    public static void setUpEmbeddedMongo() throws UnknownHostException,
	    IOException {
	embeddedMongo = new EmbeddedMongo();
	embeddedMongo.launchEmbeddedMongo(port);
    }

    @Before
    public void setUpTests() throws UnknownHostException {
	this.mongoClient = new MongoClient("localhost", port);

	this.urlsCollection = new UrlsDatastore(mongoClient);
    }

    @Test
    public void testAccessingUrlDB() {
	assertNotNull(urlsCollection.getMongoClient());
	assertTrue(urlsCollection.getMongoClient() instanceof MongoClient);
    }

    @Test
    public void testAddingAUrl() {
	urlsCollection = new UrlsDatastore(mongoClient);

	urlsCollection.addUrl(new Url("http://www.foo.org", "foo", "user"));
	assertEquals(1, urlsCollection.getUrls().size());
    }

    @Test
    public void testAddingAndGettingMultipleUrls() {
	urlsCollection.addUrl(new Url("http://www.foo.org", "foo", "user"));
	urlsCollection.addUrl(new Url("http://www.bar.org", "bar", "user2"));
	urlsCollection.addUrl(new Url("http://www.baz.org", "baz", "user3"));

	DBCursor urlsCursor = urlsCollection.getUrls();

	ArrayList<Url> urlList = new ArrayList<Url>();

	while (urlsCursor.hasNext()) {
	    DBObject dbo = urlsCursor.next();
	    Url currentUrl = new Url(dbo.get("url").toString(), dbo
		    .get("title").toString(), dbo.get("user").toString());
	    urlList.add(currentUrl);
	}

	assertEquals("bar", urlList.get(1).getTitle());
	assertEquals("user3", urlList.get(2).getUser());
    }

    @Test
    public void testGettingAUrlById() {
	urlsCollection.addUrl(new Url("http://www.foo.org", "foo", "user"));

	DBCollection col = mongoClient.getDB(DbProperties.DATABASE_NAME)
		.getCollection(DbProperties.URLSCOLLECTION_NAME);

	DBObject savedUrl = col.findOne();
	
	DBObject obj = urlsCollection.getUrlById(savedUrl.get("_id").toString());

	assertEquals("foo", obj.get("title"));
	assertEquals(savedUrl.get("_id"), obj.get("_id"));
    }

    @After
    public void clearTheCollection() {
	DB database = mongoClient.getDB(DbProperties.DATABASE_NAME);
	DBCollection col = database.getCollection("urls");
	col.drop();
    }

    @AfterClass
    public static void stopEmbeddedMongo() {
	embeddedMongo.stopEmbeddedMongo();
    }
}
