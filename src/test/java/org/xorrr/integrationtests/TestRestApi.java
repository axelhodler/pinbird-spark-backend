package org.xorrr.integrationtests;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.xorrr.util.BookmarkObjects;
import org.xorrr.util.EnvironmentVars;
import org.xorrr.util.HttpHeaderKeys;
import org.xorrr.util.JsonAccessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayway.restassured.RestAssured;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.MongodExecutable;
import earth.xor.EmbeddedMongo;
import earth.xor.EmbeddedMongoProperties;
import earth.xor.db.BookmarksDatastore;
import earth.xor.db.DatastoreFacade;
import earth.xor.db.MongoBookmarksDatastore;
import earth.xor.helpers.IntegrationTest;
import earth.xor.model.Bookmark;
import earth.xor.model.BookmarkFields;
import earth.xor.rest.SparkFacade;
import earth.xor.rest.SparkRestApi;
import earth.xor.rest.routes.DeleteBookmarkByIdRoute;
import earth.xor.rest.routes.GetAllBookmarksRoute;
import earth.xor.rest.routes.GetBookmarkByIdRoute;
import earth.xor.rest.routes.OptionsForIdRoute;
import earth.xor.rest.routes.OptionsRoute;
import earth.xor.rest.routes.PostBookmarkRoute;
import earth.xor.rest.routes.Routes;
import earth.xor.rest.transformation.JSONTransformator;

@Category(IntegrationTest.class)
public class TestRestApi {
    private static Gson gson;
    private static MongoClient mongoClient;
    private static BookmarksDatastore bookmarksData;
    private static MongodExecutable mongodExe;

