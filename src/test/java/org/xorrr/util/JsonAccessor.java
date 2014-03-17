package org.xorrr.util;

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
        main.put(BookmarkFields.LINK, link);

        return main.toJSONString();
    }
}
