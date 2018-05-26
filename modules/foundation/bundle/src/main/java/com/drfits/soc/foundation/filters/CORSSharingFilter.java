package com.drfits.soc.foundation.filters;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.engine.EngineConstants;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Designate(ocd = CORSSharingFilter.Config.class)
@Component(
        service = Filter.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Cross-Origin Resource Sharing Filter for development.",
                EngineConstants.SLING_FILTER_SCOPE + "=" + EngineConstants.FILTER_SCOPE_REQUEST,
                Constants.SERVICE_RANKING + "=100"
        },
        configurationPolicy = ConfigurationPolicy.REQUIRE)
public final class CORSSharingFilter implements Filter {

    @ObjectClassDefinition(
            name = "SOC: Access Control Headers filter",
            description = "Filter that allow manage Access-Control headers (CORS, TTL and so on). ")
    public @interface Config {
        @AttributeDefinition(name = "Access-Control-Allow-Origin",
                description = "List of allowed origins. Use * to allow any." +
                        "For more information: https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Access-Control-Allow-Origin")
        String[] origins() default {};

        @AttributeDefinition(name = "Access-Control-Max-Age",
                description = "The Access-Control-Max-Age response header indicates how long the results of a preflight " +
                        "request (that is the information contained in the Access-Control-Allow-Methods and " +
                        "Access-Control-Allow-Headers headers) can be cached.")
        int maxAge() default -1;

        @AttributeDefinition(name = "Access-Control-Allow-Methods",
                description = "List of the allowed HTTP request methods.")
        String[] allowMethods() default {"GET", "POST", "OPTIONS"};

        @AttributeDefinition(name = "Access-Control-Allow-Credentials",
                description = "The Access-Control-Allow-Credentials response header indicates whether or not the " +
                        "response to the request can be exposed to the page. It can be exposed when the true value is returned.")
        boolean allowCredentials() default false;
    }

    private enum ACCEPT_ORIGIN_STATE {
        NONE, ANY, DOMAIN
    }

    private static final Logger LOG = LoggerFactory.getLogger(CORSSharingFilter.class);

    private static final String ANY_ORIGIN_WILDCARD = "*";

    private static final String HEADER_ORIGIN = "Origin";
    private static final String HEADER_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    private static final String HEADER_VARY = "Vary";
    private static final String HEADER_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    private static final String HEADER_ALLOW_METHODS = "Access-Control-Allow-Methods";
    private static final String HEADER_MAX_AGE = "Access-Control-Max-Age";

    private ACCEPT_ORIGIN_STATE originState;
    private List<String> origins;
    private int maxAge;
    private String allowMethods;
    private boolean allowCredentials;

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        LOG.info("CORSSharingFilter initialized.");
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse servletResponse = (HttpServletResponse) response;
        if (this.originState != ACCEPT_ORIGIN_STATE.NONE) {
            if (this.originState == ACCEPT_ORIGIN_STATE.ANY) {
                servletResponse.setHeader(HEADER_ALLOW_ORIGIN, ANY_ORIGIN_WILDCARD);
            } else {
                final HttpServletRequest servletRequest = (HttpServletRequest) request;
                final String origin = StringUtils.defaultString(servletRequest.getHeader(HEADER_ORIGIN));
                if (this.origins.contains(origin)) {
                    servletResponse.setHeader(HEADER_ALLOW_ORIGIN, origin);
                    servletResponse.setHeader(HEADER_VARY, origin);
                }
            }
        }
        if (this.allowCredentials) {
            servletResponse.setHeader(HEADER_ALLOW_CREDENTIALS, "true");
        }
        if (!this.allowMethods.isEmpty()) {
            servletResponse.setHeader(HEADER_ALLOW_METHODS, this.allowMethods);
        }
        if (this.maxAge > -1) {
            servletResponse.setHeader(HEADER_MAX_AGE, String.valueOf(this.maxAge));
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        LOG.info("CORSSharingFilter destroyed.");
    }

    @Activate
    private void activate(final Config config) {
        this.origins = Collections.emptyList();
        final List<String> allowedOrigins = Arrays.stream(config.origins())
                .filter(StringUtils::isNotBlank)
                .sorted()
                .collect(Collectors.toList());
        if (allowedOrigins.isEmpty()) {
            this.originState = ACCEPT_ORIGIN_STATE.NONE;
        } else if (allowedOrigins.size() == 1 && allowedOrigins.contains(ANY_ORIGIN_WILDCARD)) {
            this.originState = ACCEPT_ORIGIN_STATE.ANY;
        } else {
            this.originState = ACCEPT_ORIGIN_STATE.DOMAIN;
            this.origins = allowedOrigins;
        }
        this.maxAge = config.maxAge();
        this.allowMethods = Arrays.stream(config.allowMethods())
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(", "));
        this.allowCredentials = config.allowCredentials();
        LOG.debug("Activated: origins=[{} - {}], TTL=[{}], methods=[{}], allow credentials=[{}].",
                allowedOrigins.toArray(), this.originState, this.maxAge, this.allowMethods, this.allowCredentials);
    }
}
