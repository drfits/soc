package com.drfits.soc.foundation.models;

import com.drfits.soc.foundation.api.Page;
import com.drfits.soc.foundation.util.GlobalConstants;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;

/**
 * Model class for {@link PageModel#RESOURCE_TYPE} resourceType
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 11/7/16.
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = PageModel.RESOURCE_TYPE
)
public final class PageModel implements Page {

    private static final Logger log = LoggerFactory.getLogger(PageModel.class);

    static final String RESOURCE_TYPE = "soc/foundation/page";

    @Inject
    @Default(values = "")
    @Named(GlobalConstants.JCR_TITLE)
    private String title;

    @Inject
    @Default(values = "")
    @Named(JcrConstants.JCR_LANGUAGE)
    private String language;

    @Inject
    @Default(values = "")
    @Named(GlobalConstants.JCR_DESCRIPTION)
    private String description;

    @Inject
    @Named(JcrConstants.JCR_CREATED)
    private Calendar created;

    @Inject
    @Named(JcrConstants.JCR_LASTMODIFIED)
    private Calendar modified;

    @Inject
    private Calendar published;

    @Inject
    private boolean visible;

    @SlingObject
    private Resource resource;

    private Locale locale;

    @PostConstruct
    protected void init() {
        log.debug("Page model init");
        locale = Optional.ofNullable(language)
                .filter(StringUtils::isNotBlank)
                .map(LocaleUtils::toLocale)
                .orElse(Locale.getDefault());
        log.debug("Page model init finished");
    }

    @Override
    public String getTitle() {
        return title;
    }

    public String getLanguage() {
        return language;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Calendar getCreated() {
        return created;
    }

    @Override
    public Calendar getModified() {
        return modified;
    }

    @Override
    public Calendar getPublished() {
        return published;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public Resource getContentResource() {
        return resource;
    }

    @Override
    public String getPath() {
        return getContentResource().getParent().getPath();
    }

    @Override
    public Locale getLocale() {
        return locale;
    }
}
