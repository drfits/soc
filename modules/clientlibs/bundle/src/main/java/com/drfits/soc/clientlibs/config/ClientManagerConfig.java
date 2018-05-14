package com.drfits.soc.clientlibs.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Created by evgeniy_fitsner on 10/27/16.
 */
@ObjectClassDefinition(
    name = "Client Manager Configuration"
)
public @interface ClientManagerConfig {

    @AttributeDefinition(
        name = "Scan paths",
        description = "Paths for scan during client libraries resolving"
    )
    String[] paths() default { "/etc/clientlibs" };

    @AttributeDefinition(
        name = "Cache size",
        description = "Size of cached client libraries"
    )
    long cacheSize() default 32;

    @AttributeDefinition(
        name = "Cache expiration",
        description = "Time in seconds after which cached client library will be expired"
    )
    long expireAfterWrite() default 5 * 60;
}
