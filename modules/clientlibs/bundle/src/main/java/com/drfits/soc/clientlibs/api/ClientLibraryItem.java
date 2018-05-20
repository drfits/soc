package com.drfits.soc.clientlibs.api;

import java.io.Serializable;

/**
 * Represents one entry within client library.
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 10/20/16.
 */
public interface ClientLibraryItem extends Serializable {

    /**
     * If entry represents external resource.<br>
     * <b>Default:</b> false
     *
     * @return true if external resource, false - otherwise.
     */
    boolean isExternal();

    /**
     * Cross origin for item (can be skipped or specified).
     *
     * @return <b>null</b> - if not specified, value - if <b>empty string</b> or any value was specified.
     */
    String getCrossOrigin();

    /**
     * Source destination.
     *
     * @return source path where data located.
     */
    String getSrc();

    /**
     * Cryptographic hash for "Sub resource Integrity (SRI)"
     *
     * @return hash if specified.
     */
    String getIntegrity();

    /**
     * Type of this entry
     *
     * @return client library type.
     */
    ClientLibraryType getType();
}
