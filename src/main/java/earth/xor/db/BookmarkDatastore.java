package earth.xor.db;

import java.util.List;

import earth.xor.model.Bookmark;

public interface BookmarkDatastore {

    void addLink(Bookmark link);

    List<Bookmark> getLinks();

    Bookmark getLinkById(String id);
}
