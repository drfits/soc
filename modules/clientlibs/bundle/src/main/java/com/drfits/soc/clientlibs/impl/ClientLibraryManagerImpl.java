package com.drfits.soc.clientlibs.impl;

import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import javax.jcr.query.Query;

import com.drfits.soc.clientlibs.api.ClientLibrary;
import com.drfits.soc.clientlibs.api.ClientLibraryManager;
import com.drfits.soc.clientlibs.config.ClientManagerConfig;
import com.drfits.soc.clientlibs.model.ClientLibraryModel;
import com.drfits.soc.clientlibs.model.ClientLibraryServiceUser;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.oak.query.SQL2Parser;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true)
@Designate(ocd = ClientManagerConfig.class)
public class ClientLibraryManagerImpl implements ClientLibraryManager {

    private static final Logger log = LoggerFactory.getLogger(ClientLibraryManagerImpl.class);

    private static final String Q_FIND_LIBRARY = "SELECT f.* FROM [" + CLIENT_LIBRARY_NODE_TYPE + "] AS f WHERE f.categories = ";

    private static volatile LoadingCache<String, ClientLibrary> librariesCache;

    private String[] libPaths;

    private static final Map<String, Object> SERVICE_USER = ImmutableMap.of(
        ResourceResolverFactory.USER, ClientLibraryServiceUser.NAME
    );

    @Reference
    private ResourceResolverFactory resolverFactory;

    private static void addCssMarkup(StringBuilder sb, ClientLibrary clientLibrary, ResourceResolver resolver) {
        final List<String> paths = new ArrayList<>();
        for (final String path : clientLibrary.getCss()) {
            final String filePath = StringUtils.removeStart(clientLibrary.getBaseCssPath() + '/' + path, "/");
            final Resource childRes = resolver.getResource(clientLibrary.getPath() + '/' + filePath);
            if (childRes != null) {
                paths.add(childRes.getPath());
            }
        }
        for (final String path : paths) {
            sb.append("<link rel=\"stylesheet\" href=\"").append(path).append("\">");
        }
        Optional.of(clientLibrary)
            .map(ClientLibrary::getExternal)
            .map(ClientLibraryModel::getCss)
            .ifPresent(css -> css.forEach(sb::append));
    }

    private static void addJsMarkup(StringBuilder sb, ClientLibrary clientLibrary, ResourceResolver resolver) {
        final List<String> paths = new ArrayList<>();
        for (final String path : clientLibrary.getJs()) {
            final String filePath = StringUtils.removeStart(clientLibrary.getBaseJsPath() + '/' + path, "/");
            final Resource childRes = resolver.getResource(clientLibrary.getPath() + '/' + filePath);
            if (childRes != null) {
                paths.add(childRes.getPath());
            }
        }
        for (final String path : paths) {
            sb.append("<script type=\"text/javascript\" src=\"").append(path).append("\"></script>");
        }
        Optional.of(clientLibrary)
            .map(ClientLibrary::getExternal)
            .map(ClientLibraryModel::getJs)
            .ifPresent(js -> js.forEach(sb::append));
    }

    @Activate
    @Modified
    protected void activate(ClientManagerConfig config) {
        log.info("[*** ClientLibraryManagerImpl]: activating");
        libPaths = config.paths();
        log.info("[*** ClientLibraryManagerImpl] lib paths: {}", (Object[]) libPaths);
        librariesCache = CacheBuilder.<String, ClientLibrary>newBuilder()
            .maximumSize(config.cacheSize())
            .expireAfterWrite(config.expireAfterWrite(), TimeUnit.SECONDS)
            .build(
                new CacheLoader<String, ClientLibrary>() {
                    public ClientLibrary load(@Nonnull String category) throws Exception {
                        return findClientLibrary(category);
                    }
                });
    }

    @Override
    public String getIncludeMarkup(@Nonnull final String[] categories, @Nonnull final ClientLibraryType libraryType) {
        final Deque<ClientLibrary> libraries = libraries(Arrays.asList(categories));
        return Optional.ofNullable(libraries).map(libs -> getMarkup(libs, libraryType)).orElse("");
    }

    @Override
    public void invalidate(@Nonnull final String[] categories) {
        log.debug("Invalidate categories: {}", (Object[]) categories);
        Arrays.stream(categories).forEach(c -> librariesCache.invalidate(c));
    }

    @Override
    public void invalidateAll() {
        log.debug("All client libs cache invalidated");
        librariesCache.invalidateAll();
    }

    private ClientLibrary getClientLibrary(String category) {
        try {
            log.debug("Get clientlib from cache {}", category);
            return librariesCache.get(category, () -> findClientLibrary(category));
        } catch (Exception e) {
            log.warn("Can't find category {}", category);
        }
        return null;
    }

    private ClientLibrary findClientLibrary(String category) {
        log.debug("SQL search for clientlib {}", category);
        ClientLibrary clientLibrary = null;
        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(SERVICE_USER)) {
            for (String path : libPaths) {
                final String query = Q_FIND_LIBRARY + SQL2Parser.escapeStringLiteral(category) +
                    " AND ISDESCENDANTNODE(f, " + SQL2Parser.escapeStringLiteral(path) + ")";
                log.debug("Search query {}", query);
                final Iterator<Resource> it = resolver.findResources(query, Query.JCR_SQL2);
                if (it.hasNext()) {
                    Resource resource = it.next();
                    clientLibrary = resource.adaptTo(ClientLibraryModel.class);
                    if (clientLibrary != null) {
                        log.debug("Found clientlibrary underneath {}", clientLibrary.getPath());
                        break;
                    } else {
                        log.warn("Clientlibrary has incorrect format {}", resource.getPath());
                    }
                }
            }
        } catch (LoginException e) {
            log.error(e.getMessage(), e);
        }
        if (clientLibrary == null) {
            log.debug("No client library found for category {}", category);
        }
        return clientLibrary;
    }

    private String getMarkup(@Nonnull final Queue<ClientLibrary> libraries, @Nonnull final ClientLibraryType type) {
        final StringBuilder sb = new StringBuilder();
        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(SERVICE_USER)) {
            switch (type) {
                case CSS:
                    libraries.forEach(library -> addCssMarkup(sb, library, resolver));
                    break;
                case JS:
                    libraries.forEach(library -> addJsMarkup(sb, library, resolver));
                    break;
                default:
                    libraries.forEach(library -> addCssMarkup(sb, library, resolver));
                    libraries.forEach(library -> addJsMarkup(sb, library, resolver));
                    break;
            }
        } catch (LoginException e) {
            log.error(e.getMessage(), e);
        }
        return sb.toString();
    }

    private Deque<ClientLibrary> libraries(final List<String> categories) {
        final Deque<ClientLibrary> librariesDeque = new ArrayDeque<>();

        final Deque<String> unresolvedCat = new ArrayDeque<>(new LinkedHashSet<>(categories));

        while (!unresolvedCat.isEmpty()) {
            final String category = unresolvedCat.poll();
            final ClientLibrary library = getClientLibrary(category);
            if (library != null) {
                librariesDeque.add(library);
            }
        }
        return librariesDeque;
    }

}
