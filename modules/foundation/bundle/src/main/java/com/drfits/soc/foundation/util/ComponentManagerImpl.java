package com.drfits.soc.foundation.util;

import com.drfits.soc.foundation.api.Component;
import com.drfits.soc.foundation.api.ComponentManager;
import com.drfits.soc.foundation.models.ComponentModel;
import com.google.common.collect.ImmutableMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.serviceusermapping.ServiceUserMapped;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.query.Query;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 12/18/16.
 */
@org.osgi.service.component.annotations.Component(
        immediate = true,
        service = {
                ComponentManager.class
        },
        reference = {
                @Reference(
                        name = GlobalConstants.SERVICE_USER_TEMPLATES,
                        target = "(subServiceName=" + GlobalConstants.SERVICE_USER_TEMPLATES + ")",
                        service = ServiceUserMapped.class
                )
        }
)
public class ComponentManagerImpl implements ComponentManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentManagerImpl.class);

    @Reference
    private ResourceResolverFactory resourceFactory;

    private static final Map<String, Object> SERVICE_USER = ImmutableMap.of(
            ResourceResolverFactory.SUBSERVICE,
            GlobalConstants.SERVICE_USER_TEMPLATES
    );

    private static final String Q_ALL_COMPONENTS = "SELECT c.* FROM [soc:Component] as c";

    @Override
    public List<Component> getComponents(String path) {
        List<Component> components = new ArrayList<>();
        try (ResourceResolver resolver = resourceFactory.getServiceResourceResolver(SERVICE_USER)) {
            LOGGER.info("UserId : {}", resolver.getUserID());
            Iterator<Resource> resourceIt = resolver.findResources(Q_ALL_COMPONENTS, Query.JCR_SQL2);
            while (resourceIt.hasNext()) {
                Optional.ofNullable(resourceIt.next())
                        .map(r -> (Component) r.adaptTo(ComponentModel.class))
                        .filter(t -> Objects.nonNull(t.getTitle()))
                        .filter(t -> parentPathMatch(t, path))
                        .ifPresent(components::add);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return components;
    }

    private boolean parentPathMatch(Component component, String path) {
        if (component.getAllowedParents() != null) {
            for (String regexp : component.getAllowedParents()) {
                try {
                    if (Pattern.compile(regexp).matcher(path).matches()) {
                        return true;
                    }
                } catch (PatternSyntaxException e) {
                    LOGGER.warn("Component regexp isn't valid: {}", regexp);
                }
            }
        }
        return false;
    }
}
