package org.xorrr.util;

import org.json.simple.JSONObject;

import earth.xor.model.LinkFields;

@SuppressWarnings("unchecked")
public class JsonAccessor {

    public static String getPostRequestBody() {
        JSONObject link = new JSONObject();
        link.put(LinkFields.URL, "http://www.foo.org");
        link.put(LinkFields.TITLE, "foo");
        link.put(LinkFields.USER, "test");

        JSONObject main = new JSONObject();
        main.put("link", link);

        return main.toJSONString();
    }

    public static String getExampleLink() {
        JSONObject link = new JSONObject();
        link.put(LinkFields.URL, "http://www.foo.org");
        link.put(LinkFields.TITLE, "foo");
        link.put(LinkFields.USER, "user1");
        link.put(LinkFields.TIMESTAMP, null);
        link.put(LinkFields.ID, null);

        JSONObject main = new JSONObject();
        main.put(LinkFields.LINK, link);

        return main.toJSONString();
    }
}
