package earth.xor.rest.transformation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.xorrr.util.JsonAccessor;
import org.xorrr.util.LinkObjects;

import earth.xor.model.Bookmark;

public class TestTransformator {

    private JSONTransformator trans;
    private String jsonExample = "{\"link\":{\"url\":\"http://www.foo.org\", "
            + "\"title\":\"foo\", " + "\"user\":\"user\"}}";
    private List<Bookmark> links;

    private String linkInJson = "{\"link\":{\"timestamp\":null,"
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
        links.add(LinkObjects.testLink1);
        links.add(LinkObjects.testLink2);

        String json = trans.listOfLinksToJson(links);
        assertEquals(json, JsonAccessor.getExampleBookmarks());
    }

    @Test
    public void canTransformLinkToJson() {
        String json = trans.linkToJson(LinkObjects.testLink1);

        assertEquals(json, linkInJson);
    }
}
