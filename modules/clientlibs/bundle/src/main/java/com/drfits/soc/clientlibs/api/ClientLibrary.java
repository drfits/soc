package com.drfits.soc.clientlibs.api;

import java.io.Serializable;
import java.util.List;

import com.drfits.soc.clientlibs.model.ClientLibraryModel;

/**
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 10/20/16.
 */
public interface ClientLibrary extends Serializable {

    /**
     * Client library resource path
     * @return path to Client library
     */
    String getPath();

    /**
     * Client library categories
     * @return list of categories name
     */
    List<String> getCategories();

    /**
     * Path to folder where CSS files locates
     * @return category name without start "/".<br/>
     * Ex.: cssFilesFolder/version1
     */
    String getBaseCssPath();

    /**
     * CSS file names which client library includes. Names should be relative to baseCssPath without start "/"
     * @return list of file names
     */
    List<String> getCss();

    /**
     * Path to folder where JS files locates
     * @return category name without start "/".<br/>
     * Ex.: jsFilesFolder/version1
     */
    String getBaseJsPath();

    /**
     * JS file names which client library includes. Names should be relative to baseJsPath without start "/"
     * @return list of file names
     */
    List<String> getJs();

    /**
     * Provide list of external dependencies (ust for CDN or other static files delivery networks).
     * @return {@link ClientLibraryModel} object which contains list of ready to use within html markup code fragments
     */
    ClientLibraryModel getExternal();
}
