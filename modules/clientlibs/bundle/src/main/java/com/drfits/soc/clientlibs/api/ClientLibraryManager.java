package com.drfits.soc.clientlibs.api;

import com.drfits.soc.clientlibs.impl.ClientLibraryType;

import javax.annotation.Nonnull;

/**
 * Client Manager - manage client libraries for page markup
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 10/20/16.
 */
public interface ClientLibraryManager {

    /**
     * Folder which store client library
     */
    String CLIENT_LIBRARY_NODE_TYPE = "soc:ClientLibraryFolder";

    /**
     * Get library include markup.
     *
     * @param categories  for include
     * @param libraryType for include
     * @return HTML markup string for include within page header
     */
    String getIncludeMarkup(@Nonnull String[] categories, @Nonnull ClientLibraryType libraryType);

    /**
     * Remove from the libraries cache.
     *
     * @param categories to invalidate
     */
    void invalidate(@Nonnull String[] categories);

    /**
     * Clean libraries cache
     */
    void invalidateAll();

}
