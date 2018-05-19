package com.drfits.soc.clientlibs.impl;

import com.drfits.soc.clientlibs.api.ClientLibrary;
import com.drfits.soc.clientlibs.api.ClientLibraryStore;
import com.drfits.soc.clientlibs.model.ClientLibraryModel;
import com.drfits.soc.test.core.SOCContext;
import com.google.common.collect.ImmutableList;
import org.apache.jackrabbit.oak.query.SQL2Parser;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.jcr.query.Query;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientLibraryStoreImplTest {

    private static final String TEST_RES_PATH = "/libs/soc/foundation/clientlibs";

    private static final String CLIENTLIB_COMMON = TEST_RES_PATH + "/common.json";

    @Rule
    public final SOCContext context = new SOCContext();

    @Before
    public void setUp() {
        this.context.load().json(CLIENTLIB_COMMON, TEST_RES_PATH);
        this.context.addModelsForPackage(ClientLibraryModel.class.getPackage().getName());

        final ClientLibraryStore store = this.context.registerService(ClientLibraryStore.class, new ClientLibraryStoreImpl());
        assertNotNull(this.context.getService(ClientLibraryStore.class));
    }

    @Test
    public void getNotExistedClientLibraryTest() {
        final ClientLibraryStore store = this.context.getService(ClientLibraryStore.class);
        assertNotNull(store);
        final String SEARCH_CATEGORY = "not-existed-client-library";
        final ResourceResolver resolver = mock(ResourceResolver.class);
        when(resolver.findResources(this.getQuery(SEARCH_CATEGORY), Query.JCR_SQL2)).thenReturn(Collections.emptyIterator());
        final ClientLibrary clientLibrary = store.get(resolver, SEARCH_CATEGORY);
        assertNull(clientLibrary);
    }

    @Test
    public void getExternalClientLibraryTest() {
        final ClientLibraryStore store = this.context.getService(ClientLibraryStore.class);
        assertNotNull(store);
        final Resource clientLibRes = this.context.resourceResolver().getResource(TEST_RES_PATH + "/external");
        assertNotNull(clientLibRes);
        final String SEARCH_CATEGORY_1 = "bootstrap-4.1.0-external";
        final String SEARCH_CATEGORY_2 = "bootstrap-new-external";
        final ResourceResolver resolver = mock(ResourceResolver.class);
        when(resolver.findResources(this.getQuery(SEARCH_CATEGORY_1), Query.JCR_SQL2))
                .thenReturn(ImmutableList.of(clientLibRes).iterator());
        final ClientLibrary clientLibrary = store.get(resolver, SEARCH_CATEGORY_1);
        assertNotNull(clientLibrary);
        assertTrue(clientLibrary.getCategories().contains(SEARCH_CATEGORY_1));
        assertTrue(clientLibrary.getCategories().contains(SEARCH_CATEGORY_2));
        assertEquals(3, clientLibrary.getItems().size());
    }

    @Test
    public void getExternalClientLibrariesTest() {
        final ClientLibraryStore store = this.context.getService(ClientLibraryStore.class);
        assertNotNull(store);
        final Resource clientLibRes = this.context.resourceResolver().getResource(TEST_RES_PATH + "/external");
        assertNotNull(clientLibRes);
        final String SEARCH_CATEGORY_1 = "bootstrap-4.1.0-external";
        final String SEARCH_CATEGORY_2 = "bootstrap-new-external";
        final ResourceResolver resolver = mock(ResourceResolver.class);
        when(resolver.findResources(this.getQuery(SEARCH_CATEGORY_1), Query.JCR_SQL2))
                .thenReturn(ImmutableList.of(clientLibRes).iterator());
        when(resolver.findResources(this.getQuery(SEARCH_CATEGORY_2), Query.JCR_SQL2))
                .thenReturn(ImmutableList.of(clientLibRes).iterator());
        final List<ClientLibrary> libraries = store.get(resolver, new String[]{SEARCH_CATEGORY_1, SEARCH_CATEGORY_2});
        assertNotNull(libraries);
        assertEquals(1, libraries.size());
        assertTrue(libraries.get(0).getCategories().contains(SEARCH_CATEGORY_1));
        assertTrue(libraries.get(0).getCategories().contains(SEARCH_CATEGORY_2));
        assertEquals(3, libraries.get(0).getItems().size());
    }

    @Test
    public void getInternalClientLibraryTest() {
        final ClientLibraryStore store = this.context.getService(ClientLibraryStore.class);
        assertNotNull(store);
        final Resource clientLibRes = this.context.resourceResolver().getResource(TEST_RES_PATH + "/internal");
        assertNotNull(clientLibRes);
        final String SEARCH_CATEGORY_1 = "bootstrap-4.1.0-internal";
        final String SEARCH_CATEGORY_2 = "bootstrap-new-internal";
        final ResourceResolver resolver = mock(ResourceResolver.class);
        when(resolver.findResources(this.getQuery(SEARCH_CATEGORY_1), Query.JCR_SQL2))
                .thenReturn(ImmutableList.of(clientLibRes).iterator());
        final ClientLibrary clientLibrary = store.get(resolver, SEARCH_CATEGORY_1);
        assertNotNull(clientLibrary);
        assertTrue(clientLibrary.getCategories().contains(SEARCH_CATEGORY_1));
        assertTrue(clientLibrary.getCategories().contains(SEARCH_CATEGORY_2));
        assertEquals(3, clientLibrary.getItems().size());
    }

    @Test
    public void getInternalClientLibrariesTest() {
        final ClientLibraryStore store = this.context.getService(ClientLibraryStore.class);
        assertNotNull(store);
        final Resource clientLibRes = this.context.resourceResolver().getResource(TEST_RES_PATH + "/internal");
        assertNotNull(clientLibRes);
        final String SEARCH_CATEGORY_1 = "bootstrap-4.1.0-internal";
        final String SEARCH_CATEGORY_2 = "bootstrap-new-internal";
        final ResourceResolver resolver = mock(ResourceResolver.class);
        when(resolver.findResources(this.getQuery(SEARCH_CATEGORY_1), Query.JCR_SQL2))
                .thenReturn(ImmutableList.of(clientLibRes).iterator());
        when(resolver.findResources(this.getQuery(SEARCH_CATEGORY_2), Query.JCR_SQL2))
                .thenReturn(ImmutableList.of(clientLibRes).iterator());
        final List<ClientLibrary> libraries = store.get(resolver, new String[]{SEARCH_CATEGORY_1, SEARCH_CATEGORY_2});
        assertNotNull(libraries);
        assertEquals(1, libraries.size());
        assertTrue(libraries.get(0).getCategories().contains(SEARCH_CATEGORY_1));
        assertTrue(libraries.get(0).getCategories().contains(SEARCH_CATEGORY_2));
        assertEquals(3, libraries.get(0).getItems().size());
    }


    private String getQuery(final String category) {
        return ClientLibraryStoreImpl.Q_FIND_LIBRARY + SQL2Parser.escapeStringLiteral(category);
    }
}