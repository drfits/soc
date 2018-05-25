package com.drfits.soc.clientlibs.model;

import com.drfits.soc.clientlibs.api.ClientLibrary;
import com.drfits.soc.clientlibs.api.ClientLibraryItem;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 10/20/16.
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public final class ClientLibraryModel implements ClientLibrary {

    private static final Logger LOG = LoggerFactory.getLogger(ClientLibraryModel.class);

    private final String path;

    @Inject
    private boolean allowProxy;

    @Inject
    private final List<String> categories = Collections.emptyList();

    @Inject
    private final List<ClientLibraryItem> items = Collections.emptyList();

    @Inject
    public ClientLibraryModel(@Nonnull @Self final Resource resource) {
        this.path = resource.getPath();
        LOG.debug("Construct ClientLibraryModel for {}", this.path);
    }

    @PostConstruct
    protected void init() {
        LOG.debug("Init ClientLibraryModel");
    }

    @Override
    public List<String> getCategories() {
        return this.categories;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public List<ClientLibraryItem> getItems() {
        return this.items;
    }

    @Override
    public boolean allowProxy() {
        return this.allowProxy;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final ClientLibraryModel that = (ClientLibraryModel) o;
        return Objects.equals(this.getPath(), that.getPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getPath());
    }
}
