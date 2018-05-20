package com.drfits.soc.foundation.components.foundation.dialog.fields;

import com.drfits.soc.foundation.sightly.WCMUse;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.servlets.post.SlingPostConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 11/13/16.
 */
public abstract class BaseField extends WCMUse {

    // Dialog field which specify where value should be saved
    public static final String NAME_FIELD = "name";

    private static final Logger log = LoggerFactory.getLogger(BaseField.class);

    // Field path for resource resolver
    private String path = "";

    private String propertyName = "";

    @Override
    public void activate() {
        String targetPath = StringUtils.defaultIfEmpty(getRequest().getRequestPathInfo().getSuffix(), "");
        targetPath = StringUtils.substringBefore(targetPath, ".");
        log.debug("Suffix: {}", targetPath);
        String targetName = getProperties().get(NAME_FIELD, "");
        log.debug("Dialog field value: {}", targetPath);
        if (!targetPath.isEmpty() && !targetName.isEmpty()) {
            if (targetName.startsWith(SlingPostConstants.ITEM_PREFIX_RELATIVE_CURRENT)) {
                targetName = StringUtils.substringAfter(targetName, SlingPostConstants.ITEM_PREFIX_RELATIVE_CURRENT);
            } else if (targetName.startsWith(SlingPostConstants.ITEM_PREFIX_RELATIVE_PARENT)) {
                targetPath = StringUtils.substringBeforeLast(targetPath, "/");
                targetName = StringUtils.substringAfter(targetName, SlingPostConstants.ITEM_PREFIX_RELATIVE_PARENT);
            }
            // If resource exists - resolve relative name
            Resource resource = getResourceResolver().resolve(targetPath);
            if (!ResourceUtil.isNonExistingResource(resource)) {
                int deep = StringUtils.countMatches(targetName, "/");
                if (deep > 0) {
                    path = targetPath + "/" + StringUtils.substringBeforeLast(targetName, "/");
                    propertyName = StringUtils.substringAfterLast(targetName, "/");
                } else {
                    path = targetPath;
                    propertyName = targetName;
                }
            }
        }
        log.debug("Activate base area template");
    }

    public String getPath() {
        return path;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
