package com.drfits.soc.foundation.api;

import java.util.List;

/**
 * Provides methods to operate with components.
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 06/09/17.
 */
public interface ComponentManager {

    String SUBSERVICE_NAME = "component-manager";

    /**
     * List of available components which can be inserted into specified path
     *
     * @param path for which components will be searched
     * @return list of available components
     */
    List<Component> getComponents(String path);

}
