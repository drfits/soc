package com.drfits.soc.foundation.exceptions;

/**
 * Exception used for failing SOC core operations
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 1/5/17.
 */
public class SOCException extends Exception {

    public SOCException() {
        super();
    }

    public SOCException(String message) {
        super(message);
    }

    public SOCException(String message, Throwable cause) {
        super(message, cause);
    }

    public SOCException(Throwable cause) {
        super(cause);
    }
}
