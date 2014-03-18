package earth.xor.rest.routes;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.xorrr.util.EnvironmentVars;
import org.xorrr.util.HttpHeaderKeys;
import org.xorrr.util.BookmarkObjects;

import spark.Request;
import spark.Response;
import earth.xor.db.DatastoreFacade;
import earth.xor.model.Bookmark;
import earth.xor.model.BookmarkFields;
import earth.xor.rest.transformation.JSONTransformator;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class TestGetAllBookmarksRoute {
    @Mock
    Request req;
    @Mock
    Response resp;
    @Mock
    DatastoreFacade facade;
    @Mock
    JSONTransformator transformator;

    private GetAllBookmarksRoute route;

    @Before
    public void setUp() {
        route = new GetAllBookmarksRoute(facade, transformator);
    }

    @Test
    public void canGetBookmarks() {
        List<Bookmark> bookmarks = createTestLinks();
        JSONObject mainJsonObject = conformToEmberStandards(bookmarks);
        when(transformator.listOfBookmarksToJson(anyListOf(Bookmark.class)))
                .thenReturn(mainJsonObject.toJSONString());
        when(facade.getBookmarks()).thenReturn(bookmarks);

        Object jsonString = route.handle(req, resp);

        verify(facade, times(1)).getBookmarks();
        verify(transformator, times(1)).listOfBookmarksToJson(
                anyListOf(Bookmark.class));
        assertEquals(jsonString, mainJsonObject.toJSONString());
    }

    @Test
    public void accessControlAllowOriginHeaderUsed() {
        when(req.queryParams("pw")).thenReturn(
                System.getenv(EnvironmentVars.PW));
        when(req.body()).thenReturn("something");

        route.handle(req, resp);

        verify(resp, times(1)).header(HttpHeaderKeys.ACAOrigin, "*");
    }

    private JSONObject conformToEmberStandards(List<Bookmark> bookmarks) {
        JSONObject mainJsonObject = new JSONObject();
        mainJsonObject.put(BookmarkFields.BOOKMARKS,
                createTestBookmarksArray(bookmarks));
        return mainJsonObject;
    }

    private JSONArray createTestBookmarksArray(List<Bookmark> bookmarks) {
        JSONArray bookmarksArray = new JSONArray();
        for (Bookmark b : bookmarks) {
            JSONObject jo = new JSONObject();
            jo.put(BookmarkFields.ID, b.getObjectId());
            jo.put(BookmarkFields.URL, b.getUrl());
            jo.put(BookmarkFields.TITLE, b.getTitle());
            jo.put(BookmarkFields.USER, b.getUser());
            jo.put(BookmarkFields.TIMESTAMP, b.getTimeStamp());
            bookmarksArray.add(jo);
        }
        return bookmarksArray;
    }

    private List<Bookmark> createTestLinks() {
        List<Bookmark> bookmarks = new ArrayList<>();
        bookmarks.add(BookmarkObjects.testBookmark1);
        bookmarks.add(BookmarkObjects.testBookmark2);
        return bookmarks;
    }
}
