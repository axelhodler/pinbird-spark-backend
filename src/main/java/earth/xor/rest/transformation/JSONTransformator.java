package earth.xor.rest.transformation;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import earth.xor.model.Link;
import earth.xor.model.LinkFields;

@SuppressWarnings("unchecked")
public class JSONTransformator {
    public Link jsonToLink(String json) {
        return createLink((JSONObject) JSONValue.parse(json));
    }

    public String listOfLinksToJson(List<Link> links) {
        JSONObject mainObject = new JSONObject();
        mainObject.put(LinkFields.LINKS_NAME, iterateLinksAndAddToArray(links));

        return mainObject.toJSONString();
    }

    public String linkToJson(Link testlink1) {
        JSONObject main = new JSONObject();
        main.put("link", transformLinkToJson(testlink1));
        return main.toJSONString();
    }

    private Link createLink(JSONObject json) {
        return buildLink(json);
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

    private JSONObject transformLinkToJson(Link link) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(LinkFields.ID, link.getObjectId());
        jsonObject.put(LinkFields.URL, link.getUrl());
        jsonObject.put(LinkFields.TITLE, link.getTitle());
        jsonObject.put(LinkFields.USER, link.getUser());
        jsonObject.put(LinkFields.TIMESTAMP, link.getTimeStamp());

        return jsonObject;
    }

    private JSONArray iterateLinksAndAddToArray(List<Link> links) {
        JSONArray linksArray = new JSONArray();
        for (Link l : links)
            linksArray.add(transformLinkToJson(l));
        return linksArray;
    }

    private Link buildLink(JSONObject json) {
        return new Link.Builder().url(getUrl(json)).title(getTitle(json))
                .user(getUser(json)).build();
    }
}