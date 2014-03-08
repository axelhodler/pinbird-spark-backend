package earth.xor.db;

import java.util.List;

public interface LinksDatastore {

    public void addLink(Link link);

    public List<Link> getLinks();

    public Link getLinkById(String id);
}
