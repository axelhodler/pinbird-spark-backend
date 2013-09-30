package earth.xor.db;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class UrlsDatastore {

    private MongoClient mongo;
    private String databasename;

    public UrlsDatastore(MongoClient mongo, String dbname) {
	this.mongo = mongo;
	this.databasename = dbname;
    }

    public void addUrl(Url url) {
	DB database = mongo.getDB(databasename);
	DBCollection col = database
		.getCollection(DbProperties.URLSCOLLECTION_NAME);

	col.insert(new BasicDBObject(DbProperties.URLSCOLLECTION_URL, 
		url.getUrl()).append(DbProperties.URLSCOLLECTION_TITLE,
		url.getTitle()).append(DbProperties.URLSCOLLECTION_USER,
		url.getUser()));
    }

    public DBCursor getUrls() {
	DB database = mongo.getDB(databasename);
	DBCollection col = database
		.getCollection(DbProperties.URLSCOLLECTION_NAME);

	return col.find();
    }

    public Object getMongoClient() {
	return this.mongo;
    }

}
