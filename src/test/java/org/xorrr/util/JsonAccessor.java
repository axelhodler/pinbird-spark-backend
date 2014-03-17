package org.xorrr.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import earth.xor.model.BookmarkFields;

@SuppressWarnings("unchecked")
public class JsonAccessor {

    public static String getPostRequestBody() {
        JSONObject link = new JSONObject();
        link.put(BookmarkFields.URL, "http://www.foo.org");
        link.put(BookmarkFields.TITLE, "foo");
        link.put(BookmarkFields.USER, "test");

        JSONObject main = new JSONObject();
        main.put("link", link);

        return main.toJSONString();
    }

    public static String getExampleLink() {
        JSONObject link = new JSONObject();
        link.put(BookmarkFields.URL, "http://www.foo.org");
        link.put(BookmarkFields.TITLE, "foo");
        link.put(BookmarkFields.USER, "user1");
        link.put(BookmarkFields.TIMESTAMP, null);
        link.put(BookmarkFields.ID, null);

        JSONObject main = new JSONObject();
        main.put(BookmarkFields.BOOKMARK, link);

        return main.toJSONString();
    }

    public static String getExampleLinks() {
        JSONObject link1 = new JSONObject();
        link1.put(BookmarkFields.URL, "http://www.foo.org");
        link1.put(BookmarkFields.TITLE, "foo");
        link1.put(BookmarkFields.USER, "user1");
        link1.put(BookmarkFields.TIMESTAMP, null);
        link1.put(BookmarkFields.ID, null);

        JSONObject link2 = new JSONObject();
        link2.put(BookmarkFields.URL, "http://www.bar.org");
        link2.put(BookmarkFields.TITLE, "bar");
        link2.put(BookmarkFields.USER, "user2");
        link2.put(BookmarkFields.TIMESTAMP, null);
        link2.put(BookmarkFields.ID, null);

        JSONArray linksArray = new JSONArray();
        linksArray.add(link1);
        linksArray.add(link2);

        JSONObject main = new JSONObject();
        main.put(BookmarkFields.BOOKMARKS, linksArray);

        return main.toJSONString();
    }
}
