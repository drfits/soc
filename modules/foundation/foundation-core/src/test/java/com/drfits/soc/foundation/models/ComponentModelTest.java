package com.drfits.soc.foundation.models;

import java.util.Objects;

import com.drfits.soc.test.core.SOCContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class ComponentModelTest {

    private static final String TEST_RES_PATH = "/com/drfits/soc/foundation/models";

    private static final String TEST_DATA_APPS = TEST_RES_PATH + "/ComponentModelTest-apps.json";

    @Rule
    public SOCContext context = new SOCContext();

    @Before
    public void setUp() throws Exception {
        context.load().json(TEST_DATA_APPS, "/apps");
        context.addModelsForPackage(ComponentModel.class.getPackage().getName());
    }

    @Test
    public void testForComponentWithDialog() throws Exception {
        Resource resource = context.currentResource("/apps/test-project/components/test-component");
        assertNotNull(resource);
        ComponentModel model = resource.adaptTo(ComponentModel.class);
        assertNotNull(model);
        assertEquals("test-project/components/test-component", model.getResourceType());
        assertEquals("/apps/test-project/components/test-component", model.getPath());
        assertEquals("Login Panel", model.getTitle());
        assertEquals("Description: Login Panel for test", model.getDescription());
        assertEquals("administration components", model.getComponentGroup());
        assertTrue(Objects.deepEquals(
            model.getAllowedParents(),
            new String[]{ "(/.*)?mainContent", "(/.*)?adminContent" }
        ));
    }

    @Test
    public void testForComponentWithoutDialog() throws Exception {
        Resource resource = context.currentResource("/apps/test-project/components/component-without-dialog");
        assertNotNull(resource);
        ComponentModel model = resource.adaptTo(ComponentModel.class);
        assertNotNull(model);
        assertEquals("test-project/components/component-without-dialog", model.getResourceType());
        assertEquals("/apps/test-project/components/component-without-dialog", model.getPath());
        assertEquals("Login Panel", model.getTitle());
        assertNull(model.getDescription());
        assertNull(model.getAllowedParents());
        assertNull(model.getComponentGroup());
    }
}