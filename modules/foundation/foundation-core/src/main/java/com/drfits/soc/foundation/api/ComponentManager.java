package com.drfits.soc.foundation.api;

import java.util.List;

/**
 * Provides methods to operate with components.
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 06/09/17.
 */
public interface ComponentManager {
    /**
     * List of available components to apply under specified path
     *
     * @param path for which components will be searched
     * @return list of available components
     */
    List<Component> getComponents(String path);

}
