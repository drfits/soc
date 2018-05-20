package com.drfits.soc.clientlibs.api;

import org.apache.sling.api.resource.ResourceResolver;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Store for managing client libraries.
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 5/19/2018.
 */
public interface ClientLibraryStore {

    /**
     * Folder which store client library
     */
    String CLIENT_LIBRARY_NODE_TYPE = "soc:ClientLibraryFolder";

    /**
     * Get client libraries for specified categories.
     *
     * @param categories of client libraries.
     * @return collection of available client libraries.
     */
    List<ClientLibrary> get(@Nonnull ResourceResolver resourceResolver, @Nonnull String[] categories);

    /**
     * Get client library for specified category.
     *
     * @param category of client library.
     * @return available client library.
     */
    ClientLibrary get(@Nonnull ResourceResolver resourceResolver, @Nonnull String category);
}
