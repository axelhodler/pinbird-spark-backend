package earth.xor.db;

import java.util.List;


public class DatastoreFacade {

    private LinksDatastore ds;

    public DatastoreFacade(LinksDatastore ds) {
        this.ds = ds;
    }

    public void addLink(Link link) {
        this.ds.addLink(link);
    }

    public List<Link> getLinks() {
        return ds.getLinks();
    }

    public Link getLinkById(String id) {
        return ds.getLinkById(id);
    }
}
