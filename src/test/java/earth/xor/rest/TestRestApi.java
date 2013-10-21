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

import earth.xor.EmbedMongo;
import earth.xor.EmbedMongoProperties;
import earth.xor.ExampleLinks;
import earth.xor.db.DbProperties;
import earth.xor.db.Link;
import earth.xor.db.LinksDatastore;

public class TestRestApi {

    private Gson gson;
    private MongoClient mongoClient;
    private LinksDatastore linksData;

    @BeforeClass
    public static void setUpEmbeddedMongo() throws UnknownHostException,
            IOException {
        EmbedMongo.getInstance();
    }

    @Before
    public void setUpRestApi() throws UnknownHostException {
        this.gson = new Gson();
        this.mongoClient = new MongoClient("localhost",
                EmbedMongoProperties.PORT);
        this.linksData = new LinksDatastore(mongoClient);

        RestAssured.port = 4567;
        SparkRestApi.getInstance().launchServer(mongoClient);
    }

    @Test
    public void testAddingALinkViaRestApi() {
        addAlinkViaRestApi();
        checkIfLinkWasAddedToDatabase();
    }

    @Test
    public void testGettingAListOfAllSavedUrls() {

        linksData.addLink(ExampleLinks.testLink1);
        linksData.addLink(ExampleLinks.testLink2);
        linksData.addLink(ExampleLinks.testLink3);

        String jsonResponse = expect().contentType("application/json").and()
                .header("Access-Control-Allow-Origin", equalTo("*")).when()
                .get(DbProperties.LINKS_ROUTE).asString();

        Type type = new TypeToken<Map<String, List<Link>>>() {
        }.getType();

        Map<String, List<Link>> returnedUrls = new HashMap<String, List<Link>>();

        returnedUrls = gson.fromJson(jsonResponse, type);

        checkIfPreviouslyAddedLinksAreShown(returnedUrls);
    }

    @Test
    public void testGettingASavedLinkById() {

        String id = addLinkAndGetItsId();

        String jsonString = expect().contentType("application/json").and()
                .header("Access-Control-Allow-Origin", equalTo("*")).when()
                .get("/links/" + id).asString();

        // id has to be surrounded with double quotes,
        // otherwise its not valid JSON
        assertTrue(jsonString.contains("\"" + id + "\""));

        Type type = new TypeToken<Map<String, Link>>() {
        }.getType();

        Map<String, Link> returnedUrlRepresentation = new HashMap<String, Link>();

        returnedUrlRepresentation = gson.fromJson(jsonString, type);

        checkIfItsTheCorrectlink(returnedUrlRepresentation);
    }

    private void addAlinkViaRestApi() {
        String jsonString = given().body(getLinksPostJsonString()).expect()
                .contentType("application/json").and()
                .header("Access-Control-Allow-Origin", equalTo("*")).when()
                .post("/links").asString();

        Link savedLink = gson.fromJson(jsonString, Link.class);

        assertEquals("http://www.foo.org", savedLink.getUrl());
        assertEquals("foo", savedLink.getTitle());
        assertEquals("test", savedLink.getUser());
    }

    private void checkIfLinkWasAddedToDatabase() {
        DB urlsDb = mongoClient.getDB(DbProperties.DATABASE_NAME);
        DBCollection col = urlsDb.getCollection(DbProperties.LINKS_NAME);

        DBObject foundEntry = col.findOne(new BasicDBObject(
                DbProperties.LINK_URL, "http://www.foo.org"));

        assertEquals("http://www.foo.org",
                foundEntry.get(DbProperties.LINK_URL));
        assertEquals("foo", foundEntry.get(DbProperties.LINK_TITLE));
        assertEquals("test", foundEntry.get(DbProperties.LINK_USER));
    }

    private String getLinksPostJsonString() {

        String jsonString = null;
        try {
            File testFile = new File(TestRestApi.class.getResource(
                    "/linksPost.JSON").toURI());
            jsonString = IOUtils.toString(new FileInputStream(testFile));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
        return jsonString;
    }

    private void checkIfPreviouslyAddedLinksAreShown(
            Map<String, List<Link>> returnedUrls) {

        ArrayList<Link> allUrls = (ArrayList<Link>) returnedUrls
                .get(DbProperties.LINKS_NAME);

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

    private String addLinkAndGetItsId() {
        linksData.addLink(ExampleLinks.testLink1);

        DB linksDb = mongoClient.getDB(DbProperties.DATABASE_NAME);
        DBCollection col = linksDb.getCollection(DbProperties.LINKS_NAME);

        DBObject foundEntry = col.findOne(new BasicDBObject(
                DbProperties.LINK_URL, "http://www.foo.org"));

        return foundEntry.get(DbProperties.LINK_ID).toString();
    }

    private void checkIfItsTheCorrectlink(
            Map<String, Link> returnedUrlRepresentation) {
        Link foundLink = returnedUrlRepresentation.get(DbProperties.LINK_URL);

        assertEquals("http://www.foo.org", foundLink.getUrl());
        assertEquals("foo", foundLink.getTitle());
        assertEquals("user1", foundLink.getUser());
    }

    @After
    public void dropCollection() {
        mongoClient.getDB(DbProperties.DATABASE_NAME)
                .getCollection(DbProperties.LINKS_NAME).drop();
    }
}
