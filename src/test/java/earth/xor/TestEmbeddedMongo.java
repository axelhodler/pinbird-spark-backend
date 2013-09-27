package earth.xor;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

public class TestEmbeddedMongo {

    private static DB db;
    private static MongodExecutable mongodExecutable;
    private static MongoClient mongo;

    @BeforeClass
    public static void setUpEmbeddedMongo() throws UnknownHostException,
	    IOException {
	int port = 12345;

	IMongodConfig mongodConfig = new MongodConfigBuilder()
		.version(Version.V2_4_5)
		.net(new Net(port, Network.localhostIsIPv6())).build();

	MongodStarter runtime = MongodStarter.getDefaultInstance();

	MongodExecutable mongodExecutable = null;

	mongodExecutable = runtime.prepare(mongodConfig);
	MongodProcess mongod = mongodExecutable.start();

	mongo = new MongoClient("localhost", port);
	db = mongo.getDB("testdb");
    }

    @Test
    public void testGettingCount() {
	DBCollection col = db.getCollection("test");
	col.insert(new BasicDBObject("name", "pete"));
	
	assertEquals(1, col.getCount());
    }
    
    @Test
    public void testAccessingUrlDB() {
	UrlDB urlDB = new UrlDB(mongo);
	
	assertNotNull(urlDB.getMongoClient());
	assertTrue(urlDB.getMongoClient() instanceof MongoClient);
    }

    @AfterClass
    public static void stopEmbeddedMongo() {
	if (mongodExecutable != null) {
	    mongodExecutable.stop();
	}	    
    }

}
