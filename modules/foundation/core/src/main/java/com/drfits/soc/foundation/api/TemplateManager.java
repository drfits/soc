package com.drfits.soc.foundation.api;

import java.util.List;

/**
 * Provides methods to operate with templates.
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 12/18/16.
 */
public interface TemplateManager {
    /**
     * List of available templates to apply under specified path
     *
     * @param path for which templates will be searched
     * @return list of available templates
     */
    List<Template> getTemplates(String path);

}
