package com.drfits.soc.foundation.util;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;

import com.drfits.soc.foundation.api.Page;
import com.drfits.soc.foundation.api.PageManager;
import com.drfits.soc.foundation.exceptions.SOCException;
import com.drfits.soc.foundation.models.PageModel;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of page management
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 12/18/16.
 */
@Component(service = PageManager.class, immediate = true)
public class PageManagerImpl implements PageManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageManagerImpl.class);

    private static final String CONTENT_PATH = '/' + JcrConstants.JCR_CONTENT;

    @Override
    public Page createPage(Resource parent, String pageName, String pageTitle, String templatePath) throws SOCException {
        Page page = null;
        ResourceResolver resolver = parent.getResourceResolver();
        String destPath = parent.getPath() + '/' + pageName;
        try {
            if (resolver.getResource(destPath) != null) {
                LOGGER.info("Destination resource already exists: {}", destPath);
            } else {
                resolver.refresh();
                Session session = resolver.adaptTo(Session.class);
                if (session != null) {
                    Workspace workspace = session.getWorkspace();
                    workspace.copy(templatePath + CONTENT_PATH, destPath);
                    Resource pageResource = resolver.getResource(destPath);
                    if (pageResource != null) {
                        ValueMap properties = pageResource.adaptTo(ModifiableValueMap.class);
                        if (properties != null) {
                            properties.put(GlobalConstants.JCR_TITLE, pageTitle);
                            resolver.commit();
                            page = pageResource.adaptTo(PageModel.class);
                        }
                    }
                }
            }
        } catch (PersistenceException | RepositoryException e) {
            LOGGER.error("Couldn't create page", e);
            throw new SOCException("Couldn't create page", e);
        }
        return page;
    }

    @Override
    public Page move(Resource page, String destination, boolean resolveConflict) throws SOCException {
        throw new UnsupportedOperationException("Method not implemented yet");
    }
}
