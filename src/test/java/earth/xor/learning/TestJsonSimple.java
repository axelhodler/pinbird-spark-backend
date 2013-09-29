package earth.xor.learning;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.Assert;
import org.junit.Test;

public class TestJsonSimple {
    private String jsonExample = "{ \"url\":\"http:\\\\www.foo.org\", "
	    + "\"title\":\"foo\", " + "\"user\":\"user\"}";
    
    @Test
    public void testGettingTheJsonObjectFromString() {
	JSONObject url = (JSONObject) JSONValue.parse(jsonExample);
	
	Assert.assertEquals("foo", url.get("title"));
    }
}
