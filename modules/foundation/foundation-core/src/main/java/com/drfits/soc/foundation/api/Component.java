package com.drfits.soc.foundation.api;

import com.drfits.soc.foundation.util.GlobalConstants;

/**
 * Component object represents definition
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 06/09/17.
 */
public interface Component {

    /**
     * Returns the resource type to be used for this component. Can be overridden by setting the "sling:resourceType".
     * @return resource type
     */
    String getResourceType();

    /**
     * Component title from {@link GlobalConstants#JCR_TITLE}
     * @return component title
     */
    String getTitle();

    /**
     * Component description from {@link GlobalConstants#JCR_DESCRIPTION}
     * @return component description
     */
    String getDescription();

    /**
     * Paths which are allowed to be a parent of this component.
     * @return allowed parents for this component
     */
    String[] getAllowedParents();

    /**
     * Group under which this component can be selected within side-kick.
     * @return component group
     */
    String getComponentGroup();
}
