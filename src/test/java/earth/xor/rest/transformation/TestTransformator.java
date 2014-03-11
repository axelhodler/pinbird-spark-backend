package earth.xor.rest.transformation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.xorrr.util.LinkObjects;

import earth.xor.db.Link;
import earth.xor.db.LinkFields;

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
}
