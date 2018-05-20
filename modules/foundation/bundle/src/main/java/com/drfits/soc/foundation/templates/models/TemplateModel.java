package com.drfits.soc.foundation.templates.models;

import com.drfits.soc.foundation.api.Template;
import com.drfits.soc.foundation.util.GlobalConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 11/7/16.
 */
@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class TemplateModel implements Template {

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
    private String[] allowedChildren;
    @Inject
    private String[] allowedPaths;

    public TemplateModel(Resource resource) {
        path = resource.getPath();
    }

    public String getPath() {
        return path;
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
    public String[] getAllowedChildren() {
        return allowedChildren;
    }

    @Override
    public String[] getAllowedPaths() {
        return allowedPaths;
    }
}
