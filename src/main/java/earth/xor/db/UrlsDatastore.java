package earth.xor.db;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
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
		.getCollection(DbProperties.URLSCOLLECTION_NAME);

	col.insert(new BasicDBObject(DbProperties.URLSCOLLECTION_URL, 
		url.getUrl()).append(DbProperties.URLSCOLLECTION_TITLE,
		url.getTitle()).append(DbProperties.URLSCOLLECTION_USER,
		url.getUser()));
    }

    public DBCursor getUrls() {
	DBCollection col = this.database
		.getCollection(DbProperties.URLSCOLLECTION_NAME);

	return col.find();
    }

    public Object getMongoClient() {
	return this.mongo;
    }

}
