package earth.xor.db;

import java.util.List;

import earth.xor.model.Bookmark;

public interface BookmarksDatastore {

    void addBookmark(Bookmark bookmark);

    List<Bookmark> getBookmarks();

    Bookmark getBookmarkById(String id);

    void deleteBookmarkById(String id);
}
