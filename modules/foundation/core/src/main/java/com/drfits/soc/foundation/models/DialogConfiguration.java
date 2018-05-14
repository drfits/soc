package com.drfits.soc.foundation.models;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

/**
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 11/20/16.
 */
@Model(adaptables = Resource.class)
public final class DialogConfiguration {

    static final String RELOAD_NONE = "none";

    static final String RELOAD_CONTAINER = "container";

    static final String RELOAD_PAGE = "page";

    /**
     * Which area should be reloaded after dialog save
     */
    @Inject
    @Default(values = RELOAD_NONE)
    private String saveReload;

    public String getSaveReload() {
        return StringUtils.lowerCase(saveReload);
    }
}
