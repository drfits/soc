package com.drfits.soc.foundation.models;

import com.drfits.soc.test.core.SOCContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class PageAreaModelTest {

    private static final String TEST_RES_PATH = "/com/drfits/soc/foundation/models";

    private static final String TEST_DATA_APPS = TEST_RES_PATH + "/PageAreaModelTest-apps.json";

    private static final String TEST_DATA_CONTENT = TEST_RES_PATH + "/PageAreaModelTest-content.json";

    @Rule
    public SOCContext context = new SOCContext();

    @Before
    public void setUp() throws Exception {
        context.load().json(TEST_DATA_APPS, "/apps");
        context.load().json(TEST_DATA_CONTENT, "/content");
        context.addModelsForPackage(PageComponentModel.class.getPackage().getName());
    }

    @Test
    public void testAreaWithChildren() throws Exception {
        PageAreaModel model = context.currentResource("/content/test-page/jcr:content/mainContent")
                .adaptTo(PageAreaModel.class);
        assertNotNull(model);
        assertNotNull(model.getChildren());
        assertEquals(3, model.getChildren().size());
        assertEquals(
                "soc{\"path\":\"/content/test-page/jcr:content/mainContent\",\"type\":\"area\",\"res_type\":\"soc/foundation/area\"}",
                model.getStartMarker()
        );
        assertEquals("soc{\"path\":\"/content/test-page/jcr:content/mainContent\"}", model.getEndMarker());
    }

    @Test
    public void testEmptyArea() throws Exception {
        PageAreaModel model = context.currentResource("/content/test-page/jcr:content/empty-area")
                .adaptTo(PageAreaModel.class);
        assertNotNull(model);
        assertNotNull(model.getChildren());
        assertTrue(model.getChildren().isEmpty());
        assertEquals(
                "soc{\"path\":\"/content/test-page/jcr:content/empty-area\",\"type\":\"area\",\"res_type\":\"soc/foundation/area\"}",
                model.getStartMarker()
        );
        assertEquals("soc{\"path\":\"/content/test-page/jcr:content/empty-area\"}", model.getEndMarker());
    }
}