package com.drfits.soc.foundation.components.admin;

import com.drfits.soc.foundation.sightly.WCMUse;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.auth.core.spi.AuthenticationHandler;

/**
 * Created by evgeniy_fitsner on 10/19/16.
 */
public class Login extends WCMUse {

    /**
     * The request.
     */
    private SlingHttpServletRequest request;

    /**
     * The JAAS provided reason that authentication failed.
     */
    private String reason;

    /**
     * Initialize this Sightly component.
     */
    @Override
    public void activate() {
        request = getRequest();
        reason = request.getParameter(AuthenticationHandler.FAILURE_REASON);
    }

    /**
     * Get the reason that authentication failed.
     *
     * @return The reason that authentication failed.
     */
    public String getReason() {
        return reason;
    }
}
