package com.drfits.soc.foundation.api;

import org.apache.sling.api.resource.Resource;

import java.util.List;

/**
 * PageAreaModel to place and manage components inside it
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 11/15/16.
 */
public interface Area {

    List<Resource> getChildren();
}
