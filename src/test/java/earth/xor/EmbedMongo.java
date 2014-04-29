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

public abstract class EmbedMongo {

    public static MongodExecutable getEmbeddedMongoExecutable()
            throws UnknownHostException, IOException {
        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(EmbedMongoProperties.PORT, Network
                        .localhostIsIPv6())).build();

        MongodStarter runtime = MongodStarter.getDefaultInstance();

        return runtime.prepare(mongodConfig);
    }
}
