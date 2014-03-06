package earth.xor.learning;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.Test;

public class TestJsonSimple {
    private String jsonExample = "{ \"url\":\"http://www.foo.org\", "
            + "\"title\":\"foo\", " + "\"user\":\"user\"}";

    @Test
    public void getJsonObjectFromString() {
        JSONObject obj = (JSONObject) JSONValue.parse(jsonExample);

        assertEquals("foo", obj.get("title"));
    }

    @Test
    public void parseJsonObjectToString() {
        JSONObject obj = new JSONObject();

        obj.put("title", "foo");

        assertEquals("{\"title\":\"foo\"}", obj.toJSONString());
    }

    @Test
    public void createJsonStringFromMap() {
        Map<String, String> map = new HashMap<String, String>();

        map.put("title", "foo");

        assertEquals("{\"title\":\"foo\"}", JSONObject.toJSONString(map));
    }
}
