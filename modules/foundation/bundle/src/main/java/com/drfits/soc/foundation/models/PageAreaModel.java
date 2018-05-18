package com.drfits.soc.foundation.models;

import com.drfits.soc.foundation.api.Area;
import com.google.common.collect.ImmutableList;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.util.List;

/**
 * {@link Area} model implementation to provide required functionality on the soc:Page
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 11/13/16.
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public final class PageAreaModel extends PageComponentModel implements Area {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageAreaModel.class);

    private static final String COMPONENT_TYPE = "area";

    @ChildResource(name = ".")
    private List<Resource> children;

    @PostConstruct
    @Override
    protected void init() {
        LOGGER.debug("Init area");
        super.init();
    }

    /**
     * Child resources of specified area according to inheritance rule
     *
     * @return {@link ImmutableList} of child {@link Resource} for include within specified area
     */
    @Override
    public List<Resource> getChildren() {
        return children;
    }

    @Nonnull
    @Override
    public String getComponentType() {
        return COMPONENT_TYPE;
    }
}
