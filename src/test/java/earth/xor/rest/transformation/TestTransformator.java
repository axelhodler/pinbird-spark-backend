package earth.xor.rest.transformation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.xorrr.util.JsonAccessor;
import org.xorrr.util.BookmarkObjects;

import earth.xor.model.Bookmark;

public class TestTransformator {

    private JSONTransformator trans;
    private String jsonExample = "{\"bookmark\":{\"url\":\"http://www.foo.org\", "
            + "\"title\":\"foo\", " + "\"user\":\"user\"}}";
    private List<Bookmark> links;

    private String linkInJson = "{\"bookmark\":{\"timestamp\":null,"
            + "\"title\":\"foo\",\"_id\":null,\"user\":\"user1\",\"url\""
            + ":\"http:\\/\\/www.foo.org\"}}";

    @Before
    public void setUp() {
        trans = new JSONTransformator();
    }

    @Test
    public void canTransformStringToLink() {
        Bookmark l = trans.jsonToLink(jsonExample);

        assertEquals("http://www.foo.org", l.getUrl());
        assertEquals("foo", l.getTitle());
        assertEquals("user", l.getUser());
    }

    @Test
    public void canTransformListOfLinksToJson() {
        links = new ArrayList<>();
        links.add(BookmarkObjects.testLink1);
        links.add(BookmarkObjects.testLink2);

        String json = trans.listOfLinksToJson(links);
        assertEquals(json, JsonAccessor.getExampleBookmarks());
    }

    @Test
    public void canTransformLinkToJson() {
        String json = trans.linkToJson(BookmarkObjects.testLink1);

        assertEquals(json, linkInJson);
    }
}
