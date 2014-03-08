package earth.xor.db;

import java.util.List;

import com.mongodb.DBObject;

public interface LinksDatastore {

    public void addLink(Link link);

    public List<Link> getLinks();

    public DBObject getLinkById(String id);
}
