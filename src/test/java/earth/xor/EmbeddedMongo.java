package earth.xor;

import java.io.IOException;
import java.net.UnknownHostException;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

public class EmbeddedMongo {

    private MongodExecutable mongodExecutable;
    
    public void launchEmbeddedMongo(int port) throws UnknownHostException, IOException {
	IMongodConfig mongodConfig = new MongodConfigBuilder()
	.version(Version.V2_4_5)
	.net(new Net(port, Network.localhostIsIPv6())).build();
	
	MongodStarter runtime = MongodStarter.getDefaultInstance();
	
	startMongoExecutable(runtime, mongodConfig);
    }
    
    public void stopEmbeddedMongo() {
	if (mongodExecutable != null) {
	    mongodExecutable.stop();
	}
    }
    
    private void startMongoExecutable(MongodStarter runtime,
	    IMongodConfig mongodConfig) throws IOException {
	mongodExecutable = runtime.prepare(mongodConfig);
	mongodExecutable.start();
    }
}
