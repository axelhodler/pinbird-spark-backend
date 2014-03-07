package earth.xor.db;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.UnknownHostException;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import earth.xor.ExampleLinks;

@RunWith(MockitoJUnitRunner.class)
public class TestMongoLinksDatastore {
    @Mock
    private MongoClient mongoClient;
    @Mock
    private DB db;
    @Mock
    private DBCollection col;

    private MongoLinksDatastore linksData;

    @Before
    public void setUpTests() throws UnknownHostException {
        this.mongoClient = mock(MongoClient.class);
        this.linksData = new MongoLinksDatastore(mongoClient);

        when(mongoClient.getDB(anyString())).thenReturn(db);
        when(db.getCollection(anyString())).thenReturn(col);
    }

    @Test
    public void linkCanBeAdded() {
        linksData.addLink(ExampleLinks.testLink1);

        verify(col, times(1)).insert(any(BasicDBObject.class));
    }

    @Test
    public void canGetAllLinks() {
        linksData.getLinks();

        verify(col, times(1)).find();
    }

    @Test
    public void canGetLinkViaId() {
        linksData.getLinkById(new ObjectId().toString());

        verify(col, times(1)).findOne(any(BasicDBObject.class));
    }
}
