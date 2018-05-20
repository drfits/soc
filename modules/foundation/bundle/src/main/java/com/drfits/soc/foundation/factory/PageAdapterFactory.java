package com.drfits.soc.foundation.factory;

import com.drfits.soc.foundation.api.Page;
import com.drfits.soc.foundation.models.PageModel;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.adapter.AdapterFactory;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

/**
 * Page adapter factory.
 * Created by evgeniy on 6/30/2017.
 */
@Component(
        immediate = true,
        property = {
                // Adaptables list the \"From\" objects the adapter supports
                "adaptables=org.apache.sling.api.resource.Resource",
                // Adapters list the \"To\" objects the adapter supports
                "adapters=com.drfits.soc.foundation.api.Page"
        }
)
public final class PageAdapterFactory implements AdapterFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageAdapterFactory.class);

    @Override
    @SuppressWarnings("unchecked")
    public <AdapterType> AdapterType getAdapter(@Nonnull final Object adaptable, @Nonnull final Class<AdapterType> type) {
        final AdapterType adapter;
        // Ensure the adaptable object is of an appropriate type
        if (adaptable instanceof Resource && Page.PAGE_RESOURCE_TYPE.equals(((Resource) adaptable).getResourceType())) {
            final Resource contentResource = ((Resource) adaptable).getChild(JcrConstants.JCR_CONTENT);
            if (contentResource != null) {
                adapter = (AdapterType) contentResource.adaptTo(PageModel.class);
                LOGGER.trace("Adapted to {}. Success: {}", adapter != null);
            } else {
                LOGGER.warn("Resolver cannot be adapted to {}", type.getCanonicalName());
                adapter = null;
            }
        } else {
            LOGGER.warn("Resolver cannot be adapted to {}", type.getCanonicalName());
            adapter = null;
        }
        return adapter;
    }
}
