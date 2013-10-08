package earth.xor.db;

import earth.xor.ConfigAccessor;

public class DbProperties {
    
    public static String DATABASE_NAME = new ConfigAccessor().getDatabaseName();
    
    public static final String LINKS_NAME = "links";
    
    public static final String LINK_ID = "_id";
    public static final String LINK_URL = "url";
    public static final String LINK_TITLE = "title";
    public static final String LINK_USER = "user";
    public static final String LINK_TIMESTAMP = "timestamp";
    
    public static final String LINKS_ROUTE = "/links";
}
