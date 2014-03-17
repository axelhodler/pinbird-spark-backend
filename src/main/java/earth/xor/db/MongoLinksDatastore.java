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

public class MongoLinksDatastore implements LinksDatastore {

    private MongoClient mongo;

    public MongoLinksDatastore(MongoClient mongo) {
        this.mongo = mongo;
    }

    public void addLink(Bookmark link) {
        DBCollection col = getCollection();

        col.insert(new BasicDBObject(BookmarkFields.URL, link.getUrl())
                .append(BookmarkFields.TITLE, link.getTitle())
                .append(BookmarkFields.USER, link.getUser())
                .append(BookmarkFields.TIMESTAMP, new Date()));
    }

    public List<Bookmark> getLinks() {
        List<Bookmark> links = new ArrayList<>();

        DBCursor curs = getCollection().find();
        for (DBObject dbo : curs)
            links.add(buildLink(dbo));

        return links;
    }

    public Bookmark getLinkById(String id) {
        DBObject foundLink = getCollection().findOne(
                new BasicDBObject(BookmarkFields.ID, new ObjectId(id)));

        return buildLink(foundLink);
    }

    private DBCollection getCollection() {
        return mongo.getDB(BookmarkFields.DATABASE_NAME).getCollection(
                BookmarkFields.LINKS_NAME);
    }

    private Bookmark buildLink(DBObject dbo) {
        return new Bookmark.Builder().objectId(dbo.get(BookmarkFields.ID).toString())
                .url(dbo.get(BookmarkFields.URL).toString())
                .title(dbo.get(BookmarkFields.TITLE).toString())
                .user(dbo.get(BookmarkFields.USER).toString())
                .timestamp(dbo.get(BookmarkFields.TIMESTAMP).toString()).build();
    }
}
