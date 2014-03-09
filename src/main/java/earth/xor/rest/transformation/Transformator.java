package earth.xor.rest.transformation;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import earth.xor.db.Link;
import earth.xor.db.LinkFields;

public class Transformator {
    public Link toLink(String jsonExample) {
        JSONObject jo = (JSONObject) JSONValue.parse(jsonExample);
        return new Link.Builder().url(jo.get(LinkFields.URL).toString())
                .title(jo.get(LinkFields.TITLE).toString())
                .user(jo.get(LinkFields.USER).toString()).build();
    }
}
