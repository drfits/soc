package com.drfits.soc.foundation.models;

import com.drfits.soc.test.core.SOCContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DialogConfigurationTest {

    private static final String TEST_RES_PATH = "/com/drfits/soc/foundation/models";

    private static final String TEST_DATA_APPS = TEST_RES_PATH + "/DialogConfigurationTest-apps.json";

    private static final String COMPONENT_PATH = "/apps/test-project/components/test-component";

    @Rule
    public SOCContext context = new SOCContext();

    @Before
    public void setUp() throws Exception {
        context.load().json(TEST_DATA_APPS, COMPONENT_PATH);
        context.addModelsForPackage(DialogConfiguration.class.getPackage().getName());
    }

    @Test
    public void testReloadNone() throws Exception {
        Resource resource = context.currentResource(COMPONENT_PATH + "/dialog-reload-none");
        assertNotNull(resource);
        DialogConfiguration model = resource.adaptTo(DialogConfiguration.class);
        assertNotNull(model);
        assertEquals(DialogConfiguration.RELOAD_NONE, model.getSaveReload());
    }

    @Test
    public void testReloadContainer() throws Exception {
        Resource resource = context.currentResource(COMPONENT_PATH + "/dialog-reload-container");
        assertNotNull(resource);
        DialogConfiguration model = resource.adaptTo(DialogConfiguration.class);
        assertNotNull(model);
        assertEquals(DialogConfiguration.RELOAD_CONTAINER, model.getSaveReload());
    }

    @Test
    public void testReloadPage() throws Exception {
        Resource resource = context.currentResource(COMPONENT_PATH + "/dialog-reload-page");
        assertNotNull(resource);
        DialogConfiguration model = resource.adaptTo(DialogConfiguration.class);
        assertNotNull(model);
        assertEquals(DialogConfiguration.RELOAD_PAGE, model.getSaveReload());
    }
}