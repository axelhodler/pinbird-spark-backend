package earth.xor;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigAccessor {
    
    private Properties properties;
    
    public ConfigAccessor() {
	this.properties = new Properties();
	try {
	    this.properties.load(new FileInputStream("config.properties"));
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    public String getMongoUri() {
	return this.properties.getProperty("uri");
    }
    
    public String getDatabaseName() {
	return this.properties.getProperty("dbname");
    }
}
