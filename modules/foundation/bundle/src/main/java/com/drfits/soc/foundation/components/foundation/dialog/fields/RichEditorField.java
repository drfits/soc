package com.drfits.soc.foundation.components.foundation.dialog.fields;

import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 11/13/16.
 */
public class RichEditorField extends BaseField {

    private static final Logger log = LoggerFactory.getLogger(RichEditorField.class);

    private String value;

    @Override
    public void activate() {
        log.debug("RichEditor Field activate");
        super.activate();
        log.debug("Path: {}", this.getPath());
        if (!this.getPropertyName().isEmpty()) {
            final Resource resource = this.getResourceResolver().resolve(this.getPath());
            this.value = resource.getValueMap().get(this.getPropertyName(), "");
        }
    }

    public String getValue() {
        return value;
    }
}
