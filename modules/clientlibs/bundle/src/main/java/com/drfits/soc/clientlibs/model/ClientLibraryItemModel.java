package com.drfits.soc.clientlibs.model;

import com.drfits.soc.clientlibs.api.ClientLibraryItem;
import com.drfits.soc.clientlibs.api.ClientLibraryType;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Required;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Client library entry representation. Ex.: CSS or JS definition.
 */
@Model(
        adaptables = Resource.class,
        adapters = ClientLibraryItem.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public final class ClientLibraryItemModel implements ClientLibraryItem {

    private static final Logger LOG = LoggerFactory.getLogger(ClientLibraryItemModel.class);

    @Inject
    private boolean external;

    @Inject
    private String crossOrigin;

    @Inject
    @Required
    private String src;

    @Inject
    private String integrity;

    @Inject
    @Required
    @Named("type")
    private String typeString;

    private ClientLibraryType type;

    @PostConstruct
    protected void init() {
        LOG.debug("Init ClientLibraryItemModel");
        final String enumName = StringUtils.defaultString(this.typeString).toUpperCase();
        if (EnumUtils.isValidEnum(ClientLibraryType.class, enumName)) {
            this.type = ClientLibraryType.valueOf(enumName);
        }
        if (StringUtils.isBlank(this.src) || this.type == null) {
            LOG.warn("Client Side library has incorrect parameters.");
        }
    }

    public boolean isExternal() {
        return this.external;
    }

    public String getCrossOrigin() {
        return this.crossOrigin;
    }

    public String getSrc() {
        return this.src;
    }

    @Override
    public String getIntegrity() {
        return this.integrity;
    }

    public ClientLibraryType getType() {
        return this.type;
    }
}
