package com.drfits.soc.foundation.components.foundation.dialog.fields;

import com.drfits.soc.foundation.sightly.WCMUse;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 11/13/16.
 */
public class ContainerField extends WCMUse {

    private static final Logger log = LoggerFactory.getLogger(ContainerField.class);

    private List<Resource> children;

    @Override
    public void activate() {
        log.debug("Activate base area template");
    }

}
