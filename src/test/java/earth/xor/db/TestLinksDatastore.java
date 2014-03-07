package earth.xor.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import earth.xor.ExampleLinks;

@RunWith(MockitoJUnitRunner.class)
public class TestLinksDatastore {
    @Mock
    private MongoClient mongoClient;
    @Mock
    private DB db;
    @Mock
    private DBCollection col;

    private LinksDatastore linksData;

    @Before
    public void setUpTests() throws UnknownHostException {
        this.mongoClient = mock(MongoClient.class);
        this.linksData = new LinksDatastore(mongoClient);

        when(mongoClient.getDB(anyString())).thenReturn(db);
        when(db.getCollection(anyString())).thenReturn(col);
    }

    @Test
    public void linkCanBeAdded() {
        linksData.addLink(ExampleLinks.testLink1);

        verify(col, times(1)).insert(any(BasicDBObject.class));
    }

    @Ignore
    @Test
    public void testAddingAndGettingAllLinks() {
        linksData.addLink(ExampleLinks.testLink1);
        linksData.addLink(ExampleLinks.testLink2);
        linksData.addLink(ExampleLinks.testLink3);

        DBCursor linksCursor = linksData.getLinks();

        List<Link> linkList = createLinksArrayFromCursor(linksCursor);

        assertEquals("bar", linkList.get(1).getTitle());
        assertEquals("user3", linkList.get(2).getUser());

        assertNotNull(linkList.get(1).getTimeStamp());
    }

    @Ignore
    @Test
    public void testGettingAUrlById() {
        linksData.addLink(ExampleLinks.testLink1);

        DBCollection col = mongoClient.getDB(LinkFields.DATABASE_NAME)
                .getCollection(LinkFields.LINKS_NAME);

        DBObject savedUrl = col.findOne();

        DBObject obj = linksData.getLinkById(savedUrl.get(LinkFields.ID)
                .toString());

        assertEquals("foo", obj.get(LinkFields.TITLE).toString());
        assertEquals(savedUrl.get(LinkFields.ID), obj.get(LinkFields.ID));
    }

    private List<Link> createLinksArrayFromCursor(DBCursor linksCurs) {
        List<Link> linkList = new ArrayList<Link>();
        iterateCursorToAddLinksToList(linksCurs, linkList);
        return linkList;
    }

    private void iterateCursorToAddLinksToList(DBCursor linksCurs,
            List<Link> linkList) {
        while (linksCurs.hasNext()) {
            DBObject dbo = linksCurs.next();
            linkList.add(dbObjectToLink(dbo));
        }
    }

    private Link dbObjectToLink(DBObject dbo) {
        return new Link(dbo.get(LinkFields.URL).toString(), dbo.get(
                LinkFields.TITLE).toString(),
                dbo.get(LinkFields.USER).toString(), dbo
                        .get(LinkFields.TIMESTAMP).toString());
    }

    @After
    public void dropLinksCollection() {
        mongoClient.getDB(LinkFields.DATABASE_NAME)
                .getCollection(LinkFields.LINKS_NAME).drop();
    }
}
