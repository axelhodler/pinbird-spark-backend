package earth.xor.learning;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import earth.xor.model.Link;

public class TestGson {

    private String jsonExample = "{\"url\":\"testurl\","
            + "\"title\":\"testtitle\"," + "\"user\":\"testuser\"}";

    private Link urlExample;

    private Gson gson;

    @Before
    public void setUpTests() {
        this.gson = new Gson();
        this.urlExample = new Link.Builder().url("testurl").title("testtitle")
                .user("testuser").build();
    }

    @Test
    public void testObjectToJsonConversion() {

        String urlAsJson = this.gson.toJson(this.urlExample);

        assertEquals(jsonExample, urlAsJson);
    }

    @Test
    public void testJsonToJavaObjectConversion() {
        Link url = gson.fromJson(jsonExample, Link.class);

        assertEquals(urlExample.getTitle(), url.getTitle());
        assertEquals(urlExample.getUrl(), url.getUrl());
        assertEquals(urlExample.getUser(), url.getUser());
    }
}
