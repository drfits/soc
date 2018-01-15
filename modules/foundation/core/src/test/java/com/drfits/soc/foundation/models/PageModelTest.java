package com.drfits.soc.foundation.models;

import java.util.Locale;

import com.drfits.soc.foundation.api.Page;
import com.drfits.soc.test.core.SOCContext;
import com.drfits.soc.test.core.utils.StringToCalendarCompare;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class PageModelTest {

    private static final String TEST_DATA = "/com/drfits/soc/foundation/models/PageModelTest.json";

    private static final String ROOT_PATH = "/content/soc/test";

    private static final String EMPTY_PAGE_PATH = ROOT_PATH + "/empty-page/jcr:content";

    private static final String PAGE_WITH_DATA_PATH = ROOT_PATH + "/page-with-data/jcr:content";

    @Rule
    public SOCContext context = new SOCContext();

    @Before
    public void setUp() throws Exception {
        context.load().json(TEST_DATA, ROOT_PATH);
        context.addModelsForPackage(PageModel.class.getPackage().getName());
    }

    @Test
    public void init() throws Exception {
        assertNotNull(context);
        Resource pageResource = context.currentResource(EMPTY_PAGE_PATH);
        assertNotNull(pageResource);
        PageModel pageModel = pageResource.adaptTo(PageModel.class);
        assertNotNull(pageModel);
    }

    @Test
    public void testWhenPropertiesAreEmpty() throws Exception {
        Resource pageResource = context.currentResource(EMPTY_PAGE_PATH);
        assertNotNull(pageResource);
        PageModel model = pageResource.adaptTo(PageModel.class);
        pageResource.adaptTo(Page.class);
        assertNotNull(model);
        assertEquals(model.getTitle(), "");
        assertEquals(model.getDescription(), "");
        assertEquals(model.getLanguage(), "");
        assertNull(model.getCreated());
        assertNotNull(model.getModified());
        assertNull(model.getPublished());
        assertFalse(model.isVisible());
        assertNotNull(model.getResource());
        assertNotNull(model.getLocale());
    }

    @Test
    public void testResourcePageWithProperties() throws Exception {
        Resource pageResource = context.currentResource(PAGE_WITH_DATA_PATH);
        assertNotNull(pageResource);
        PageModel model = pageResource.adaptTo(PageModel.class);
        assertNotNull(model);
        assertEquals(model.getTitle(), "test title");
        assertEquals(model.getDescription(), "test description");
        assertEquals("ru", model.getLanguage());
        assertNotNull(model.getModified());
        assertTrue(new StringToCalendarCompare("2017-11-01T09:40:02.000+03:00").same(model.getModified()));
        assertNotNull(model.getPublished());
        assertTrue(new StringToCalendarCompare("2017-11-03T13:30:00.000+03:00").same(model.getPublished()));
        assertTrue(model.isVisible());
        assertNotNull(model.getResource());
        assertEquals(new Locale("ru"), model.getLocale());
    }

    @Test
    public void testRequestPageWithProperties() throws Exception {
        PageModel model = context.currentResource(PAGE_WITH_DATA_PATH).adaptTo(PageModel.class);
        assertNotNull(model);
        assertEquals(model.getTitle(), "test title");
        assertEquals(model.getDescription(), "test description");
        assertEquals("ru", model.getLanguage());
        assertNotNull(model.getModified());
        assertTrue(new StringToCalendarCompare("2017-11-01T09:40:02.000+03:00").same(model.getModified()));
        assertNotNull(model.getPublished());
        assertTrue(new StringToCalendarCompare("2017-11-03T13:30:00.000+03:00").same(model.getPublished()));
        assertTrue(model.isVisible());
        assertNotNull(model.getResource());
        assertEquals(new Locale("ru"), model.getLocale());
    }
}