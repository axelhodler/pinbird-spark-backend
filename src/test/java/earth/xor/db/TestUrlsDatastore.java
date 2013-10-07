package earth.xor.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import earth.xor.EmbeddedMongo;
import earth.xor.ExampleUrls;

public class TestUrlsDatastore {

    private static int port = 12345;
    private static EmbeddedMongo embeddedMongo;

    private MongoClient mongoClient;
    private UrlsDatastore urlsData;

    @BeforeClass
    public static void setUpEmbeddedMongo() throws UnknownHostException,
	    IOException {
	embeddedMongo = new EmbeddedMongo();
	embeddedMongo.launchEmbeddedMongo(port);
    }

    @Before
    public void setUpTests() throws UnknownHostException {
	this.mongoClient = new MongoClient("localhost", port);

	this.urlsData = new UrlsDatastore(mongoClient);
    }

    @Test
    public void testAccessingUrlDB() {
	assertNotNull(urlsData.getMongoClient());
	assertTrue(urlsData.getMongoClient() instanceof MongoClient);
    }

    @Test
    public void testAddingAUrl() {
	urlsData = new UrlsDatastore(mongoClient);

	urlsData.addUrl(ExampleUrls.testUrl1);
	assertEquals(1, urlsData.getUrls().size());
    }

    @Test
    public void testAddingAndGettingMultipleUrls() {
	urlsData.addUrl(ExampleUrls.testUrl1);
	urlsData.addUrl(ExampleUrls.testUrl2);
	urlsData.addUrl(ExampleUrls.testUrl3);

	DBCursor urlsCursor = urlsData.getUrls();
	
	List<Url> urlList = createUrlsArrayFromUrlDataInCursor(urlsCursor);

	assertEquals("bar", urlList.get(1).getTitle());
	assertEquals("user3", urlList.get(2).getUser());

	assertNotNull(urlList.get(1).getTimeStamp());
    }

    @Test
    public void testGettingAUrlById() {
	urlsData.addUrl(ExampleUrls.testUrl1);

	DBCollection col = mongoClient.getDB(DbProperties.DATABASE_NAME)
		.getCollection(DbProperties.URLSCOLLECTION_NAME);

	DBObject savedUrl = col.findOne();

	DBObject obj = urlsData.getUrlById(savedUrl.get("_id").toString());

	assertNotNull(obj);
	assertEquals("foo", obj.get("title").toString());
	assertEquals(savedUrl.get("_id"), obj.get("_id"));
    }
    
    private List<Url> createUrlsArrayFromUrlDataInCursor(
	    DBCursor urlsCursor) {
	
	List<Url> urlList = new ArrayList<Url>();
	
	while (urlsCursor.hasNext()) {
	    DBObject dbo = urlsCursor.next();
	    Url currentUrl = new Url(dbo.get(DbProperties.URLSCOLLECTION_URL)
		    .toString(), dbo.get(DbProperties.URLSCOLLECTION_TITLE)
		    .toString(), dbo.get(DbProperties.URLSCOLLECTION_USER)
		    .toString(), dbo.get(DbProperties.URLSCOLLECTION_TIMESTAMP).toString());
	    urlList.add(currentUrl);
	}
	
	return urlList;
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
