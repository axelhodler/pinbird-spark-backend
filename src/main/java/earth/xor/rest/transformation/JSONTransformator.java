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
        return createBookmark((JSONObject) JSONValue.parse(json));
    }

    public String listOfBookmarksToJson(List<Bookmark> bookmarks) {
        JSONObject mainObject = new JSONObject();
        mainObject.put(BookmarkFields.BOOKMARKS,
                iterateLinksAndAddToArray(bookmarks));

        return mainObject.toJSONString();
    }

    public String bookmarkToJson(Bookmark testBookmark1) {
        JSONObject main = new JSONObject();
        main.put(BookmarkFields.BOOKMARK, transformLinkToJson(testBookmark1));
        return main.toJSONString();
    }

    private Bookmark createBookmark(JSONObject json) {
        return buildLink((JSONObject) json.get(BookmarkFields.BOOKMARK));
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

    private JSONObject transformLinkToJson(Bookmark bookmark) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(BookmarkFields.ID, bookmark.getObjectId());
        jsonObject.put(BookmarkFields.URL, bookmark.getUrl());
        jsonObject.put(BookmarkFields.TITLE, bookmark.getTitle());
        jsonObject.put(BookmarkFields.USER, bookmark.getUser());
        jsonObject.put(BookmarkFields.TIMESTAMP, bookmark.getTimeStamp());

        return jsonObject;
    }

    private JSONArray iterateLinksAndAddToArray(List<Bookmark> bookmarks) {
        JSONArray bookmarksArray = new JSONArray();
        for (Bookmark b : bookmarks)
            bookmarksArray.add(transformLinkToJson(b));
        return bookmarksArray;
    }

    private Bookmark buildLink(JSONObject json) {
        return new Bookmark.Builder().url(getUrl(json)).title(getTitle(json))
                .user(getUser(json)).build();
    }
}
