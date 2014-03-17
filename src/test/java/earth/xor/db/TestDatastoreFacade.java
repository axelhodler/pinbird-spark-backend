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

import earth.xor.model.Bookmark;

@RunWith(MockitoJUnitRunner.class)
public class TestDatastoreFacade {
    @Mock
    BookmarksDatastore ds;

    private DatastoreFacade facade;

    @Before
    public void setUp() {
        facade = new DatastoreFacade(ds);
    }

    @Test
    public void canAddLink() {
        facade.addBookmark(any(Bookmark.class));

        verify(ds, times(1)).addBookmark(any(Bookmark.class));
    }

    @Test
    public void canGetAllLinks() {
        facade.getBookmarks();

        verify(ds, times(1)).getBookmarks();
    }

    @Test
    public void canGetLinkById() {
        facade.getBookmarkById(anyString());

        verify(ds, times(1)).getBookmarkById(anyString());
    }
}
