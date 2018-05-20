package com.drfits.soc.clientlibs.api;

import com.drfits.soc.clientlibs.impl.ClientLibraryStoreImpl;
import com.drfits.soc.clientlibs.model.ClientLibraryModel;
import com.drfits.soc.test.core.SOCContext;
import com.google.common.collect.ImmutableList;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.scripting.SlingBindings;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.jcr.query.Query;
import javax.script.Bindings;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientLibIncludeObjectTest {

    private static final String TEST_RES_PATH = "/libs/soc/foundation/clientlibs";

    private static final String CLIENTLIB_COMMON = TEST_RES_PATH + "/common.json";

    private static final String ALL_LIBS_OUTPUT = "<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css\" integrity=\"sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4\" crossorigin=\"anonymous\"><script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js\" integrity=\"sha384-cs/chFZiN24E4KMATLdqdvsezGxaGsi4hLGOzlXwp5UZB1LY//20VyM2taTB4QvJ\" crossorigin=\"anonymous\"></script><script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js\" integrity=\"sha384-uefMccjFJAIv6A+rW+L4AHf99KvxDjWSu1z9VI8SKNVmz4sk7buKt/6v9KI65qnm\" crossorigin=\"anonymous\"></script><link rel=\"stylesheet\" href=\"/libs/soc/foundation/clientlibs/internal/css/bootstrap.min.css\"><script src=\"/libs/soc/foundation/clientlibs/internal/umd/popper.min.js\"></script><script src=\"/libs/soc/foundation/clientlibs/internal/js/bootstrap.min.js\"></script>";
    private static final String JS_LIBS_OUTPUT = "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js\" integrity=\"sha384-cs/chFZiN24E4KMATLdqdvsezGxaGsi4hLGOzlXwp5UZB1LY//20VyM2taTB4QvJ\" crossorigin=\"anonymous\"></script><script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js\" integrity=\"sha384-uefMccjFJAIv6A+rW+L4AHf99KvxDjWSu1z9VI8SKNVmz4sk7buKt/6v9KI65qnm\" crossorigin=\"anonymous\"></script><script src=\"/libs/soc/foundation/clientlibs/internal/umd/popper.min.js\"></script><script src=\"/libs/soc/foundation/clientlibs/internal/js/bootstrap.min.js\"></script>";
    private static final String CSS_LIBS_OUTPUT = "<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css\" integrity=\"sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4\" crossorigin=\"anonymous\"><link rel=\"stylesheet\" href=\"/libs/soc/foundation/clientlibs/internal/css/bootstrap.min.css\">";

    @Rule
    public final SOCContext context = new SOCContext();

    private ClientLibIncludeObject includeObj;
    private Bindings bindings;

    @Before
    public void setUp() {
        this.context.load().json(CLIENTLIB_COMMON, TEST_RES_PATH);
        this.context.addModelsForPackage(ClientLibraryModel.class.getPackage().getName());

        final ClientLibraryStore store = this.context.registerService(ClientLibraryStore.class, new ClientLibraryStoreImpl());
        assertNotNull(this.context.getService(ClientLibraryStore.class));

        this.includeObj = new ClientLibIncludeObject();
        this.bindings = mock(Bindings.class);
        final String SEARCH_CATEGORY_1 = "bootstrap-4.1.0-external";
        final String SEARCH_CATEGORY_2 = "bootstrap-new-internal";
        final String SEARCH_CATEGORY = SEARCH_CATEGORY_1 + ',' + SEARCH_CATEGORY_2;
        when(this.bindings.get(ClientLibIncludeObject.BINDINGS_CATEGORIES)).thenReturn(SEARCH_CATEGORY);
        when(this.bindings.get(SlingBindings.SLING)).thenReturn(this.context.slingScriptHelper());

        /* Mock SQL search result */
        final ResourceResolver resolver = mock(ResourceResolver.class);
        final Resource clientLibRes1 = this.context.resourceResolver().getResource(TEST_RES_PATH + "/external");
        assertNotNull(clientLibRes1);
        when(resolver.findResources(contains(SEARCH_CATEGORY_1), eq(Query.JCR_SQL2)))
                .thenReturn(ImmutableList.of(clientLibRes1).iterator());
        final Resource clientLibRes2 = this.context.resourceResolver().getResource(TEST_RES_PATH + "/internal");
        when(resolver.findResources(contains("not-existed-"), eq(Query.JCR_SQL2)))
                .thenReturn(Collections.emptyIterator());
        assertNotNull(clientLibRes2);
        when(resolver.findResources(contains(SEARCH_CATEGORY_2), eq(Query.JCR_SQL2)))
                .thenReturn(ImmutableList.of(clientLibRes2).iterator());

        when(this.bindings.get(SlingBindings.RESOLVER)).thenReturn(resolver);
    }

    @Test
    public void includeAllLibsTest() {
        when(this.bindings.get(ClientLibIncludeObject.PROP_MODE)).thenReturn(ClientLibraryType.ALL.name());
        this.includeObj.init(this.bindings);
        final String out = this.includeObj.include();
        assertEquals(ALL_LIBS_OUTPUT, out);
    }

    @Test
    public void includeAllLibsFromArrayRepresentationTest() {
        final String SEARCH_CATEGORY_1 = "bootstrap-4.1.0-external";
        final String SEARCH_CATEGORY_2 = "bootstrap-new-internal";
        when(this.bindings.get(ClientLibIncludeObject.BINDINGS_CATEGORIES))
                .thenReturn(new String[]{SEARCH_CATEGORY_1, SEARCH_CATEGORY_2});
        when(this.bindings.get(ClientLibIncludeObject.PROP_MODE)).thenReturn(ClientLibraryType.ALL.name());
        this.includeObj.init(this.bindings);
        final String out = this.includeObj.include();
        assertEquals(ALL_LIBS_OUTPUT, out);
    }

    @Test
    public void includeAllLibsFromNotExistedCategoriesTest() {
        when(this.bindings.get(ClientLibIncludeObject.BINDINGS_CATEGORIES))
                .thenReturn(new String[]{"not-existed-1", "not-existed-2", "not-existed-3", "not-existed-1"});
        when(this.bindings.get(ClientLibIncludeObject.PROP_MODE)).thenReturn(ClientLibraryType.ALL.name());
        this.includeObj.init(this.bindings);
        final String out = this.includeObj.include();
        assertEquals("", out);
    }

    @Test
    public void includeJsLibsTest() {
        when(this.bindings.get(ClientLibIncludeObject.PROP_MODE)).thenReturn(ClientLibraryType.JS.name());
        this.includeObj.init(this.bindings);
        final String out = this.includeObj.include();
        assertEquals(JS_LIBS_OUTPUT, out);
    }

    @Test
    public void includeCSSLibsTest() {
        when(this.bindings.get(ClientLibIncludeObject.PROP_MODE)).thenReturn(ClientLibraryType.CSS.name());
        this.includeObj.init(this.bindings);
        final String out = this.includeObj.include();
        assertEquals(CSS_LIBS_OUTPUT, out);
    }

    @Test
    public void includeIfIllegalModeTest() {
        when(this.bindings.get(ClientLibIncludeObject.PROP_MODE)).thenReturn("illegal mode");
        this.includeObj.init(this.bindings);
        final String out = this.includeObj.include();
        assertEquals("", out);
    }
}