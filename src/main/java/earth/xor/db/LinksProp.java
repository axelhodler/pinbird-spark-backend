package earth.xor.db;

import earth.xor.helpers.ConfigAccessor;

public class LinksProp {
    
    public static String DATABASE_NAME = new ConfigAccessor().getDatabaseName();
    
    public static final String LINKS_NAME = "links";
    
    public static final String ID = "_id";
    public static final String URL = "url";
    public static final String TITLE = "title";
    public static final String USER = "user";
    public static final String TIMESTAMP = "timestamp";
    
    public static final String LINKS_ROUTE = "/links";
}
