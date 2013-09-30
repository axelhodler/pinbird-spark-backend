package earth.xor;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class UrlsCollection {

    private MongoClient mongo;
    private String databasename;

    public UrlsCollection(MongoClient mongo, String dbname) {
	this.mongo = mongo;
	this.databasename = dbname;
    }

    public void addUrl(Url url) {
	DB database = mongo.getDB(databasename);
	DBCollection col = database.getCollection("urls");

	col.insert(new BasicDBObject("url", url.getUrl()).append("title", url.getTitle()).append(
		"user", url.getUser()));
    }

    public DBCursor getUrls() {
	DB database = mongo.getDB(databasename);
	DBCollection col = database.getCollection("urls");
	
	return col.find();
    }

    public Object getMongoClient() {
	return this.mongo;
    }

}
