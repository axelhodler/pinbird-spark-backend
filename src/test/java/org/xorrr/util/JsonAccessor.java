package org.xorrr.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import earth.xor.model.BookmarkFields;

@SuppressWarnings("unchecked")
public class JsonAccessor {

    public static String getPostRequestBody() {
        JSONObject bookmark = new JSONObject();
        bookmark.put(BookmarkFields.URL, "http://www.foo.org");
        bookmark.put(BookmarkFields.TITLE, "foo");
        bookmark.put(BookmarkFields.USER, "test");

        JSONObject main = new JSONObject();
        main.put(BookmarkFields.BOOKMARK, bookmark);

        return main.toJSONString();
    }

    public static String getExampleBookmark() {
        JSONObject bookmark = new JSONObject();
        bookmark.put(BookmarkFields.URL, "http://www.foo.org");
        bookmark.put(BookmarkFields.TITLE, "foo");
        bookmark.put(BookmarkFields.USER, "user1");
        bookmark.put(BookmarkFields.TIMESTAMP, null);
        bookmark.put(BookmarkFields.ID, null);

        JSONObject main = new JSONObject();
        main.put(BookmarkFields.BOOKMARKS, bookmark);

        return main.toJSONString();
    }

    public static String getExampleBookmarks() {
        JSONObject bookmark = new JSONObject();
        bookmark.put(BookmarkFields.URL, "http://www.foo.org");
        bookmark.put(BookmarkFields.TITLE, "foo");
        bookmark.put(BookmarkFields.USER, "user1");
        bookmark.put(BookmarkFields.TIMESTAMP, null);
        bookmark.put(BookmarkFields.ID, null);

        JSONObject bookmark2 = new JSONObject();
        bookmark2.put(BookmarkFields.URL, "http://www.bar.org");
        bookmark2.put(BookmarkFields.TITLE, "bar");
        bookmark2.put(BookmarkFields.USER, "user2");
        bookmark2.put(BookmarkFields.TIMESTAMP, null);
        bookmark2.put(BookmarkFields.ID, null);

        JSONArray bookmarksArray = new JSONArray();
        bookmarksArray.add(bookmark);
        bookmarksArray.add(bookmark2);

        JSONObject main = new JSONObject();
        main.put(BookmarkFields.BOOKMARKS, bookmarksArray);

        return main.toJSONString();
    }
}
