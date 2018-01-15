package com.drfits.soc.foundation.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import com.drfits.soc.foundation.api.Component;
import com.drfits.soc.foundation.util.GlobalConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component model represents component definition.
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 11/7/16.
 */
@Model(
    adaptables = Resource.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public final class ComponentModel implements Component {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentModel.class);

    private String resourceType;

    private String path;

    @Inject
    @Named(GlobalConstants.JCR_TITLE)
    private String title;

    @Inject
    @Named(GlobalConstants.JCR_DESCRIPTION)
    private String description;

    @Inject
    private String[] allowedParents;

    @Inject
    private String componentGroup;

    @Self
    private Resource resource;

    @PostConstruct
    protected void init() {
        path = resource.getPath();
        LOGGER.debug("Init component: {}", path);
        for (String searchPath : resource.getResourceResolver().getSearchPath()) {
            if (path.startsWith(searchPath)) {
                resourceType = path.substring(searchPath.length());
                LOGGER.debug("Component resource type: {}", resourceType);
                break;
            }
        }
    }

    /**
     * Path from which component was instantiated
     * @return absolute repository path
     */
    public String getPath() {
        return path;
    }

    @Override
    public String getResourceType() {
        return resourceType;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String[] getAllowedParents() {
        return allowedParents;
    }

    @Override
    public String getComponentGroup() {
        return componentGroup;
    }
}
