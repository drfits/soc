package com.drfits.soc.clientlibs.model;

import com.drfits.soc.clientlibs.api.ClientLibraryManager;
import com.drfits.soc.clientlibs.impl.ClientLibraryType;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 10/20/16.
 */
@Model(
        adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public final class ClientLibIncludeObject {

    private static final Logger log = LoggerFactory.getLogger(ClientLibIncludeObject.class);

    private static final String BINDINGS_CATEGORIES = "categories";

    private static final String PROP_MODE = "mode";

    private String[] categories;

    private String mode;

    @Inject
    private ClientLibraryManager clientLibraryManager;

    @PostConstruct
    protected void init() {
        log.debug("Init ClientLibIncludeObject");
    }

    /**
     * Create HTML markup for include js and css on HTML.
     *
     * @return
     */
    public String include() {
        ClientLibraryType type = null;
        try {
            type = ClientLibraryType.valueOf(mode);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
        }
        final StringBuilder sb = new StringBuilder();
        if (type != null) {
            sb.append(clientLibraryManager.getIncludeMarkup(categories, type));
        }
        return sb.toString();
    }

}