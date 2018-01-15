package com.drfits.soc.foundation.api;

import com.drfits.soc.foundation.exceptions.SOCException;
import org.apache.sling.api.resource.Resource;

/**
 * Provides methods to operate with pages.
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 11/14/16.
 */
public interface PageManager {
    /**
     * Create page from specified template
     *
     * @param parent       where to insert the page
     * @param pageName     is an URL name of the future resource
     * @param templatePath to create page from
     * @return path of created page
     * @throws SOCException if couldn't create page
     */
    Page createPage(Resource parent, String pageName, String pageTitle, String templatePath) throws SOCException;

    /**
     * Moves the given page to the new destination and automatically saves the changes.
     *
     * @param page            the page to move
     * @param destination     the path of the new destination
     * @param resolveConflict if true resolves name conflict if destination already exists.
     * @return the new page at the new location
     */
    Page move(Resource page, String destination, boolean resolveConflict) throws SOCException;
}
