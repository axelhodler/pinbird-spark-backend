package earth.xor;

import org.junit.Test;

import com.jayway.restassured.RestAssured;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class TestRestApi {

    @Test
    public void testAddingAUrlThroughTheRestApi() {
	
	RestAssured.port = 4567;
	given().parameters("url", "http://www.foo.org", "title", "foo",
			"user", "user").expect()
		.body("urls.title", equalTo("foo")).when().post("/urls");
    }
}
