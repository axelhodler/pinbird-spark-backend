package earth.xor.rest.transformation;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import earth.xor.db.Link;
import earth.xor.db.LinkFields;

public class Transformator {
    public Link toLink(String json) {
        return createLink((JSONObject) JSONValue.parse(json));
    }

    private Link createLink(JSONObject json) {
        return new Link.Builder().url(getUrl(json))
                .title(getTitle(json))
                .user(getUser(json)).build();
    }

    private String getUser(JSONObject json) {
        return json.get(LinkFields.USER).toString();
    }

    private String getTitle(JSONObject json) {
        return json.get(LinkFields.TITLE).toString();
    }

    private String getUrl(JSONObject json) {
        return json.get(LinkFields.URL).toString();
    }
}
