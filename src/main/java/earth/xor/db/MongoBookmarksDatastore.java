package earth.xor.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import earth.xor.model.Bookmark;
import earth.xor.model.BookmarkFields;

public class MongoBookmarksDatastore implements BookmarksDatastore {

    private MongoClient mongo;
    private DBCollection col;

    public MongoBookmarksDatastore(MongoClient mongo) {
        this.mongo = mongo;

        col = getCollection();
    }

    public void addBookmark(Bookmark bm) {
        col.insert(new BasicDBObject(BookmarkFields.URL, bm.getUrl())
                .append(BookmarkFields.TITLE, bm.getTitle())
                .append(BookmarkFields.USER, bm.getUser())
                .append(BookmarkFields.TIMESTAMP, new Date()));
    }

    public List<Bookmark> getBookmarks() {
        List<Bookmark> bookmarks = new ArrayList<>();

        DBCursor curs = getCollection().find();
        for (DBObject dbo : curs)
            bookmarks.add(buildBookmark(dbo));

        return bookmarks;
    }

    public Bookmark getBookmarkById(String id) {
        DBObject foundBookmark = findBookmarkById(id);

        return buildBookmark(foundBookmark);
    }

    @Override
    public void deleteBookmarkById(String id) {
        DBObject foundBookmark = findBookmarkById(id);

        col.remove(foundBookmark);
    }

    private DBObject findBookmarkById(String id) {
        return getCollection().findOne(
                new BasicDBObject(BookmarkFields.ID, new ObjectId(id)));
    }

    private DBCollection getCollection() {
        return mongo.getDB(BookmarkFields.DATABASE_NAME).getCollection(
                BookmarkFields.BOOKMARKS);
    }

    private Bookmark buildBookmark(DBObject dbo) {
        return new Bookmark.Builder().objectId(dbo.get(BookmarkFields.ID).toString())
                .url(dbo.get(BookmarkFields.URL).toString())
                .title(dbo.get(BookmarkFields.TITLE).toString())
                .user(dbo.get(BookmarkFields.USER).toString())
                .timestamp(dbo.get(BookmarkFields.TIMESTAMP).toString()).build();
    }

}
