package earth.xor.rest.routes;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import earth.xor.db.DatastoreFacade;
import earth.xor.db.Link;
import earth.xor.db.LinkFields;
import spark.Request;
import spark.Response;
import spark.Route;

public class GetAllLinksRoute extends Route {

    private DatastoreFacade facade;

    public GetAllLinksRoute(DatastoreFacade facade) {
        super(Routes.GET_ALL_LINKS);
        this.facade = facade;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object handle(Request request, Response response) {
        List<Link> links = facade.getLinks();

        JSONArray linksArray = new JSONArray();
        for (Link l : links) {
            JSONObject jo = new JSONObject();
            jo.put(LinkFields.ID, l.getObjectId());
            jo.put(LinkFields.URL, l.getUrl());
            jo.put(LinkFields.TITLE, l.getTitle());
            jo.put(LinkFields.USER, l.getUser());
            jo.put(LinkFields.TIMESTAMP, l.getTimeStamp());
            linksArray.add(jo);
        }

        JSONObject mainJsonObject = new JSONObject();
        mainJsonObject.put("links", linksArray);

        return mainJsonObject.toJSONString();
    }

}
