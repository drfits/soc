package com.drfits.soc.clientlibs.model;

import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

import com.drfits.soc.clientlibs.api.ClientLibrary;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

/**
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 10/20/16.
 */
@Model(
    adaptables = Resource.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public final class ClientLibraryModel implements ClientLibrary {

    @Inject
    private List<String> categories = Collections.emptyList();

    @Inject
    @Default(values = "")
    private String baseCssPath;

    @Inject
    private List<String> css = Collections.emptyList();

    @Inject
    @Default(values = "")
    private String baseJsPath;

    @Inject
    private List<String> js = Collections.emptyList();

    @Inject
    private ClientLibraryModel external;

    private String path;

    @Inject
    public ClientLibraryModel(@Self Resource resource) {
        path = resource.getPath();
    }

    @Override
    public List<String> getCategories() {
        return categories;
    }

    @Override
    public String getBaseCssPath() {
        return baseCssPath;
    }

    @Override
    public List<String> getCss() {
        return css;
    }

    @Override
    public String getBaseJsPath() {
        return baseJsPath;
    }

    @Override
    public List<String> getJs() {
        return js;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public ClientLibraryModel getExternal() {
        return external;
    }
}
