package earth.xor.db;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.xorrr.util.LinkObjects;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import earth.xor.model.Link;
import earth.xor.model.LinkFields;

@RunWith(MockitoJUnitRunner.class)
public class TestMongoLinksDatastore {
    @Mock
    private MongoClient mongoClient;
    @Mock
    private DB db;
    @Mock
    private DBCollection col;
    @Mock
    private DBCursor curs;
    @Mock
    private Iterator<DBObject> dbos;
    @Mock
    private DBObject dbo;

    private MongoLinksDatastore linksData;

    private void mockDBObjectBehaviour() {
        when(dbo.get(LinkFields.URL)).thenReturn("u");
        when(dbo.get(LinkFields.TITLE)).thenReturn("t");
        when(dbo.get(LinkFields.USER)).thenReturn("us");
        when(dbo.get(LinkFields.TIMESTAMP)).thenReturn("ts");
    }

    @Before
    public void setUpTests() throws UnknownHostException {
        this.mongoClient = mock(MongoClient.class);
        this.linksData = new MongoLinksDatastore(mongoClient);

        when(mongoClient.getDB(anyString())).thenReturn(db);
        when(db.getCollection(anyString())).thenReturn(col);
    }

    @Test
    public void linkCanBeAdded() {
        linksData.addLink(LinkObjects.testLink1);

        verify(col, times(1)).insert(any(BasicDBObject.class));
    }

    @Test
    public void canGetAllLinks() {
        when(col.find()).thenReturn(curs);
        when(curs.iterator()).thenReturn(dbos);
        when(dbos.hasNext()).thenReturn(true, false);
        when(dbos.next()).thenReturn(dbo);
        mockDBObjectBehaviour();
        
        List<Link> links = linksData.getLinks();

        verify(col, times(1)).find();
        assertEquals("u", links.get(0).getUrl());
        assertEquals("t", links.get(0).getTitle());
        assertEquals("us", links.get(0).getUser());
        assertEquals("ts", links.get(0).getTimeStamp());
    }

    @Test
    public void canGetLinkViaId() {
        when(col.findOne(any(DBObject.class))).thenReturn(dbo);
        mockDBObjectBehaviour();

        Link l = linksData.getLinkById(new ObjectId().toString());

        verify(col, times(1)).findOne(any(BasicDBObject.class));
        assertEquals("u", l.getUrl());
        assertEquals("t", l.getTitle());
        assertEquals("us", l.getUser());
        assertEquals("ts", l.getTimeStamp());
    }
}
