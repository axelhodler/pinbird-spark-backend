package earth.xor.rest.transformation;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import earth.xor.db.Link;

public class TestTransformator {

    private Transformator trans;
    private String jsonExample = "{ \"url\":\"http://www.foo.org\", "
            + "\"title\":\"foo\", " + "\"user\":\"user\"}";

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

}