    @BeforeClass
    public static void setUpEmbeddedMongo() throws UnknownHostException,
            IOException {
        mongodExe = EmbeddedMongo.getEmbeddedMongoExecutable();
        mongodExe.start();

        gson = new Gson();
        mongoClient = new MongoClient("localhost", EmbeddedMongoProperties.PORT);
        bookmarksData = new MongoBookmarksDatastore(mongoClient);

        RestAssured.port = Integer
                .parseInt(System.getenv(EnvironmentVars.PORT));

        DatastoreFacade facade = new DatastoreFacade(bookmarksData);
        JSONTransformator transformator = new JSONTransformator();

        SparkRestApi rest = new SparkRestApi(new SparkFacade());
        rest.setPort(Integer.valueOf(System.getenv(EnvironmentVars.PORT)));
        rest.createGETbookmarkByIdRoute(new GetBookmarkByIdRoute(facade,
                transformator));
        rest.createPOSTbookmarksRoute(new PostBookmarkRoute(facade,
                transformator));
        rest.createGETbookmarksRoute(new GetAllBookmarksRoute(facade,
                transformator));
        rest.createDELETEbookmarkByIdRoute(new DeleteBookmarkByIdRoute(facade));
        rest.createOPTIONSbookmarksRoute(new OptionsRoute());
        rest.createOPTIONSForBookmarkIdRoute(new OptionsForIdRoute());
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
    public void canPostBookmark() {
        addBookmarkViaRestApi();

        DBObject bookmarkDbo = getSavedBookmarkFromDb();

        assertEquals("http://www.foo.org", bookmarkDbo.get(BookmarkFields.URL));
        assertEquals("foo", bookmarkDbo.get(BookmarkFields.TITLE));
        assertEquals("test", bookmarkDbo.get(BookmarkFields.USER));
    }

    @Test
    public void canGetListOfAllSavedBookmarks() {
        bookmarksData.addBookmark(BookmarkObjects.testBookmark1);
        bookmarksData.addBookmark(BookmarkObjects.testBookmark2);
        bookmarksData.addBookmark(BookmarkObjects.testBookmark3);

        String jsonResponse = getListOfSavedBookmarks();

        Type type = new TypeToken<Map<String, List<Bookmark>>>() {
        }.getType();
        Map<String, List<Bookmark>> returnedUrls = new HashMap<String, List<Bookmark>>();
        returnedUrls = gson.fromJson(jsonResponse, type);
        checkIfPreviouslyAddedBookmarksAreShown(returnedUrls);
    }

    @Test
    public void canGetSavedBookmarkById() {
        addTestBookmark();
        String id = getAddedBookmarkById();

        String jsonString = getSavedBookmarkViaRest(id);

        assertTrue(isIdSurroundedWithDoubleQuotes(id, jsonString));
        Type type = new TypeToken<Map<String, Bookmark>>() {
        }.getType();
        Map<String, Bookmark> returnedUrlRepresentation = new HashMap<String, Bookmark>();
        returnedUrlRepresentation = gson.fromJson(jsonString, type);
        checkIfItsTheCorrectBookmark(returnedUrlRepresentation);
    }

    @Test
    public void canDeleteSavedBookmarkById() {
        addTestBookmark();
        String id = getAddedBookmarkById();

        deleteSavedBookmarkViaRest(id);

        DB bookmarksDb = mongoClient.getDB(BookmarkFields.DATABASE_NAME);
        DBCollection col = bookmarksDb.getCollection(BookmarkFields.BOOKMARKS);

        assertNull(col.findOne(new BasicDBObject(BookmarkFields.URL,
                "http://www.foo.org")));
    }

    @Test
    public void canGetOptionsBaseRoute() {
        expect().header(HttpHeaderKeys.ACAOrigin, "*").and()
                .header(HttpHeaderKeys.ACAMethods, "GET, POST").when()
                .options(Routes.BASE);
    }

    @Test
    public void canGetOptionsForIdRoute() {
        addTestBookmark();
        String id = getAddedBookmarkById();

        expect().header(HttpHeaderKeys.ACAOrigin, "*").and()
                .header(HttpHeaderKeys.ACAMethods, "GET, DELETE").when()
                .options(Routes.BASE + "/" + id);
    }

    @After
    public void dropCollection() {
        mongoClient.getDB(BookmarkFields.DATABASE_NAME)
                .getCollection(BookmarkFields.BOOKMARKS).drop();
    }

    private void deleteSavedBookmarkViaRest(String id) {
        given().header(HttpHeaderKeys.Authorization,
                System.getenv(EnvironmentVars.PW)).expect()
                .header(HttpHeaderKeys.ACAOrigin, "*").when()
                .delete(Routes.BASE + "/" + id);
    }

    private String getSavedBookmarkViaRest(String id) {
        return expect().header(HttpHeaderKeys.ACAOrigin, equalTo("*")).when()
                .get(Routes.BASE + "/" + id).asString();
    }

    private String getListOfSavedBookmarks() {
        return expect().header(HttpHeaderKeys.ACAOrigin, equalTo("*")).when()
                .get(Routes.GET_ALL_BOOKMARKS).asString();
    }

    private boolean isIdSurroundedWithDoubleQuotes(String id, String jsonString) {
        return jsonString.contains("\"" + id + "\"");
    }

    private void addBookmarkViaRestApi() {
        given().body(JsonAccessor.getPostRequestBody())
                .header(HttpHeaderKeys.Authorization,
                        System.getenv(EnvironmentVars.PW)).expect()
                .header(HttpHeaderKeys.ACAOrigin, equalTo("*")).when()
                .post(Routes.BASE).asString();
    }

    private DBObject getSavedBookmarkFromDb() {
        DB urlsDb = mongoClient.getDB(BookmarkFields.DATABASE_NAME);
        DBCollection col = urlsDb.getCollection(BookmarkFields.BOOKMARKS);

        return col.findOne(new BasicDBObject(BookmarkFields.URL,
                "http://www.foo.org"));
    }

    private void checkIfPreviouslyAddedBookmarksAreShown(
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

    private void addTestBookmark() {
        bookmarksData.addBookmark(BookmarkObjects.testBookmark1);
    }

    private String getAddedBookmarkById() {
        DB bookmarksDb = mongoClient.getDB(BookmarkFields.DATABASE_NAME);
        DBCollection col = bookmarksDb.getCollection(BookmarkFields.BOOKMARKS);

        DBObject foundEntry = col.findOne(new BasicDBObject(BookmarkFields.URL,
                "http://www.foo.org"));

        return foundEntry.get(BookmarkFields.ID).toString();
    }

    private void checkIfItsTheCorrectBookmark(
            Map<String, Bookmark> returnedUrlRepresentation) {
        Bookmark foundBookmark = returnedUrlRepresentation
                .get(BookmarkFields.BOOKMARK);

        assertEquals("http://www.foo.org", foundBookmark.getUrl());
        assertEquals("foo", foundBookmark.getTitle());
        assertEquals("user1", foundBookmark.getUser());
    }

}
