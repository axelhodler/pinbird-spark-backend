package earth.xor;

import java.util.ArrayList;

import com.mongodb.MongoClient;

public class UrlDB {

    private MongoClient mongo;
    
    public UrlDB(MongoClient mongo) {
	this.mongo = mongo;
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
