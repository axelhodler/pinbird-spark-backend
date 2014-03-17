package earth.xor.rest.transformation;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import earth.xor.model.Bookmark;
import earth.xor.model.BookmarkFields;

@SuppressWarnings("unchecked")
public class JSONTransformator {
    public Bookmark jsonToLink(String json) {
        return createLink((JSONObject) JSONValue.parse(json));
    }

    public String listOfLinksToJson(List<Bookmark> links) {
        JSONObject mainObject = new JSONObject();
        mainObject.put(BookmarkFields.LINKS_NAME, iterateLinksAndAddToArray(links));

        return mainObject.toJSONString();
    }

    public String linkToJson(Bookmark testlink1) {
        JSONObject main = new JSONObject();
        main.put("link", transformLinkToJson(testlink1));
        return main.toJSONString();
    }

    private Bookmark createLink(JSONObject json) {
        return buildLink((JSONObject) json.get("link"));
    }


    private String getUser(JSONObject json) {
        return json.get(BookmarkFields.USER).toString();
    }

    private String getTitle(JSONObject json) {
        return json.get(BookmarkFields.TITLE).toString();
    }

    private String getUrl(JSONObject json) {
        return json.get(BookmarkFields.URL).toString();
    }

    private JSONObject transformLinkToJson(Bookmark link) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(BookmarkFields.ID, link.getObjectId());
        jsonObject.put(BookmarkFields.URL, link.getUrl());
        jsonObject.put(BookmarkFields.TITLE, link.getTitle());
        jsonObject.put(BookmarkFields.USER, link.getUser());
        jsonObject.put(BookmarkFields.TIMESTAMP, link.getTimeStamp());

        return jsonObject;
    }

    private JSONArray iterateLinksAndAddToArray(List<Bookmark> links) {
        JSONArray linksArray = new JSONArray();
        for (Bookmark l : links)
            linksArray.add(transformLinkToJson(l));
        return linksArray;
    }

    private Bookmark buildLink(JSONObject json) {
        return new Bookmark.Builder().url(getUrl(json)).title(getTitle(json))
                .user(getUser(json)).build();
    }
}
