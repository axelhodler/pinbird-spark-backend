package earth.xor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

	startMongoExecutable(runtime, mongodConfig);

	mongo = new MongoClient("localhost", port);
	db = mongo.getDB("testdb");
    }

    private static void startMongoExecutable(MongodStarter runtime,
	    IMongodConfig mongodConfig) throws IOException {
	MongodExecutable mongodExecutable = runtime.prepare(mongodConfig);
	mongodExecutable.start();
    }

    @Test
    public void testGettingCount() {
	DBCollection col = db.getCollection("test");
	col.insert(new BasicDBObject("name", "pete"));

	assertEquals(1, col.getCount());
    }

    @Test
    public void testAccessingUrlDB() {
	UrlDB urlDB = new UrlDB(mongo, "test");

	assertNotNull(urlDB.getMongoClient());
	assertTrue(urlDB.getMongoClient() instanceof MongoClient);
    }

    @Test
    public void testAddingAUrl() {
	UrlDB urlDB = new UrlDB(mongo, "test");

	urlDB.addUrl("http://www.foo.org", "foo", "user");
	assertEquals(1, urlDB.getUrls().size());
    }

    @AfterClass
    public static void stopEmbeddedMongo() {
	if (mongodExecutable != null) {
	    mongodExecutable.stop();
	}
    }

}
