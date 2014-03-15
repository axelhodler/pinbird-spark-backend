package earth.xor.db;

import java.util.List;

import earth.xor.model.Link;

public interface LinksDatastore {

    void addLink(Link link);

    List<Link> getLinks();

    Link getLinkById(String id);
}
