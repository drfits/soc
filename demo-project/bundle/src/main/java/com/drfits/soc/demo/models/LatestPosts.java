package com.drfits.soc.demo.models;

import com.drfits.soc.foundation.api.Page;
import com.drfits.soc.foundation.api.PageManager;
import org.apache.jackrabbit.oak.query.SQL2Parser;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.jcr.query.Query;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public final class LatestPosts {

    private static final String CHILD_POST_QUERY = "SELECT * FROM [soc:PageContent] as p " +
            "WHERE ISDESCENDANTNODE(%s) " +
            "AND p.[sling:resourceType]='demo-project/pages/post' " +
            "ORDER BY p.[jcr:lastModified]";

    @Self
    private Resource self;

    @OSGiService
    private PageManager pageManager;

    private List<Resource> posts = new ArrayList<>();

    @PostConstruct
    protected void init() {
        final Page page = this.pageManager.getContainingPage(this.self);
        if (page != null) {
            final String query = String.format(CHILD_POST_QUERY, SQL2Parser.escapeStringLiteral(page.getPath()));
            final Iterator<Resource> resourceIt = this.self.getResourceResolver().findResources(query, Query.JCR_SQL2);
            while (resourceIt.hasNext()) {
                this.posts.add(resourceIt.next());
            }
        }
    }

    public List<Resource> getPosts() {
        return this.posts;
    }
}
