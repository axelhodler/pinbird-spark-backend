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
    public void canGetLink() {
        List<Bookmark> links = createTestLinks();
        JSONObject mainJsonObject = conformToEmberStandards(links);
        when(transformator.listOfLinksToJson(anyListOf(Bookmark.class)))
                .thenReturn(mainJsonObject.toJSONString());
        when(facade.getLinks()).thenReturn(links);

        Object jsonString = route.handle(req, resp);

        verify(facade, times(1)).getLinks();
        verify(transformator, times(1)).listOfLinksToJson(
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

    private JSONObject conformToEmberStandards(List<Bookmark> links) {
        JSONObject mainJsonObject = new JSONObject();
        mainJsonObject.put(BookmarkFields.BOOKMARKS,
                createTestLinksArray(links));
        return mainJsonObject;
    }

    private JSONArray createTestLinksArray(List<Bookmark> links) {
        JSONArray linksArray = new JSONArray();
        for (Bookmark l : links) {
            JSONObject jo = new JSONObject();
            jo.put(BookmarkFields.ID, l.getObjectId());
            jo.put(BookmarkFields.URL, l.getUrl());
            jo.put(BookmarkFields.TITLE, l.getTitle());
            jo.put(BookmarkFields.USER, l.getUser());
            jo.put(BookmarkFields.TIMESTAMP, l.getTimeStamp());
            linksArray.add(jo);
        }
        return linksArray;
    }

    private List<Bookmark> createTestLinks() {
        List<Bookmark> links = new ArrayList<>();
        links.add(BookmarkObjects.testLink1);
        links.add(BookmarkObjects.testLink2);
        return links;
    }
}
