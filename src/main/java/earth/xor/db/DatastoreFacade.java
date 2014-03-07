package earth.xor.db;


public class DatastoreFacade {

    private LinksDatastore ds;

    public DatastoreFacade(LinksDatastore ds) {
        this.ds = ds;
    }

    public void addLink(Link link) {
        this.ds.addLink(link);
    }
}
