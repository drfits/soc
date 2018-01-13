package com.drfits.soc.foundation.i18n;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 11/7/16.
 */
@ObjectClassDefinition(
        name = "Request Locale Resolver Config"
)
public @interface RequestLocaleResolverImplConfig {
    @AttributeDefinition(
            name = "Default locale",
            description = "If locale couldn't be resolved default locale will set"
    )
    String defaultLocale() default "en";
}
