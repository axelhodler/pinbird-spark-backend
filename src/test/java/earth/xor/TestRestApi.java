package earth.xor;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

import earth.xor.db.DbProperties;
import earth.xor.rest.SparkRestApi;

public class TestRestApi {

    private SparkRestApi restapi;
    
    private String jsonTestString = "{\"title\":\"foo\"}"; 
    
    private static int port = 12345;
    private static EmbeddedMongo embeddedMongo;
    private MongoClient mongoClient;
    
    @BeforeClass
    public static void setUpEmbeddedMongo() throws UnknownHostException,
	    IOException {
	embeddedMongo = new EmbeddedMongo();
	embeddedMongo.launchEmbeddedMongo(port);
    }
    
    @Before
    public void setUpRestApi() throws UnknownHostException {
	this.mongoClient = new MongoClient("localhost", port);
	
	restapi = new SparkRestApi(mongoClient);
	restapi.launchServer();
    }
    
    @Test
    public void testAddingAUrlThroughTheRestApi() {
	
	RestAssured.port = 4567;
		
	given().body(jsonTestString).expect().contentType("application/json")
		.body("title", equalTo("foo")).when().post("/urls");
	
	DB urlsDb = mongoClient.getDB(DbProperties.DATABASE_NAME);
	DBCollection col = urlsDb.getCollection("urls");

	assertEquals("foo", col.findOne(new BasicDBObject("title", "foo")).get("title"));
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
