package com.drfits.soc.foundation.servlets;

import java.io.IOException;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
    service = Servlet.class,
    property = {
        ServletResolverConstants.SLING_SERVLET_METHODS + "=GET",
        ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES + "=soc:Page"
    }
)
public class PageForwardServlet extends SlingSafeMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(PageForwardServlet.class);

    @Override
    protected void doGet(@Nonnull final SlingHttpServletRequest request, @Nonnull final SlingHttpServletResponse response)
        throws ServletException, IOException {
        log.debug("Page forward servlet start for: {}", request.getServletPath());
        Resource content = Optional.of(request)
            .map(SlingHttpServletRequest::getResource)
            .map(r -> r.getChild(JcrConstants.JCR_CONTENT))
            .filter(r -> !ResourceUtil.isNonExistingResource(r))
            .orElse(null);
        if (content != null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher(content);
            if (dispatcher != null) {
                log.debug("Forward request");
                dispatcher.forward(request, response);
            } else {
                log.debug("RequestDispatcher is null");
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            log.debug("Resource Not Found");
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        log.debug("Page forward servlet completed for: {}", request.getServletPath());
    }
}
