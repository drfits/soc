package com.drfits.soc.clientlibs.api;

import com.drfits.soc.clientlibs.impl.ClientLibraryType;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.scripting.sightly.pojo.Use;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Bindings;

/**
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 10/20/16.
 */
public class ClientLibIncludeObject implements Use {

    private static final Logger log = LoggerFactory.getLogger(ClientLibIncludeObject.class);

    private static final String BINDINGS_CATEGORIES = "categories";

    private static final String PROP_MODE = "mode";

    private String[] categories;
    private String mode;
    private ClientLibraryManager clientLibraryManager;

    public void init(Bindings bindings) {
        Object categoriesObject = bindings.get(BINDINGS_CATEGORIES);
        if (categoriesObject != null) {
            if (categoriesObject instanceof Object[]) {
                Object[] categoriesArray = (Object[]) categoriesObject;
                categories = new String[categoriesArray.length];
                int i = 0;
                for (Object o : categoriesArray) {
                    if (o instanceof String) {
                        categories[i++] = ((String) o).trim();
                    }
                }
            } else if (categoriesObject instanceof String) {
                categories = ((String) categoriesObject).split(",");
                int i = 0;
                for (String c : categories) {
                    categories[i++] = c.trim();
                }
            }
            if (categories != null && categories.length > 0) {
                mode = (String) bindings.get(PROP_MODE);
                SlingScriptHelper sling = (SlingScriptHelper) bindings.get(SlingBindings.SLING);
                clientLibraryManager = sling.getService(ClientLibraryManager.class);
            }
        }
    }

    /**
     * Create HTML markup for include js and css on HTML.
     *
     * @return
     */
    public String include() {
        ClientLibraryType type = null;
        try {
            type = ClientLibraryType.valueOf(mode);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
        }
        final StringBuilder sb = new StringBuilder();
        if (type != null) {
            sb.append(clientLibraryManager.getIncludeMarkup(categories, type));
        }
        return sb.toString();
    }

}