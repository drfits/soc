package com.drfits.soc.foundation.templates;

import com.drfits.soc.foundation.api.Template;
import com.drfits.soc.foundation.api.TemplateManager;
import com.drfits.soc.foundation.templates.models.TemplateModel;
import com.drfits.soc.foundation.util.GlobalConstants;
import com.google.common.collect.ImmutableMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.serviceusermapping.ServiceUserMapped;
import org.osgi.service.component.annotations.Component;
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
@Component(
        immediate = true,
        service = {
                TemplateManager.class
        },
        reference = {
                @Reference(
                        name = GlobalConstants.SERVICE_USER_TEMPLATES,
                        target = "(subServiceName=" + GlobalConstants.SERVICE_USER_TEMPLATES + ")",
                        service = ServiceUserMapped.class
                )
        }
)
public class TemplateManagerImpl implements TemplateManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateManagerImpl.class);

    @Reference
    private ResourceResolverFactory resourceFactory;

    private static final Map<String, Object> SERVICE_USER = ImmutableMap.of(
            ResourceResolverFactory.SUBSERVICE,
            GlobalConstants.SERVICE_USER_TEMPLATES
    );

    private static final String Q_ALL_TEMPLATES = "SELECT t.* FROM [soc:Template] as t";

    @Override
    public List<Template> getTemplates(String path) {
        List<Template> templates = new ArrayList<>();
        try (ResourceResolver resolver = resourceFactory.getServiceResourceResolver(SERVICE_USER)) {
            LOGGER.info("UserId : {}", resolver.getUserID());
            Iterator<Resource> resourceIt = resolver.findResources(Q_ALL_TEMPLATES, Query.JCR_SQL2);
            while (resourceIt.hasNext()) {
                Optional.ofNullable(resourceIt.next())
                        .map(r -> (Template) r.adaptTo(TemplateModel.class))
                        .filter(t -> templatePathMatch(t, path))
                        .ifPresent(templates::add);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return templates;
    }

    private boolean templatePathMatch(Template template, String path) {
        if (template.getAllowedPaths() != null) {
            for (String regexp : template.getAllowedPaths()) {
                try {
                    if (Pattern.compile(regexp).matcher(path).matches()) {
                        return true;
                    }
                } catch (PatternSyntaxException e) {
                    LOGGER.warn("Template regexp isn't valid: {}", regexp);
                }
            }
        }
        return false;
    }
}
