package earth.xor;

import java.net.UnknownHostException;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import earth.xor.rest.SparkRestApi;

public class Main {

    public static void main(String args[]) throws UnknownHostException {

        ConfigAccessor config = new ConfigAccessor();

        MongoClientURI mongoUri = new MongoClientURI(config.getMongoUri());
        MongoClient client = new MongoClient(mongoUri);

        SparkRestApi.getInstance().launchServer(client);
    }
}
