package earth.xor.rest.transformation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.xorrr.util.BookmarkObjects;
import org.xorrr.util.JsonAccessor;

import earth.xor.model.Bookmark;

public class TestTransformator {

    private JSONTransformator trans;
    private String jsonExample = "{\"bookmark\":{\"url\":\"http://www.foo.org\", "
            + "\"title\":\"foo\", " + "\"user\":\"user\"}}";
    private List<Bookmark> bookmarks;

    private String bookmarkInJson = "{\"bookmark\":{\"_id\":null,"
            + "\"title\":\"foo\",\"user\":\"user1\",\"url\""
            + ":\"http:\\/\\/www.foo.org\",\"timestamp\":null}}";

    @Before
    public void setUp() {
        trans = new JSONTransformator();
    }

    @Test
    public void canTransformStringToBookmark() {
        Bookmark b = trans.jsonToBookmark(jsonExample);

        assertEquals("http://www.foo.org", b.getUrl());
        assertEquals("foo", b.getTitle());
        assertEquals("user", b.getUser());
    }

    @Test
    public void canTransformListOfBookmarkssToJson() {
        bookmarks = new ArrayList<>();
        bookmarks.add(BookmarkObjects.testBookmark1);
        bookmarks.add(BookmarkObjects.testBookmark2);

        String json = trans.listOfBookmarksToJson(bookmarks);
        assertEquals(json, JsonAccessor.getExampleBookmarks());
    }

    @Test
    public void canTransformBookmarkToJson() {
        String json = trans.bookmarkToJson(BookmarkObjects.testBookmark1);

        // The properties of a JSON Object are not ordered, therefore
        // comparing the String char by char makes no sense
        assertEquals(json.length(), bookmarkInJson.length());
    }
}
