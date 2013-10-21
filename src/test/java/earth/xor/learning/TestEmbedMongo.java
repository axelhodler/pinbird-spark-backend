package earth.xor.learning;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import earth.xor.EmbedMongo;
import earth.xor.EmbedMongoProperties;
import earth.xor.db.LinksProp;
import earth.xor.db.LinksDatastore;

public class TestEmbedMongo {
    private MongoClient mongo;
    private LinksDatastore linksData;

    @BeforeClass
    public static void setUpEmbeddedMongo() throws UnknownHostException,
            IOException {
        EmbedMongo.getInstance();
    }

    @Before
    public void setUpTests() throws UnknownHostException {
        this.mongo = new MongoClient("localhost", EmbedMongoProperties.PORT);

        this.linksData = new LinksDatastore(mongo);
    }

    @Test
    public void testGettingCount() {

        DBCollection col = mongo.getDB(LinksProp.DATABASE_NAME)
                .getCollection("test");
        col.insert(new BasicDBObject("name", "pete"));

        assertEquals(1, col.getCount());
    }
}
