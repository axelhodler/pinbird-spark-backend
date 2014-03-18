package earth.xor.db;

import java.util.List;

import earth.xor.model.Bookmark;


public class DatastoreFacade {

    private BookmarksDatastore ds;

    public DatastoreFacade(BookmarksDatastore ds) {
        this.ds = ds;
    }

    public void addBookmark(Bookmark bookmark) {
        this.ds.addBookmark(bookmark);
    }

    public List<Bookmark> getBookmarks() {
        return ds.getBookmarks();
    }

    public Bookmark getBookmarkById(String id) {
        return ds.getBookmarkById(id);
    }

    public void deleteBookmarkById(String id) {
        this.ds.deleteBookmarkById(id);
    }
}
