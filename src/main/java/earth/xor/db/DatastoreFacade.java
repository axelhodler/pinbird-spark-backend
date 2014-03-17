package earth.xor.db;

import java.util.List;

import earth.xor.model.Bookmark;


public class DatastoreFacade {

    private LinksDatastore ds;

    public DatastoreFacade(LinksDatastore ds) {
        this.ds = ds;
    }

    public void addLink(Bookmark link) {
        this.ds.addLink(link);
    }

    public List<Bookmark> getLinks() {
        return ds.getLinks();
    }

    public Bookmark getLinkById(String id) {
        return ds.getLinkById(id);
    }
}
