package earth.xor;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.BeforeClass;

import com.mongodb.DB;
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
    
    @BeforeClass
    public static void setUpEmbeddedMongo() throws UnknownHostException,
	    IOException {
	int port = 12345;

	IMongodConfig mongodConfig = new MongodConfigBuilder()
		.version(Version.V2_4_5)
		.net(new Net(port, Network.localhostIsIPv6())).build();
	
	MongodStarter runtime = MongodStarter.getDefaultInstance();
	
	MongodExecutable mongodExecutable = null;
	
	try {
	    mongodExecutable = runtime.prepare(mongodConfig);
	    MongodProcess mongod = mongodExecutable.start();
	    
	    MongoClient mongo = new MongoClient("localhost", port);
	    db = mongo.getDB("testdb");
	} finally {
	    if (mongodExecutable != null)
		mongodExecutable.stop();
	}
    }
}
