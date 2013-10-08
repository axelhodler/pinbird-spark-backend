package earth.xor.db;

import earth.xor.ConfigAccessor;

public class DbProperties {
    
    public static String DATABASE_NAME = new ConfigAccessor().getDatabaseName();
    
    public static final String URLS_NAME = "urls";
    public static final String URLS_URL = "url";
    public static final String URLS_TITLE = "title";
    public static final String URLS_USER = "user";
    public static final String URLS_TIMESTAMP = "timestamp";
}
