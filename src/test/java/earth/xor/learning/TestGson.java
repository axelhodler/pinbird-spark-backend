package earth.xor.learning;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gson.Gson;

import earth.xor.db.Url;

public class TestGson {

    @Test
    public void testObjectToJsonConversion() {
	Url url = new Url("testurl", "testtitle", "testuser");
	
	Gson gson = new Gson();
	
	String urlAsJson = gson.toJson(url);
	
	assertEquals("{\"url\":\"testurl\","
		+ "\"title\":\"testtitle\","
		+ "\"user\":\"testuser\"}", urlAsJson);
    }
}
