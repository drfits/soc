package com.drfits.soc.clientlibs.impl;

import com.drfits.soc.clientlibs.api.ClientLibrary;
import com.drfits.soc.clientlibs.api.ClientLibraryStore;
import com.drfits.soc.clientlibs.config.ClientManagerConfig;
import com.drfits.soc.clientlibs.model.ClientLibraryModel;
import org.apache.jackrabbit.oak.query.SQL2Parser;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.serviceusermapping.ServiceUserMapped;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.jcr.query.Query;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component(immediate = true)
@Designate(ocd = ClientManagerConfig.class)
public class ClientLibraryStoreImpl implements ClientLibraryStore {

    private static final Logger LOG = LoggerFactory.getLogger(ClientLibraryStoreImpl.class);

    static final String Q_FIND_LIBRARY = "SELECT f.* FROM [" + CLIENT_LIBRARY_NODE_TYPE + "] AS f WHERE f.categories = ";

    public ClientLibraryStoreImpl() {
        LOG.info("Construct ClientLibraryStoreImpl");
    }

    @Override
    public List<ClientLibrary> get(@Nonnull final ResourceResolver resourceResolver, @Nonnull final String[] categories) {
        return Arrays.stream(categories)
                .map(category -> this.get(resourceResolver, category))
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public ClientLibrary get(@Nonnull final ResourceResolver resourceResolver, @Nonnull final String category) {
        LOG.debug("SQL search for client side library {}", category);
        final String query = Q_FIND_LIBRARY + SQL2Parser.escapeStringLiteral(category);
        LOG.debug("Search query {}", query);
        final Iterator<Resource> it = resourceResolver.findResources(query, Query.JCR_SQL2);
        if (it.hasNext()) {
            final Resource resource = it.next();
            final ClientLibrary clientLibrary = resource.adaptTo(ClientLibraryModel.class);
            if (clientLibrary != null) {
                LOG.debug("Found client side library: [{}]", clientLibrary.getPath());
                return clientLibrary;
            } else {
                LOG.warn("Client side library has incorrect format {}", resource.getPath());
            }
        }
        LOG.debug("No client library found for category {}", category);
        return null;
    }
}
