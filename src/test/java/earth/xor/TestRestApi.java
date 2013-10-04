package earth.xor;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

    private final Url testUrl1 = new Url("http://www.foo.org", "foo", "user1");
    private final Url testUrl2 = new Url("http://www.bar.org", "bar", "user2");
    private final Url testUrl3 = new Url("http://www.baz.org", "baz", "user3");

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

	String jsonString = given().body(jsonTestString).expect()
		.contentType("application/json").post("/urls").asString();

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

	ds.addUrl(testUrl1);
	ds.addUrl(testUrl2);
	ds.addUrl(testUrl3);

	String jsonString = expect().contentType("application/json")
		.get("/urls").asString();

	Type type = new TypeToken<Map<String, List<Url>>>() {
	}.getType();

	Map<String, List<Url>> map = new HashMap<String, List<Url>>();
	
	map = gson.fromJson(jsonString, type);

	ArrayList<Url> allUrls = (ArrayList<Url>) map.get("urls");
	
	assertEquals("http://www.foo.org", allUrls.get(0).getUrl());
	assertEquals("foo", allUrls.get(0).getTitle());
	assertEquals("user1", allUrls.get(0).getUser());

	assertEquals("http://www.bar.org", allUrls.get(1).getUrl());
	assertEquals("bar", allUrls.get(1).getTitle());
	assertEquals("user2", allUrls.get(1).getUser());

	assertEquals("http://www.baz.org", allUrls.get(2).getUrl());
	assertEquals("baz", allUrls.get(2).getTitle());
	assertEquals("user3", allUrls.get(2).getUser());
    }

    @Test
    public void testGettingASavedUrlById() {

	UrlsDatastore ds = new UrlsDatastore(mongoClient);

	ds.addUrl(testUrl1);

	DB urlsDb = mongoClient.getDB(DbProperties.DATABASE_NAME);
	DBCollection col = urlsDb.getCollection("urls");

	DBObject foundEntry = col.findOne(new BasicDBObject("url",
		"http://www.foo.org"));

	String id = foundEntry.get("_id").toString();

	String jsonString = expect().contentType("application/json")
		.get("/urls/" + id).asString();
	
	Type type = new TypeToken<Map<String, Url>>() {
	}.getType();
	
	Map<String, Url> map = new HashMap<String, Url>();
	
	map = gson.fromJson(jsonString, type);
	
	Url foundUrl = map.get("url");
	
	// id has to be surrounded with double quotes,
	// otherwise its not valid JSON
	assertTrue(jsonString.contains("\"" + id +"\"" ));
	
	assertEquals(id, foundUrl.getObjectId());
	assertEquals("http://www.foo.org", foundUrl.getUrl());
	assertEquals("foo", foundUrl.getTitle());
	assertEquals("user1", foundUrl.getUser());
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
