package com.drfits.soc.foundation.area.dialog.fields;

import com.drfits.soc.foundation.api.Component;
import com.drfits.soc.foundation.api.ComponentManager;
import com.drfits.soc.foundation.components.foundation.dialog.fields.BaseField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 11/13/16.
 */
public class OptionField extends BaseField {

    private static final Logger log = LoggerFactory.getLogger(OptionField.class);

    private List<Component> options;

    @Override
    public void activate() {
        log.debug("Option Field activate");
        super.activate();
        log.debug("Path: {}", getPath());
        ComponentManager cm = getSlingScriptHelper().getService(ComponentManager.class);
        if (cm != null) {
            options = cm.getComponents(getPath());
        }
    }

    public List<Component> getOptions() {
        return options;
    }
}
