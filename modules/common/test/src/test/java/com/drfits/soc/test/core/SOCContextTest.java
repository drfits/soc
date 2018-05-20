package com.drfits.soc.test.core;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public final class SOCContextTest {

    private static final String TEST_PATH = "/content/test-resource";

    @Rule
    public SOCContext context = new SOCContext();

    public SOCContextTest() {
    }

    @Before
    public void setUp() {
        this.context.load().json("/com/drfits/models/model.json", TEST_PATH);
        this.context.addModelsForPackage(TestMockModel.class.getPackage().getName());
    }

    @Test
    public void testContextInstantiation() {
        assertNotNull(this.context);
    }

    @Test
    public void testContextAllowSlingMocks() {
        final Resource resource = this.context.resourceResolver().getResource(TEST_PATH);
        assertNotNull(resource);
        final TestMockModel mockModel = resource.adaptTo(TestMockModel.class);
        assertNotNull(mockModel);
        assertEquals("sling model test string", mockModel.getTestLine());
    }
}