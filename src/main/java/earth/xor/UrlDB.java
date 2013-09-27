package earth.xor;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class UrlDB {

    private MongoClient mongo;
    private String databasename;

    public UrlDB(MongoClient mongo, String dbname) {
	this.mongo = mongo;
	this.databasename = dbname;
    }

    public void addUrl(String url, String title, String user) {
	DB database = mongo.getDB(databasename);
	DBCollection col = database.getCollection("urls");

	col.insert(new BasicDBObject("url", url).append("title", title).append(
		"user", user));
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
