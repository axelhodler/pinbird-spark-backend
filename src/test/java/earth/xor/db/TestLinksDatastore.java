package earth.xor.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import earth.xor.EmbedMongo;
import earth.xor.EmbedMongoProperties;
import earth.xor.ExampleLinks;

public class TestLinksDatastore {
    private MongoClient mongoClient;
    private LinksDatastore linksData;

    @BeforeClass
    public static void setUpEmbeddedMongo() throws UnknownHostException,
            IOException {
        EmbedMongo.getInstance();
    }

    @Before
    public void setUpTests() throws UnknownHostException {
        this.mongoClient = new MongoClient("localhost",
                EmbedMongoProperties.PORT);

        this.linksData = new LinksDatastore(mongoClient);
    }

    @Test
    public void testAddingAndGettingAllLinks() {
        linksData.addLink(ExampleLinks.testLink1);
        linksData.addLink(ExampleLinks.testLink2);
        linksData.addLink(ExampleLinks.testLink3);

        DBCursor linksCursor = linksData.getLinks();

        List<Link> linkList = createLinksArrayFromUrlDataInCursor(linksCursor);

        assertEquals("bar", linkList.get(1).getTitle());
        assertEquals("user3", linkList.get(2).getUser());

        assertNotNull(linkList.get(1).getTimeStamp());
    }

    @Test
    public void testGettingAUrlById() {
        linksData.addLink(ExampleLinks.testLink1);

        DBCollection col = mongoClient.getDB(DbProperties.DATABASE_NAME)
                .getCollection(DbProperties.LINKS_NAME);

        DBObject savedUrl = col.findOne();

        DBObject obj = linksData.getLinkById(savedUrl.get("_id").toString());

        assertEquals("foo", obj.get(DbProperties.LINK_TITLE).toString());
        assertEquals(savedUrl.get("_id"), obj.get("_id"));
    }

    private List<Link> createLinksArrayFromUrlDataInCursor(DBCursor urlsCursor) {

        List<Link> urlList = new ArrayList<Link>();

        while (urlsCursor.hasNext()) {
            DBObject dbo = urlsCursor.next();
            Link currentUrl = new Link(dbo.get(DbProperties.LINK_URL)
                    .toString(), dbo.get(DbProperties.LINK_TITLE).toString(),
                    dbo.get(DbProperties.LINK_USER).toString(), dbo.get(
                            DbProperties.LINK_TIMESTAMP).toString());
            urlList.add(currentUrl);
        }

        return urlList;
    }

    @After
    public void dropLinksCollection() {
        mongoClient.getDB(DbProperties.DATABASE_NAME)
                .getCollection(DbProperties.LINKS_NAME).drop();
    }
}
