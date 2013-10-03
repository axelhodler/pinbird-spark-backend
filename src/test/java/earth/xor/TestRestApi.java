package earth.xor;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.jayway.restassured.RestAssured;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import earth.xor.db.DbProperties;
import earth.xor.db.Url;
import earth.xor.db.UrlsDatastore;
import earth.xor.rest.SparkRestApi;

public class TestRestApi {

    private SparkRestApi restapi;
    private Gson gson;

    private String jsonTestString = "{\"url\":\"http://www.foo.org\","
	    + "\"title\":\"foo\", \"user\":\"test\"}";

    private static int port = 12345;
    private static EmbeddedMongo embeddedMongo;
    private MongoClient mongoClient;

    @BeforeClass
    public static void setUpEmbeddedMongo() throws UnknownHostException,
	    IOException {
	embeddedMongo = new EmbeddedMongo();
	embeddedMongo.launchEmbeddedMongo(port);
    }

    @Before
    public void setUpRestApi() throws UnknownHostException {
	
	this.gson = new Gson();
	
	this.mongoClient = new MongoClient("localhost", port);

	RestAssured.port = 4567;
	
	restapi = new SparkRestApi(mongoClient);
	restapi.launchServer();
    }

    @Test
    public void testAddingAUrlThroughTheRestApi() {

	String jsonString = given().body(jsonTestString)
		.expect()
		.contentType("application/json")
		.post("/urls").asString();
	
	Url savedUrl = gson.fromJson(jsonString, Url.class);
	
	assertEquals("http://www.foo.org", savedUrl.getUrl());
	assertEquals("foo", savedUrl.getTitle());
	assertEquals("test", savedUrl.getUser());

	// Doublecheck if it really has been added to the Collection
	DB urlsDb = mongoClient.getDB(DbProperties.DATABASE_NAME);
	DBCollection col = urlsDb.getCollection("urls");

	DBObject foundEntry = col.findOne(new BasicDBObject("url",
		"http://www.foo.org"));

	assertEquals("http://www.foo.org", foundEntry.get("url"));
	assertEquals("foo", foundEntry.get("title"));
	assertEquals("test", foundEntry.get("user"));
    }

    @Test
    public void testGettingAListOfAllSavedUrls() {

	UrlsDatastore ds = new UrlsDatastore(mongoClient);

	ds.addUrl(new Url("http://www.foo.org", "foo", "user1"));
	ds.addUrl(new Url("http://www.bar.org", "bar", "user2"));
	ds.addUrl(new Url("http://www.baz.org", "baz", "user3"));

	expect().body(containsString("foo")).body(containsString("bar"))
		.body(containsString("baz")).body(containsString("_id"))
		.get("/urls");

    }
    
    @Test
    public void testGettingASavedUrlById() {
	
	UrlsDatastore ds = new UrlsDatastore(mongoClient);
	
	ds.addUrl(new Url("http://www.foo.org", "foo", "user1"));
	
	DB urlsDb = mongoClient.getDB(DbProperties.DATABASE_NAME);
	DBCollection col = urlsDb.getCollection("urls");

	DBObject foundEntry = col.findOne(new BasicDBObject("url",
		"http://www.foo.org"));
	
	String id = foundEntry.get("_id").toString();
	
	expect().body(containsString(id)).get("/urls/" + id);
	
	assertTrue(expect().get("/urls/" + id).getStatusCode() != 404);
    }

    /**
     * Drop the collection and stop the server with the Rest API
     */
    @After
    public void stopRestApi() {
	mongoClient.getDB(DbProperties.DATABASE_NAME)
		.getCollection(DbProperties.URLSCOLLECTION_NAME).drop();
	restapi.stopServer();
    }

    @AfterClass
    public static void stopEmbeddedMongo() {
	embeddedMongo.stopEmbeddedMongo();
    }
}
