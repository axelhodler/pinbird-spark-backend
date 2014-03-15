package earth.xor.rest.routes;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.xorrr.util.LinkObjects;

import spark.Request;
import spark.Response;
import earth.xor.db.DatastoreFacade;
import earth.xor.model.Link;
import earth.xor.model.LinkFields;
import earth.xor.rest.transformation.JSONTransformator;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class TestGetAllLinksRoute {
    @Mock
    Request req;
    @Mock
    Response resp;
    @Mock
    DatastoreFacade facade;
    @Mock
    JSONTransformator transformator;

    private GetAllLinksRoute route;

    @Before
    public void setUp() {
        route = new GetAllLinksRoute(facade, transformator);
    }

    @Test
    public void canGetLink() {
        List<Link> links = createTestLinks();
        JSONObject mainJsonObject = conformToEmberStandards(links);
        when(transformator.listOfLinksToJson(anyListOf(Link.class)))
                .thenReturn(mainJsonObject.toJSONString());
        when(facade.getLinks()).thenReturn(links);

        Object jsonString = route.handle(req, resp);

        verify(facade, times(1)).getLinks();
        verify(transformator, times(1))
                .listOfLinksToJson(anyListOf(Link.class));
        assertEquals(jsonString, mainJsonObject.toJSONString());
    }

    private JSONObject conformToEmberStandards(List<Link> links) {
        JSONObject mainJsonObject = new JSONObject();
        mainJsonObject.put("links", createTestLinksArray(links));
        return mainJsonObject;
    }

    private JSONArray createTestLinksArray(List<Link> links) {
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
        return linksArray;
    }

    private List<Link> createTestLinks() {
        List<Link> links = new ArrayList<>();
        links.add(LinkObjects.testLink1);
        links.add(LinkObjects.testLink2);
        return links;
    }
}
