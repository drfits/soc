package com.drfits.soc.foundation.api;

/**
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 11/14/16.
 */
public interface Template {

    /**
     * Template title
     *
     * @return
     */
    String getTitle();

    /**
     * Template description
     *
     * @return
     */
    String getDescription();

    /**
     * Path of a template that is allowed to be a parent of this template.
     *
     * @return
     */
    String[] getAllowedParents();

    /**
     * Path of a template that is allowed to be a child of this template.
     *
     * @return
     */
    String[] getAllowedChildren();

    /**
     * Path of a page that is allowed to be based on this template.
     *
     * @return
     */
    String[] getAllowedPaths();
}
