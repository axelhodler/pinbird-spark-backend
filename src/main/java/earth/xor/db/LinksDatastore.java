package earth.xor.db;

import java.util.List;

public interface LinksDatastore {

    void addLink(Link link);

    List<Link> getLinks();

    Link getLinkById(String id);
}
