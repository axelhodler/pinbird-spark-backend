package earth.xor.learning;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import earth.xor.db.Url;

public class TestGson {

    private String jsonExample = "{\"url\":\"testurl\","
    	+ "\"title\":\"testtitle\","
    	+ "\"user\":\"testuser\"}";
    
    private Url urlExample;

    private Gson gson;
    
    @Before
    public void setUpTests() {
	this.gson = new Gson();
	this.urlExample = new Url("testurl", "testtitle", "testuser");
    }
    
    @Test
    public void testObjectToJsonConversion() {
		
	String urlAsJson = this.gson.toJson(this.urlExample);
	
	assertEquals(jsonExample, urlAsJson);
    }
}
