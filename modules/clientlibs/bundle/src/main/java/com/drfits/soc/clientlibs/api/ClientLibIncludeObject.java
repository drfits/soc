package com.drfits.soc.clientlibs.api;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.scripting.sightly.pojo.Use;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.script.Bindings;

/**
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 10/20/16.
 */
public final class ClientLibIncludeObject implements Use {

    private static final Logger LOG = LoggerFactory.getLogger(ClientLibIncludeObject.class);

    static final String BINDINGS_CATEGORIES = "categories";
    static final String PROP_MODE = "mode";

    private String[] categories;
    private String mode;
    private ClientLibraryStore clientLibraryStore;

    private ResourceResolver resourceResolver;

    ClientLibIncludeObject() {
        LOG.debug("Instantiate ClientLibIncludeObject");
    }

    public void init(final Bindings bindings) {
        final Object categoriesObject = bindings.get(BINDINGS_CATEGORIES);
        if (categoriesObject != null) {
            if (categoriesObject instanceof Object[]) {
                final Object[] categoriesArray = (Object[]) categoriesObject;
                this.categories = new String[categoriesArray.length];
                int i = 0;
                for (final Object category : categoriesArray) {
                    if (category instanceof String) {
                        this.categories[i++] = ((String) category).trim();
                    }
                }
            } else if (categoriesObject instanceof String) {
                this.categories = ((String) categoriesObject).split(",");
                int i = 0;
                for (final String category : this.categories) {
                    this.categories[i++] = category.trim();
                }
            }
            if (ArrayUtils.isNotEmpty(this.categories)) {
                this.mode = (String) bindings.get(PROP_MODE);
                final SlingScriptHelper sling = (SlingScriptHelper) bindings.get(SlingBindings.SLING);
                this.clientLibraryStore = sling.getService(ClientLibraryStore.class);
            }
        }
        this.resourceResolver = (ResourceResolver) bindings.get(SlingBindings.RESOLVER);
    }

    /**
     * Create HTML markup for include js and css on HTML.
     *
     * @return HTML string to include within HTML output.
     */
    public String include() {
        ClientLibraryType type = null;
        try {
            type = ClientLibraryType.valueOf(this.mode);
        } catch (final IllegalArgumentException e) {
            LOG.error(e.getMessage(), e);
        }
        final StringBuilder sb = new StringBuilder();
        if (type != null) {
            for (final String category : this.categories) {
                final ClientLibrary item = this.clientLibraryStore.get(this.resourceResolver, category);
                sb.append(this.getMarkup(item, type));
            }
        }
        return sb.toString();
    }

    private String getMarkup(final ClientLibrary library, @Nonnull final ClientLibraryType type) {
        if (library == null) {
            return "";
        }
        final StringBuilder out = new StringBuilder();
        for (final ClientLibraryItem item : library.getItems()) {
            switch (item.getType()) {
                case CSS:
                    if (type == ClientLibraryType.CSS || type == ClientLibraryType.ALL) {
                        out.append(getCssMarkup(item, library.getPath()));
                    }
                    break;
                case JS:
                    if (type == ClientLibraryType.JS || type == ClientLibraryType.ALL) {
                        out.append(getJsMarkup(item, library.getPath()));
                    }
                    break;
                default:
                    LOG.warn("Client Side Library item was not processed: {}", library.getPath());
            }
        }
        return out.toString();
    }

    private static StringBuilder getCssMarkup(final ClientLibraryItem item, final String libraryPath) {
        final StringBuilder out = new StringBuilder("<link rel=\"stylesheet\" href=\"");
        if (item.isExternal()) {
            out.append(item.getSrc());
        } else {
            out.append(libraryPath).append('/').append(item.getSrc());
        }
        out.append("\"");
        if (StringUtils.isNoneBlank(item.getIntegrity())) {
            out.append(" integrity=\"").append(item.getIntegrity()).append("\"");

        }
        if (item.getCrossOrigin() != null) {
            out.append(" crossorigin=\"").append(item.getCrossOrigin()).append("\"");
        }
        out.append(">");
        return out;
    }

    private static StringBuilder getJsMarkup(final ClientLibraryItem item, final String libraryPath) {
        final StringBuilder out = new StringBuilder("<script src=\"");
        if (item.isExternal()) {
            out.append(item.getSrc());
        } else {
            out.append(libraryPath).append('/').append(item.getSrc());
        }
        out.append("\"");
        if (StringUtils.isNoneBlank(item.getIntegrity())) {
            out.append(" integrity=\"").append(item.getIntegrity()).append("\"");

        }
        if (item.getCrossOrigin() != null) {
            out.append(" crossorigin=\"").append(item.getCrossOrigin()).append("\"");
        }
        out.append("></script>");
        return out;
    }
}