package com.drfits.soc.clientlibs.api;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 10/20/16.
 */
public interface ClientLibrary extends Serializable {

    /**
     * Client library resource path
     *
     * @return path to Client library
     */
    String getPath();

    /**
     * Client library categories
     *
     * @return list of categories name
     */
    List<String> getCategories();

    /**
     * Client library items (Ex.: CSS and JS files).
     *
     * @return list of entries.
     */
    List<ClientLibraryItem> getItems();
}
