package com.drfits.soc.foundation.components.foundation.dialog.fields;

import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 11/13/16.
 */
public class InputField extends BaseField {

    private static final Logger log = LoggerFactory.getLogger(InputField.class);

    private String value;

    @Override
    public void activate() {
        log.debug("Input Field activate");
        super.activate();
        log.debug("Path: {}", getPath());
        if (!getPropertyName().isEmpty()) {
            Resource resource = getResourceResolver().resolve(getPath());
            value = resource.getValueMap().get(getPropertyName(), "");
        }
    }

    public String getValue() {
        return value;
    }
}
