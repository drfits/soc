package com.drfits.soc.foundation.models;

import com.drfits.soc.test.core.SOCContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PageComponentModelTest {

    private static final String TEST_RES_PATH = "/com/drfits/soc/foundation/models";

    private static final String TEST_DATA_APPS = TEST_RES_PATH + "/PageComponentModelTest-apps.json";

    private static final String TEST_DATA_CONTENT = TEST_RES_PATH + "/PageComponentModelTest-content.json";

    @Rule
    public SOCContext context = new SOCContext();

    @Before
    public void setUp() throws Exception {
        context.load().json(TEST_DATA_APPS, "/apps");
        context.load().json(TEST_DATA_CONTENT, "/content");
        context.addModelsForPackage(PageComponentModel.class.getPackage().getName());
    }

    @Test
    public void testForComponentWithDialog() throws Exception {
        PageComponentModel model = context
            .currentResource("/content/test-page/jcr:content/mainContent/login")
            .adaptTo(PageComponentModel.class);
        assertNotNull(model);
        assertNotNull(
            "soc{" +
                "\"title\":\"Login Panel\"," +
                "\"path\":\"/content/test-page/jcr:content/mainContent/login\"," +
                "\"dlg\":\"/apps/test-project/components/test-component/dialog.html/content/test-page/jcr:content/mainContent/login\"," +
                "\"res_type\":\"test-project/components/test-component\"" +
                "}",
            model.getStartMarker()
        );
        assertEquals(
            "soc{\"path\":\"/content/test-page/jcr:content/mainContent/login\"}",
            model.getEndMarker()
        );
        assertEquals("/content/test-page/jcr:content/mainContent/login", model.getPath());
        assertEquals("test-component", model.getChildSelector());
    }

    @Test
    public void testForComponentWithoutDialog() throws Exception {
        PageComponentModel model = context
            .currentResource("/content/test-page/jcr:content/mainContent/title-text")
            .adaptTo(PageComponentModel.class);
        assertNotNull(model);
        assertEquals(
            "soc{\"title\":\"Login Panel\",\"path\":\"/content/test-page/jcr:content/mainContent/title-text\",\"type\":\"component\",\"res_type\":\"test-project/components/title-component\"}",
            model.getStartMarker()
        );
        assertEquals(
            "soc{\"path\":\"/content/test-page/jcr:content/mainContent/title-text\"}",
            model.getEndMarker()
        );
    }

    @Test
    public void testForComponentWithoutDialogAndTitle() throws Exception {
        PageComponentModel model = context
            .currentResource("/content/test-page/jcr:content/mainContent/footer-text")
            .adaptTo(PageComponentModel.class);
        assertNotNull(model);
        assertEquals(
            "soc{\"title\":\"\",\"path\":\"/content/test-page/jcr:content/mainContent/footer-text\",\"type\":\"component\",\"res_type\":\"test-project/components/footer-component\"}",
            model.getStartMarker()
        );
        assertEquals(
            "soc{\"path\":\"/content/test-page/jcr:content/mainContent/footer-text\"}",
            model.getEndMarker()
        );
    }
}