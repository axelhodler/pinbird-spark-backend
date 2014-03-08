package earth.xor.db;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestDatastoreFacade {
    @Mock
    LinksDatastore ds;

    private DatastoreFacade facade;

    @Before
    public void setUp() {
        facade = new DatastoreFacade(ds);
    }

    @Test
    public void canAddLink() {
        facade.addLink(any(Link.class));

        verify(ds, times(1)).addLink(any(Link.class));
    }

    @Test
    public void canGetAllLinks() {
        facade.getLinks();

        verify(ds, times(1)).getLinks();
    }

    @Test
    public void canGetLinkById() {
        facade.getLinkById(anyString());

        verify(ds, times(1)).getLinkById(anyString());
    }
}
