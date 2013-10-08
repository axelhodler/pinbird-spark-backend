package earth.xor.db;

import java.util.Date;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class LinksDatastore {

    private MongoClient mongo;
    private DB database;

    public LinksDatastore(MongoClient mongo) {
	this.mongo = mongo;
	this.database = mongo.getDB(DbProperties.DATABASE_NAME);
    }

    public void addLink(Link link) {
	DBCollection col = this.database
		.getCollection(DbProperties.LINKS_NAME);

	col.insert(new BasicDBObject(DbProperties.LINK_URL, link
		.getUrl())
		.append(DbProperties.LINK_TITLE, link.getTitle())
		.append(DbProperties.LINK_USER, link.getUser())
		.append(DbProperties.LINK_TIMESTAMP, new Date()));
    }

    public DBCursor getLinks() {
	DBCollection col = this.database
		.getCollection(DbProperties.LINKS_NAME);

	return col.find();
    }

    public Object getMongoClient() {
	return this.mongo;
    }

    public DBObject getLinkById(String id) {

	DBCollection col = this.database
		.getCollection(DbProperties.LINKS_NAME);

	DBObject foundLink = col.findOne(new BasicDBObject("_id", new ObjectId(
		id)));

	return foundLink;
    }
}
