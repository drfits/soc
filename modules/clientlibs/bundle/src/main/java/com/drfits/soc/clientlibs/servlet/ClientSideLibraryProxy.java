package com.drfits.soc.clientlibs.servlet;


import com.drfits.soc.clientlibs.api.ClientLibraryStore;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.sling.api.servlets.ServletResolverConstants.*;

@Component(
        service = Servlet.class,
        property = {
                SLING_SERVLET_PATHS + "=" + ClientSideLibraryProxy.PROXY_SERVLET_PATH,
                SLING_SERVLET_METHODS + "=GET",
                SLING_SERVLET_SELECTORS + "=" + ClientSideLibraryProxy.PROXY_SERVLET_SELECTOR,
        }
)
public final class ClientSideLibraryProxy extends SlingSafeMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(ClientSideLibraryProxy.class);

    /**
     * Base path from which proxy servlet will retrieve client library item content.
     */
    public static final String PROXY_TARGET_PATH = "/apps";
    static final String PROXY_SERVLET_PATH = "/etc";
    static final String PROXY_SERVLET_SELECTOR = "clientlibs";
    public static final String PROXY_URL_PATH = PROXY_SERVLET_PATH + '.' + PROXY_SERVLET_SELECTOR;
    /**
     * For performance purpoces retrieve property directly rather then from model abstraction.
     */
    private static final String ALLOW_PROXY_PROPERTY = "allowProxy";

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private ClientLibraryStore clientLibraryStore;

    @Override
    protected void doGet(@Nonnull final SlingHttpServletRequest request, @Nonnull final SlingHttpServletResponse response) throws ServletException, IOException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Get client library resource: [{}]", request.getRequestPathInfo().getSuffix());
        }
        try (final ResourceResolver resolver = this.resolverFactory.getServiceResourceResolver(null)) {
            if (StringUtils.isBlank(request.getRequestPathInfo().getSuffix())) {
                throw new IllegalArgumentException("Suffix parameter is null or empty.");
            }
            final String path = PROXY_TARGET_PATH + request.getRequestPathInfo().getSuffix();
            if (LOG.isTraceEnabled()) {
                LOG.trace("Client library path: {}", path);
            }
            final Resource clientLibResource = resolver.getResource(path);
            if (clientLibResource == null) {
                throw new IllegalArgumentException("Incorrect path " + path + " provided.");
            }
            if (this.isProxyEnabled(clientLibResource)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Client library node allow proxy: true");
                }
                final Node clientLibNode = clientLibResource.adaptTo(Node.class);
                if (clientLibNode != null) {
                    final String mimeType = JcrUtils.getStringProperty(clientLibNode, Node.JCR_CONTENT + '/' + Property.JCR_MIMETYPE, "");
                    if (StringUtils.isNotBlank(mimeType)) {
                        response.setContentType(mimeType);
                    }
                    JcrUtils.readFile(clientLibNode, response.getOutputStream());
                } else {
                    LOG.error("Cannot read the file.");
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot read the file.");
                }
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Client library node allow proxy: false");
                }
            }
        } catch (final LoginException e) {
            LOG.error("Cannot login for search: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (final IllegalArgumentException e) {
            LOG.warn("Cannot get content: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (final RepositoryException e) {
            LOG.error("Cannot read the file: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot read the file.");
        }
    }

    private boolean isProxyEnabled(final Resource clientLibResource) {
        Resource parent = clientLibResource;
        while (!PROXY_TARGET_PATH.equals(parent.getPath())) {
            parent = parent.getParent();
            if (parent == null) {
                throw new IllegalArgumentException("Incorrect path " + clientLibResource.getPath() + " provided.");
            }
            if (parent.isResourceType(ClientLibraryStore.CLIENT_LIBRARY_NODE_TYPE)) {
                LOG.debug("Client library node found: {}", parent.getPath());
                return parent.getValueMap().get(ALLOW_PROXY_PROPERTY, false);
            }
        }
        return false;
    }
}
