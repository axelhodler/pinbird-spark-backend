package earth.xor.db;

import java.util.Date;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class UrlsDatastore {

    private MongoClient mongo;
    private DB database;

    public UrlsDatastore(MongoClient mongo) {
	this.mongo = mongo;
	this.database = mongo.getDB(DbProperties.DATABASE_NAME);
    }

    public void addUrl(Url url) {
	DBCollection col = this.database
		.getCollection(DbProperties.URLS_NAME);

	col.insert(new BasicDBObject(DbProperties.URLS_URL, url
		.getUrl())
		.append(DbProperties.URLS_TITLE, url.getTitle())
		.append(DbProperties.URLS_USER, url.getUser())
		.append(DbProperties.URLS_TIMESTAMP, new Date()));
    }

    public DBCursor getUrls() {
	DBCollection col = this.database
		.getCollection(DbProperties.URLS_NAME);

	return col.find();
    }

    public Object getMongoClient() {
	return this.mongo;
    }

    public DBObject getUrlById(String id) {

	DBCollection col = this.database
		.getCollection(DbProperties.URLS_NAME);

	DBObject foundUrl = col.findOne(new BasicDBObject("_id", new ObjectId(
		id)));

	String objectIdStringRepresentation = foundUrl.get("_id").toString();
	String dateStringRepresentation = foundUrl.get(
		DbProperties.URLS_TIMESTAMP).toString();

	foundUrl.put("_id", objectIdStringRepresentation);
	foundUrl.put(DbProperties.URLS_TIMESTAMP,
		dateStringRepresentation);

	return foundUrl;
    }
}
