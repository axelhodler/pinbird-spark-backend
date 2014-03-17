package earth.xor.model;

import org.xorrr.util.EnvironmentVars;

public abstract class BookmarkFields {

    public static String DATABASE_NAME = System.getenv(EnvironmentVars.DB_NAME);

    public static final String BOOKMARKS = "bookmarks";
    public static final String BOOKMARK = "bookmark";

    public static final String ID = "_id";
    public static final String URL = "url";
    public static final String TITLE = "title";
    public static final String USER = "user";
    public static final String TIMESTAMP = "timestamp";

}
