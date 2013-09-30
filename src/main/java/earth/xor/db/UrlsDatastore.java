package earth.xor.db;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class UrlsDatastore {

    private MongoClient mongo;
    private String databasename;
    
    private String collectionName = "urls";
    
    private String urlKey = "url";
    private String titleKey = "title";
    private String userKey = "user";

    public UrlsDatastore(MongoClient mongo, String dbname) {
	this.mongo = mongo;
	this.databasename = dbname;
    }

    public void addUrl(Url url) {
	DB database = mongo.getDB(databasename);
	DBCollection col = database.getCollection(collectionName);

	col.insert(new BasicDBObject(urlKey, url.getUrl()).append(titleKey, url.getTitle()).append(
		userKey, url.getUser()));
    }

    public DBCursor getUrls() {
	DB database = mongo.getDB(databasename);
	DBCollection col = database.getCollection(collectionName);
	
	return col.find();
    }

    public Object getMongoClient() {
	return this.mongo;
    }

}
