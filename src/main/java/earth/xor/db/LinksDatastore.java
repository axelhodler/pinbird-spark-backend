package earth.xor.db;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public interface LinksDatastore {

    public void addLink(Link link);

    public DBCursor getLinks();

    public DBObject getLinkById(String id);
}
