package earth.xor;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.parsing.Parser;
import com.mongodb.MongoClient;

import earth.xor.rest.SparkRestApi;

public class TestRestApi {

    private SparkRestApi restapi;
    
    private String jsonTestString = "{\"title\":\"foo\"}"; 
    
    private static int port = 12345;
    private static EmbeddedMongo embeddedMongo;
    
    @BeforeClass
    public static void setUpEmbeddedMongo() throws UnknownHostException,
	    IOException {
	embeddedMongo = new EmbeddedMongo();
	embeddedMongo.launchEmbeddedMongo(port);
    }
    
    @Before
    public void setUpRestApi() {
	restapi = new SparkRestApi();
	restapi.launchServer();
    }
    
    @Test
    public void testAddingAUrlThroughTheRestApi() {
	
	RestAssured.port = 4567;
		
	given().body(jsonTestString).expect().contentType("application/json")
		.body("title", equalTo("foo")).when().post("/urls");
    }
    
    @After
    public void stopRestApi() {
	restapi.stopServer();
    }
    
    @AfterClass
    public static void stopEmbeddedMongo() {
	embeddedMongo.stopEmbeddedMongo();
    }
}
