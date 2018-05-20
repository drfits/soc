package com.drfits.soc.foundation.models;

import com.drfits.soc.foundation.util.GlobalConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Wrapper of component resource which should be rendered within requested soc:Page resource.
 * Main purpose of this class - to provide additional layer for extra-conditions and allow to control components markup
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 11/7/16.
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class PageComponentModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageComponentModel.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String START_OBJECT_MARKER = "soc";

    private static final String DIALOG_NODE = "/dialog";

    private static final String COMPONENT_TYPE = "component";

    private String path;

    @Inject
    private String componentGroup;

    @SlingObject
    private ResourceResolver resourceResolver;

    @SlingObject
    private Resource resource;

    private String childSelector;

    private String startMarker;

    private String endMarker;

    @PostConstruct
    protected void init() {
        path = resource.getPath();
        LOGGER.debug("Init component: {}", path);
        childSelector = StringUtils.substringAfterLast(resource.getResourceType(), "/");
        LOGGER.debug("Child selector: {}", childSelector);
        try {
            String title = null;
            String dlg = null;
            Resource componentRes = getComponentResource(resource);
            if (componentRes != null) {
                title = componentRes.getValueMap().get(GlobalConstants.JCR_TITLE, "");
                String dlgResourcePath = componentRes.getPath() + DIALOG_NODE;
                if (resourceResolver.getResource(dlgResourcePath) != null) {
                    dlg = dlgResourcePath + ".html" + resource.getPath();
                }
            }
            startMarker = START_OBJECT_MARKER + OBJECT_MAPPER.writeValueAsString(
                    new PageComponentModel.StartMarker(
                            title,
                            resource.getPath(),
                            dlg,
                            resource.getResourceType(),
                            getComponentType()
                    )
            );
            endMarker = START_OBJECT_MARKER + OBJECT_MAPPER.writeValueAsString(
                    new PageComponentModel.EndMarker(
                            resource.getPath()
                    )
            );
        } catch (JsonProcessingException e) {
            LOGGER.error("Unable to create marker object", e);
        }
    }

    /**
     * Find component associated with this resource
     *
     * @param resource for which wi should find component
     * @return component resource if exists
     */
    @Nullable
    private Resource getComponentResource(@Nonnull Resource resource) {
        Resource componentResource = null;
        ResourceResolver resolver = resource.getResourceResolver();
        for (String searchPath : resolver.getSearchPath()) {
            Resource componentRes = resolver.getResource(searchPath + resource.getResourceType());
            if (componentRes != null) {
                componentResource = componentRes;
                break;
            }
        }
        return componentResource;
    }

    /**
     * Start marker object representation
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final class StartMarker {

        @JsonProperty
        private final String title;

        @JsonProperty
        private final String path;

        @JsonProperty
        private final String dlg;

        @JsonProperty("res_type")
        private final String resourceType;

        @JsonProperty
        private final String type;

        StartMarker(
                @Nullable final String title,
                @Nullable final String path,
                @Nullable final String dlg,
                @Nullable final String resourceType,
                @Nullable final String type
        ) {
            this.title = title;
            this.path = path;
            this.dlg = dlg;
            this.resourceType = resourceType;
            this.type = type;
        }
    }

    /**
     * End marker object representation.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final class EndMarker {

        @JsonProperty
        private final String path;

        EndMarker(@Nullable final String path) {
            this.path = path;
        }
    }

    public String getPath() {
        return path;
    }

    /**
     * Selector which allow to render child resources of this component
     */
    @Nonnull
    public String getChildSelector() {
        return StringUtils.defaultString(childSelector);
    }

    @Nonnull
    public String getStartMarker() {
        return StringUtils.defaultString(startMarker);
    }

    @Nonnull
    public String getEndMarker() {
        return StringUtils.defaultString(endMarker);
    }

    @Nonnull
    public String getComponentType() {
        return COMPONENT_TYPE;
    }
}
