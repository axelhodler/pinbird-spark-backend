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

    public void addUrl(String string, String string2, String string3) {
	// TODO Auto-generated method stub
	
    }

    public ArrayList<UrlInfo> getUrls() {
	// TODO Auto-generated method stub
	return null;
    }

    public Object getMongoClient() {
	return this.mongo;
    }

}
