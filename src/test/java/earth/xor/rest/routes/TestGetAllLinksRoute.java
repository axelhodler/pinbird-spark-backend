package earth.xor.rest.routes;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.xorrr.util.LinkObjects;

import spark.Request;
import spark.Response;
import earth.xor.db.DatastoreFacade;
import earth.xor.db.Link;
import earth.xor.db.LinkFields;

@RunWith(PowerMockRunner.class)
public class TestGetAllLinksRoute {
    @Mock
    Request req;
    @Mock
    Response resp;
    @Mock
    DatastoreFacade facade;

    private GetAllLinksRoute route;

    @Before
    public void setUp() {
        route = new GetAllLinksRoute(facade);
    }

    @Test
    public void canGetLink() {
        List<Link> links = new ArrayList<>();
        links.add(LinkObjects.testLink1);
        links.add(LinkObjects.testLink2);
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

        when(facade.getLinks()).thenReturn(links);

        Object json = route.handle(req, resp);

        verify(facade, times(1)).getLinks();
        assertEquals(json, mainJsonObject.toJSONString());
    }
}
