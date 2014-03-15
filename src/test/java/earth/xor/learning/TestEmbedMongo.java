package earth.xor.learning;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import earth.xor.EmbedMongo;
import earth.xor.EmbedMongoProperties;
import earth.xor.helpers.IntegrationTest;
import earth.xor.model.LinkFields;

@Category(IntegrationTest.class)
public class TestEmbedMongo {
    private MongoClient mongo;

    @BeforeClass
    public static void setUpEmbeddedMongo() throws UnknownHostException,
            IOException {
        EmbedMongo.getInstance();
    }

    @Before
    public void setUpTests() throws UnknownHostException {
        this.mongo = new MongoClient("localhost", EmbedMongoProperties.PORT);
    }

    @Test
    public void testGettingCount() {
        DBCollection col = mongo.getDB(LinkFields.DATABASE_NAME)
                .getCollection("test");
        col.insert(new BasicDBObject("name", "pete"));

        assertEquals(1, col.getCount());
    }
}
