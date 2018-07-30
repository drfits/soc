package com.drfits.soc.foundation.sightly;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.scripting.sightly.pojo.Use;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.script.Bindings;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * More information about available objects: <a href="https://sling.apache.org/documentation/bundles/scripting/scripting-htl.html#global-objects">here</a>
 * <br/>
 *
 * @deprecated Please use Sling Models instead
 * Created by evgeniy_fitsner on 10/19/16.
 */
@Deprecated
public class WCMUse implements Use {

    private static final String GROUP_ID_AUTHORS = "authors";

    /**
     * Logger instance to log and debug errors.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WCMUse.class);

    /**
     * Global bindings populated by the init method.
     */
    private Bindings bindings;

    /**
     * Sightly component initialization.
     *
     * @param bindings The current execution context.
     */
    @Override
    public void init(Bindings bindings) {
        this.bindings = bindings;
        activate();
    }

    /**
     * The activate method is meant to be overridden as it's the
     * entry point to the extended class.
     */
    public void activate() {
    }

    /**
     * Get the current resource.
     *
     * @return The current resource.
     */
    public Resource getResource() {
        return (Resource) bindings.get(SlingBindings.RESOURCE);
    }

    /**
     * Get the resource resolver backed by the current resource.
     *
     * @return The current resource resolver.
     */
    public ResourceResolver getResourceResolver() {
        return getResource().getResourceResolver();
    }

    /**
     * Get the current resource properties.
     *
     * @return The current resource properties.
     */
    public ValueMap getProperties() {
        return getResource().adaptTo(ValueMap.class);
    }

    /**
     * Get the current Sling Script Helper.
     *
     * @return The current Sling Script Helper.
     */
    public SlingScriptHelper getSlingScriptHelper() {
        return (SlingScriptHelper) bindings.get(SlingBindings.SLING);
    }

    /**
     * Get the current request.
     *
     * @return The current Sling HTTP Servlet Request.
     */
    public SlingHttpServletRequest getRequest() {
        return (SlingHttpServletRequest) bindings.get(SlingBindings.REQUEST);
    }

    /**
     * Get the current response.
     *
     * @return The current Sling HTTP Servlet Response.
     */
    public SlingHttpServletResponse getResponse() {
        return (SlingHttpServletResponse) bindings.get(SlingBindings.RESPONSE);
    }

    /**
     * Get the current JCR session.
     *
     * @return The current JCR session.
     */
    public Session getSession() {
        return getResourceResolver().adaptTo(Session.class);
    }

    /**
     * Get the authorable status of the current user.
     * TODO: remove and use UserService
     *
     * @return true if the current user is an admin or author.
     */
    public boolean isAuthorable() {
        boolean authorable = false;

        JackrabbitSession js = (JackrabbitSession) getSession();

        try {
            UserManager userManager = js.getUserManager();
            Group authors = (Group) userManager.getAuthorizable(GROUP_ID_AUTHORS);
            User user = (User) js.getUserManager().getAuthorizable(js.getUserID());

            authorable = user.isAdmin() || authors.isMember(user);
        } catch (RepositoryException e) {
            LOGGER.error("Could not determine group membership", e);
        }

        return authorable;
    }

    /**
     * Generate the absolute resource path from the relative path.
     *
     * @return The absolute blog post display path.
     */
    public String getAbsolutePath(final String relativePath) {
        String displayPath = null;
        String newRelativePath = relativePath;

        if (StringUtils.isNotBlank(newRelativePath)) {
            try {
                URI uri = new URI(getRequest().getRequestURL().toString());

                if (relativePath.startsWith("/content/")) {
                    newRelativePath = StringUtils.removeStart(newRelativePath, "/content");
                }

                newRelativePath = StringUtils.removeEnd(newRelativePath, "/");

                displayPath = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(),
                        uri.getPort(), newRelativePath, uri.getQuery(), uri.getFragment()
                ).toString();
            } catch (URISyntaxException e) {
                LOGGER.error("Could not get create absolute path from Request URL", e);
            }
        }

        return displayPath;
    }
}