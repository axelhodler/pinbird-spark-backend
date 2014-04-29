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

public class EmbedMongo {

    private MongodExecutable mongodExecutable;
    private static EmbedMongo uniqueInstance;

    private EmbedMongo() {
    }

    public static EmbedMongo getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new EmbedMongo();
            tryToStartEmbeddedMongo();
        }
        return uniqueInstance;
    }

    private static void tryToStartEmbeddedMongo() {
        try {
            uniqueInstance.startEmbeddedMongo(EmbedMongoProperties.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startEmbeddedMongo(int port) throws UnknownHostException,
            IOException {
        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(port, Network.localhostIsIPv6())).build();

        MongodStarter runtime = MongodStarter.getDefaultInstance();

        startMongoExecutable(runtime, mongodConfig);
    }

    private void startMongoExecutable(MongodStarter runtime,
            IMongodConfig mongodConfig) throws IOException {
        mongodExecutable = runtime.prepare(mongodConfig);
        mongodExecutable.start();
    }

    public void stopEmbeddedMongo() {
        if (mongodExecutable != null) {
            mongodExecutable.stop();
            uniqueInstance = null;
        }
    }

}
