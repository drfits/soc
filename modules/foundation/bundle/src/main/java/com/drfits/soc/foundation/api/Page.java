package com.drfits.soc.foundation.api;

import org.apache.sling.api.resource.Resource;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 11/14/16.
 */
public interface Page {

    /**
     * Page resource type
     */
    String PAGE_RESOURCE_TYPE = "soc:Page";

    String getTitle();

    Locale getLocale();

    String getDescription();

    Calendar getCreated();

    Calendar getModified();

    Calendar getPublished();

    boolean isVisible();

    Resource getContentResource();

    /**
     * Get path of the page.
     *
     * @return path of this page.
     */
    String getPath();
}
