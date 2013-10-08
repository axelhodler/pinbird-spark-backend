package earth.xor.rest;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
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

import earth.xor.EmbeddedMongo;
import earth.xor.ExampleUrls;
import earth.xor.db.DbProperties;
import earth.xor.db.Link;
import earth.xor.db.LinksDatastore;

public class TestRestApi {

    private SparkRestApi restapi;
    private Gson gson;

    private static int port = 12345;
    private static EmbeddedMongo embeddedMongo;
    private MongoClient mongoClient;
    private LinksDatastore urlsData;

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
	this.urlsData = new LinksDatastore(mongoClient);

	RestAssured.port = 4567;

	restapi = new SparkRestApi(mongoClient);
	restapi.launchServer();

    }

    @Test
    public void testAddingAUrlThroughTheRestApi() {

	addAUrlThroughTheRestApi();
	checkIfUrlWasAddedToDatabase();
    }

    @Test
    public void testGettingAListOfAllSavedUrls() {

	urlsData.addUrl(ExampleUrls.testUrl1);
	urlsData.addUrl(ExampleUrls.testUrl2);
	urlsData.addUrl(ExampleUrls.testUrl3);

	String jsonResponse = expect().contentType("application/json").and()
		.header("Access-Control-Allow-Origin", equalTo("*")).when()
		.get("/urls").asString();

	Type type = new TypeToken<Map<String, List<Link>>>() {
	}.getType();

	Map<String, List<Link>> returnedUrls = new HashMap<String, List<Link>>();

	returnedUrls = gson.fromJson(jsonResponse, type);

	checkIfPreviouslyAddedUrlsAreShown(returnedUrls);
    }

    @Test
    public void testGettingASavedUrlById() {

	String id = addUrlAndGetItsId();

	String jsonString = expect().contentType("application/json").and()
		.header("Access-Control-Allow-Origin", equalTo("*")).when()
		.get("/urls/" + id).asString();

	// id has to be surrounded with double quotes,
	// otherwise its not valid JSON
	assertTrue(jsonString.contains("\"" + id + "\""));

	Type type = new TypeToken<Map<String, Link>>() {
	}.getType();

	Map<String, Link> returnedUrlRepresentation = new HashMap<String, Link>();

	returnedUrlRepresentation = gson.fromJson(jsonString, type);

	checkIfItsTheCorrectUrl(returnedUrlRepresentation);
    }

    private void addAUrlThroughTheRestApi() {
	String jsonString = given().body(getUrlsPostJsonString()).expect()
		.contentType("application/json").and()
		.header("Access-Control-Allow-Origin", equalTo("*")).when()
		.post("/urls").asString();

	Link savedUrl = gson.fromJson(jsonString, Link.class);

	assertEquals("http://www.foo.org", savedUrl.getUrl());
	assertEquals("foo", savedUrl.getTitle());
	assertEquals("test", savedUrl.getUser());
    }

    private void checkIfUrlWasAddedToDatabase() {
	DB urlsDb = mongoClient.getDB(DbProperties.DATABASE_NAME);
	DBCollection col = urlsDb.getCollection("urls");

	DBObject foundEntry = col.findOne(new BasicDBObject("url",
		"http://www.foo.org"));

	assertEquals("http://www.foo.org", foundEntry.get("url"));
	assertEquals("foo", foundEntry.get("title"));
	assertEquals("test", foundEntry.get("user"));
    }

    private String getUrlsPostJsonString() {

	String jsonString = null;
	try {
	    File testFile = new File(TestRestApi.class.getResource(
		    "/urlsPost.JSON").toURI());
	    jsonString = IOUtils.toString(new FileInputStream(testFile));
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (URISyntaxException e1) {
	    e1.printStackTrace();
	}
	return jsonString;
    }

    private void checkIfPreviouslyAddedUrlsAreShown(
	    Map<String, List<Link>> returnedUrls) {

	ArrayList<Link> allUrls = (ArrayList<Link>) returnedUrls.get("urls");

	assertEquals("http://www.foo.org", allUrls.get(0).getUrl());
	assertEquals("foo", allUrls.get(0).getTitle());
	assertEquals("user1", allUrls.get(0).getUser());
	
	assertNotNull(allUrls.get(0).getTimeStamp());

	assertEquals("http://www.bar.org", allUrls.get(1).getUrl());
	assertEquals("bar", allUrls.get(1).getTitle());
	assertEquals("user2", allUrls.get(1).getUser());
	
	assertNotNull(allUrls.get(1).getTimeStamp());

	assertEquals("http://www.baz.org", allUrls.get(2).getUrl());
	assertEquals("baz", allUrls.get(2).getTitle());
	assertEquals("user3", allUrls.get(2).getUser());
	
	assertNotNull(allUrls.get(2).getTimeStamp());
    }

    private String addUrlAndGetItsId() {
	urlsData.addUrl(ExampleUrls.testUrl1);

	DB urlsDb = mongoClient.getDB(DbProperties.DATABASE_NAME);
	DBCollection col = urlsDb.getCollection("urls");

	DBObject foundEntry = col.findOne(new BasicDBObject("url",
		"http://www.foo.org"));

	return foundEntry.get("_id").toString();
    }

    private void checkIfItsTheCorrectUrl(
	    Map<String, Link> returnedUrlRepresentation) {
	Link foundUrl = returnedUrlRepresentation.get("url");

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
		.getCollection(DbProperties.URLS_NAME).drop();
	restapi.stopServer();
    }

    @AfterClass
    public static void stopEmbeddedMongo() {
	embeddedMongo.stopEmbeddedMongo();
    }
}
