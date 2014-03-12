package earth.xor.rest.transformation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.xorrr.util.LinkObjects;

import earth.xor.db.Link;

public class TestTransformator {

    private Transformator trans;
    private String jsonExample = "{\"url\":\"http://www.foo.org\", "
            + "\"title\":\"foo\", " + "\"user\":\"user\"}";
    private List<Link> links;

    private String linksInJson = "{\"links\":[{\"timestamp\":null,"
            + "\"title\":\"foo\",\"_id\":null,\"user\":\"user1\",\"url\""
            + ":\"http:\\/\\/www.foo.org\"},{\"timestamp\":null,\"title\""
            + ":\"bar\",\"_id\":null,\"user\":\"user2\",\"url\""
            + ":\"http:\\/\\/www.bar.org\"}]}";

    private String linkInJson = "{\"link\":{\"timestamp\":null,"
            + "\"title\":\"foo\",\"_id\":null,\"user\":\"user1\",\"url\""
            + ":\"http:\\/\\/www.foo.org\"}}";

    @Before
    public void setUp() {
        trans = new Transformator();
    }

    @Test
    public void canTransformStringToLink() {
        Link l = trans.toLink(jsonExample);

        assertEquals("http://www.foo.org", l.getUrl());
        assertEquals("foo", l.getTitle());
        assertEquals("user", l.getUser());
    }

    @Test
    public void canTransformListOfLinksToJson() {
        links = new ArrayList<>();
        links.add(LinkObjects.testLink1);
        links.add(LinkObjects.testLink2);

        String json = trans.toJson(links);
        assertEquals(json, linksInJson);
    }

    @Test
    public void canTransformLinkToJson() {
        String json = trans.linkToJson(LinkObjects.testLink1);

        assertEquals(json, linkInJson);
    }
}
