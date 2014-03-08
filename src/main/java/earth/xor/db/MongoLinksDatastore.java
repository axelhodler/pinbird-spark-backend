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

public class MongoLinksDatastore implements LinksDatastore {

    private MongoClient mongo;

    public MongoLinksDatastore(MongoClient mongo) {
        this.mongo = mongo;
    }

    public void addLink(Link link) {
        DBCollection col = getCollection();

        col.insert(new BasicDBObject(LinkFields.URL, link.getUrl())
                .append(LinkFields.TITLE, link.getTitle())
                .append(LinkFields.USER, link.getUser())
                .append(LinkFields.TIMESTAMP, new Date()));
    }

    public List<Link> getLinks() {
        DBCollection col = getCollection();

        List<Link> links = new ArrayList<>();

        DBCursor curs = col.find();
        for (DBObject dbo : curs) {
            Link link = new Link.Builder().url(dbo.get(LinkFields.URL).toString())
                        .title(dbo.get(LinkFields.TITLE).toString())
                        .user(dbo.get(LinkFields.USER).toString())
                        .timestamp(dbo.get(LinkFields.TIMESTAMP).toString())
                        .build();
            links.add(link);
        }

        return links;
    }

    public DBObject getLinkById(String id) {
        DBCollection col = getCollection();

        DBObject foundLink = col.findOne(new BasicDBObject(LinkFields.ID, new ObjectId(
                id)));

        return foundLink;
    }

    private DBCollection getCollection() {
        return mongo.getDB(LinkFields.DATABASE_NAME).getCollection(
                LinkFields.LINKS_NAME);
    }
}
