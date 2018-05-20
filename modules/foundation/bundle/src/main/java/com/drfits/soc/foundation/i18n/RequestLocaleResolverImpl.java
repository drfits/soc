package com.drfits.soc.foundation.i18n;

import com.google.common.collect.ImmutableList;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.i18n.RequestLocaleResolver;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

/**
 * Locale will be resolved in this way (first matched will be used):
 * <ul>
 * <li>check {@link RequestLocaleResolverImpl#LANGUAGE_SESSION_ATTRIBUTE} variable in session</li>
 * <li>check request locales and take language from it</li>
 * </ul>
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 11/7/16.
 */
@Component(
        immediate = true,
        service = {RequestLocaleResolver.class}
)
@Designate(ocd = RequestLocaleResolverImplConfig.class)
public class RequestLocaleResolverImpl implements RequestLocaleResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLocaleResolverImpl.class);

    private RequestLocaleResolverImplConfig config;

    /**
     * Attribute which specifying language for request
     */
    public static final String LANGUAGE_SESSION_ATTRIBUTE = "user-lang";

    @Activate
    @Modified
    protected void init(ComponentContext componentCtx, BundleContext bundleCtx, RequestLocaleResolverImplConfig config) {
        this.config = config;
        if (LOGGER.isInfoEnabled() && this.config != null) {
            LOGGER.info("RequestLocaleResolverImpl configuration");
            LOGGER.info("Default locale: {}", this.config.defaultLocale());
        }
    }

    public List<Locale> resolveLocale(final SlingHttpServletRequest request) {
        return this.resolveLocale((HttpServletRequest) request);
    }

    /**
     * @see org.apache.sling.i18n.RequestLocaleResolver#resolveLocale(javax.servlet.http.HttpServletRequest)
     */
    public List<Locale> resolveLocale(final HttpServletRequest request) {
        List<Locale> localeList = null;
        HttpSession session = request.getSession();
        if (session != null) {
            String lang = (String) session.getAttribute(LANGUAGE_SESSION_ATTRIBUTE);
            if (lang != null && !lang.isEmpty()) {
                localeList = ImmutableList.of(new Locale(lang));
            }
        }
        if (localeList == null) {
            localeList = new ArrayList<>();
            Enumeration<?> locales = request.getLocales();
            while (locales.hasMoreElements()) {
                localeList.add((Locale) locales.nextElement());
            }
        }

        return localeList;
    }
}
