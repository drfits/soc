package com.drfits.soc.foundation.components;

import com.drfits.soc.foundation.sightly.WCMUse;
import org.apache.sling.settings.SlingSettingsService;

public class EditorAuthorLibs extends WCMUse {

    private final static String EDIT_MODE = "edit";
    private final static String AUTHOR_MODE = "author";
    private final static String PUBLISH_MODE = "publish";

    private final static String UI_MODE_PARAM = "uimode";

    private SlingSettingsService slingSettingsService;

    private String uiMode = "";

    @Override
    public void activate() {
        slingSettingsService = getSlingScriptHelper().getService(SlingSettingsService.class);
        uiMode = getRequest().getParameter(UI_MODE_PARAM);
    }

    public boolean isEditMode() {
        return EDIT_MODE.equalsIgnoreCase(uiMode) || slingSettingsService.getRunModes().contains(EDIT_MODE);
    }

    public boolean isAuthorMode() {
        return AUTHOR_MODE.equalsIgnoreCase(uiMode) || slingSettingsService.getRunModes().contains(AUTHOR_MODE);
    }

    public boolean isPublishMode() {
        return PUBLISH_MODE.equalsIgnoreCase(uiMode) || slingSettingsService.getRunModes().contains(PUBLISH_MODE);
    }
}
