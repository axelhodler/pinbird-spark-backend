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
import earth.xor.db.LinkFields;
import earth.xor.db.MongoLinksDatastore;

public class TestEmbedMongo {
    private MongoClient mongo;
    private MongoLinksDatastore linksData;

    @BeforeClass
    public static void setUpEmbeddedMongo() throws UnknownHostException,
            IOException {
        EmbedMongo.getInstance();
    }

    @Before
    public void setUpTests() throws UnknownHostException {
        this.mongo = new MongoClient("localhost", EmbedMongoProperties.PORT);

        this.linksData = new MongoLinksDatastore(mongo);
    }

    @Test
    public void testGettingCount() {

        DBCollection col = mongo.getDB(LinkFields.DATABASE_NAME)
                .getCollection("test");
        col.insert(new BasicDBObject("name", "pete"));

        assertEquals(1, col.getCount());
    }
}
