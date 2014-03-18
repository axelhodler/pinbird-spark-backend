package earth.xor.db;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
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
import org.xorrr.util.BookmarkObjects;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import earth.xor.model.Bookmark;
import earth.xor.model.BookmarkFields;

@RunWith(MockitoJUnitRunner.class)
public class TestMongoBookmarksDatastore {
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

    private MongoBookmarksDatastore ds;

    private void mockDBObjectBehaviour() {
        when(dbo.get(BookmarkFields.ID)).thenReturn("i");
        when(dbo.get(BookmarkFields.URL)).thenReturn("u");
        when(dbo.get(BookmarkFields.TITLE)).thenReturn("t");
        when(dbo.get(BookmarkFields.USER)).thenReturn("us");
        when(dbo.get(BookmarkFields.TIMESTAMP)).thenReturn("ts");
    }

    @Before
    public void setUpTests() throws UnknownHostException {
        when(mongoClient.getDB(anyString())).thenReturn(db);
        when(db.getCollection(anyString())).thenReturn(col);

        this.ds = new MongoBookmarksDatastore(mongoClient);
    }

    @Test
    public void bookmarkCanBeAdded() {
        ds.addBookmark(BookmarkObjects.testBookmark1);

        verify(col, times(1)).insert(any(BasicDBObject.class));
    }

    @Test
    public void canGetAllBookmarks() {
        when(col.find()).thenReturn(curs);
        when(curs.iterator()).thenReturn(dbos);
        when(dbos.hasNext()).thenReturn(true, false);
        when(dbos.next()).thenReturn(dbo);
        mockDBObjectBehaviour();
        
        List<Bookmark> bookmarks = ds.getBookmarks();

        verify(col, times(1)).find();
        assertEquals("i", bookmarks.get(0).getObjectId());
        assertEquals("u", bookmarks.get(0).getUrl());
        assertEquals("t", bookmarks.get(0).getTitle());
        assertEquals("us", bookmarks.get(0).getUser());
        assertEquals("ts", bookmarks.get(0).getTimeStamp());
    }

    @Test
    public void canGetBookmarkViaId() {
        when(col.findOne(any(DBObject.class))).thenReturn(dbo);
        mockDBObjectBehaviour();

        Bookmark b = ds.getBookmarkById(new ObjectId().toString());

        verify(col, times(1)).findOne(any(BasicDBObject.class));
        assertEquals("u", b.getUrl());
        assertEquals("t", b.getTitle());
        assertEquals("us", b.getUser());
        assertEquals("ts", b.getTimeStamp());
    }

    @Test
    public void canDeleteBookmarkViaId() {
        when(col.findOne(any(DBObject.class))).thenReturn(dbo);

        ds.deleteBookmarkById(new ObjectId().toString());

        verify(col, times(1)).remove(dbo);
    }
}
