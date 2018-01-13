package com.drfits.soc.foundation.util;

import org.apache.sling.api.resource.ResourceResolver;

/**
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 10/21/16.
 */
public class SOCUtil {
    /**
     * Close specified resource resolver
     *
     * @param resolver for close
     */
    public static void close(ResourceResolver resolver) {
        if (resolver != null && resolver.isLive()) {
            resolver.close();
        }
    }
}
