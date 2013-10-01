package earth.xor.db;

import earth.xor.ConfigAccessor;

public class DbProperties {
    
    public static String DATABASE_NAME = new ConfigAccessor().getDatabaseName();
    
    public static final String URLSCOLLECTION_NAME = "urls";
    public static final String URLSCOLLECTION_URL = "url";
    public static final String URLSCOLLECTION_TITLE = "title";
    public static final String URLSCOLLECTION_USER = "user";
}
