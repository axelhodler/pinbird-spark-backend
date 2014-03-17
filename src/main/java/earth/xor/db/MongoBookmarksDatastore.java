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

    public MongoBookmarksDatastore(MongoClient mongo) {
        this.mongo = mongo;
    }

    public void addBookmark(Bookmark bm) {
        DBCollection col = getCollection();

        col.insert(new BasicDBObject(BookmarkFields.URL, bm.getUrl())
                .append(BookmarkFields.TITLE, bm.getTitle())
                .append(BookmarkFields.USER, bm.getUser())
                .append(BookmarkFields.TIMESTAMP, new Date()));
    }

    public List<Bookmark> getBookmarks() {
        List<Bookmark> bookmarks = new ArrayList<>();

        DBCursor curs = getCollection().find();
        for (DBObject dbo : curs)
            bookmarks.add(buildLink(dbo));

        return bookmarks;
    }

    public Bookmark getBookmarkById(String id) {
        DBObject foundBookmark = getCollection().findOne(
                new BasicDBObject(BookmarkFields.ID, new ObjectId(id)));

        return buildLink(foundBookmark);
    }

    private DBCollection getCollection() {
        return mongo.getDB(BookmarkFields.DATABASE_NAME).getCollection(
                BookmarkFields.BOOKMARKS);
    }

    private Bookmark buildLink(DBObject dbo) {
        return new Bookmark.Builder().objectId(dbo.get(BookmarkFields.ID).toString())
                .url(dbo.get(BookmarkFields.URL).toString())
                .title(dbo.get(BookmarkFields.TITLE).toString())
                .user(dbo.get(BookmarkFields.USER).toString())
                .timestamp(dbo.get(BookmarkFields.TIMESTAMP).toString()).build();
    }
}
