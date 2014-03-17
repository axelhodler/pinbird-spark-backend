package org.xorrr.integrationtests;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.xorrr.util.EnvironmentVars;
import org.xorrr.util.HttpHeaderKeys;
import org.xorrr.util.JsonAccessor;
import org.xorrr.util.LinkObjects;

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
import earth.xor.db.DatastoreFacade;
import earth.xor.db.LinksDatastore;
import earth.xor.db.MongoLinksDatastore;
import earth.xor.helpers.IntegrationTest;
import earth.xor.model.Bookmark;
import earth.xor.model.BookmarkFields;
import earth.xor.rest.SparkFacade;
import earth.xor.rest.SparkRestApi;
import earth.xor.rest.routes.GetAllLinksRoute;
import earth.xor.rest.routes.GetLinkByIdRoute;
import earth.xor.rest.routes.PostLinkRoute;
import earth.xor.rest.routes.Routes;
import earth.xor.rest.transformation.JSONTransformator;

@Category(IntegrationTest.class)
public class TestRestApi {
    private static Gson gson;
    private static MongoClient mongoClient;
    private static LinksDatastore linksData;

    @BeforeClass
    public static void setUpEmbeddedMongo() throws UnknownHostException,
            IOException {
        EmbedMongo.getInstance();

        gson = new Gson();
        mongoClient = new MongoClient("localhost", EmbedMongoProperties.PORT);
        linksData = new MongoLinksDatastore(mongoClient);

        RestAssured.port = Integer
                .parseInt(System.getenv(EnvironmentVars.PORT));

        DatastoreFacade facade = new DatastoreFacade(linksData);
        JSONTransformator transformator = new JSONTransformator();

        SparkRestApi rest = new SparkRestApi(new SparkFacade());
        rest.setPort(Integer.valueOf(System.getenv(EnvironmentVars.PORT)));
        rest.createGETlinkByIdRoute(new GetLinkByIdRoute(facade, transformator));
        rest.createPOSTlinksRoute(new PostLinkRoute(facade, transformator));
        rest.createGETlinksRoute(new GetAllLinksRoute(facade, transformator));
    }

    @Test
    public void authenticationNecessary() {
        expect().statusCode(400).when().post(Routes.POST_BOOKMARK);
    }

    @Test
    public void dealsWithIncorrectAuth() {
        given().header(HttpHeaderKeys.Authorization, "wrong").expect()
                .statusCode(401).when().post(Routes.POST_BOOKMARK);
    }

    @Test
    public void dealWithMissingPayload() {
        given().header(HttpHeaderKeys.Authorization,
                System.getenv(EnvironmentVars.PW)).expect().statusCode(400)
                .when().post(Routes.POST_BOOKMARK);
    }

    @Test
    public void canPostLink() {
        addAlinkViaRestApi();

        DBObject foundEntry = getSavedLinkFromDb();

        assertEquals("http://www.foo.org", foundEntry.get(BookmarkFields.URL));
        assertEquals("foo", foundEntry.get(BookmarkFields.TITLE));
        assertEquals("test", foundEntry.get(BookmarkFields.USER));
    }

    @Test
    public void canGetListOfAllSavedLinks() {
        linksData.addLink(LinkObjects.testLink1);
        linksData.addLink(LinkObjects.testLink2);
        linksData.addLink(LinkObjects.testLink3);

        expect().header(HttpHeaderKeys.ACAOrigin, equalTo("*")).when()
                .get(Routes.GET_ALL_BOOKMARKS);
        String jsonResponse = expect().when().get(Routes.GET_ALL_BOOKMARKS)
                .asString();

        Type type = new TypeToken<Map<String, List<Bookmark>>>() {
        }.getType();
        Map<String, List<Bookmark>> returnedUrls = new HashMap<String, List<Bookmark>>();
        returnedUrls = gson.fromJson(jsonResponse, type);
        checkIfPreviouslyAddedLinksAreShown(returnedUrls);
    }

    @Test
    public void canGetSavedLinkById() {
        String id = addLinkAndGetItsId();

        String jsonString = expect()
                .header(HttpHeaderKeys.ACAOrigin, equalTo("*")).when()
                .get(Routes.BASE + "/" + id).asString();

        assertTrue(isIdSurroundedWithDoubleQuotes(id, jsonString));
        Type type = new TypeToken<Map<String, Bookmark>>() {
        }.getType();
        Map<String, Bookmark> returnedUrlRepresentation = new HashMap<String, Bookmark>();
        returnedUrlRepresentation = gson.fromJson(jsonString, type);
        checkIfItsTheCorrectlink(returnedUrlRepresentation);
    }

    private boolean isIdSurroundedWithDoubleQuotes(String id, String jsonString) {
        return jsonString.contains("\"" + id + "\"");
    }

    private void addAlinkViaRestApi() {
        String jsonString = given()
                .body(JsonAccessor.getPostRequestBody())
                .header(HttpHeaderKeys.Authorization,
                        System.getenv(EnvironmentVars.PW)).expect()
                .header(HttpHeaderKeys.ACAOrigin, equalTo("*")).when()
                .post(Routes.BASE).asString();

        JSONObject mainObj = (JSONObject) JSONValue.parse(jsonString);
        JSONObject link = (JSONObject) mainObj.get("link");

        assertEquals("http://www.foo.org", link.get(BookmarkFields.URL).toString());
        assertEquals("foo", link.get(BookmarkFields.TITLE).toString());
        assertEquals("test", link.get(BookmarkFields.USER).toString());
    }

    private DBObject getSavedLinkFromDb() {
        DB urlsDb = mongoClient.getDB(BookmarkFields.DATABASE_NAME);
        DBCollection col = urlsDb.getCollection(BookmarkFields.BOOKMARKS);

        return col.findOne(new BasicDBObject(BookmarkFields.URL,
                "http://www.foo.org"));
    }

    private void checkIfPreviouslyAddedLinksAreShown(
            Map<String, List<Bookmark>> returnedUrls) {

        ArrayList<Bookmark> allUrls = (ArrayList<Bookmark>) returnedUrls
                .get(BookmarkFields.BOOKMARKS);

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
        linksData.addLink(LinkObjects.testLink1);

        DB linksDb = mongoClient.getDB(BookmarkFields.DATABASE_NAME);
        DBCollection col = linksDb.getCollection(BookmarkFields.BOOKMARKS);

        DBObject foundEntry = col.findOne(new BasicDBObject(BookmarkFields.URL,
                "http://www.foo.org"));

        return foundEntry.get(BookmarkFields.ID).toString();
    }

    private void checkIfItsTheCorrectlink(
            Map<String, Bookmark> returnedUrlRepresentation) {
        Bookmark foundLink = returnedUrlRepresentation.get("link");

        assertEquals("http://www.foo.org", foundLink.getUrl());
        assertEquals("foo", foundLink.getTitle());
        assertEquals("user1", foundLink.getUser());
    }

    @After
    public void dropCollection() {
        mongoClient.getDB(BookmarkFields.DATABASE_NAME)
                .getCollection(BookmarkFields.BOOKMARKS).drop();
    }
}
