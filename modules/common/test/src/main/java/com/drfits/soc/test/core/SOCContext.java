package com.drfits.soc.test.core;

import org.apache.sling.testing.mock.osgi.context.ContextPlugins;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.context.SlingContextImpl;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public final class SOCContext extends SlingContextImpl implements TestRule {

    private final ContextPlugins plugins;

    private final TestRule delegate;

    /**
     * Initialize Sling context with default resource resolver type:
     * {@link ResourceResolverType#RESOURCERESOLVER_MOCK}.
     */
    public SOCContext() {
        this(new ContextPlugins(), null);
    }

    /**
     * Initialize Sling context with resource resolver type.
     *
     * @param contextPlugins       Context plugins
     * @param resourceResolverType Resource resolver type.
     */
    private SOCContext(final ContextPlugins contextPlugins, final ResourceResolverType resourceResolverType) {

        this.plugins = contextPlugins;

        // set resource resolver type in parent context
        this.setResourceResolverType(resourceResolverType);

        // wrap {@link ExternalResource} rule executes each test method once
        this.delegate = new ExternalResource() {
            @Override
            protected void before() {
                SOCContext.this.plugins.executeBeforeSetUpCallback(SOCContext.this);
                SOCContext.this.setUp();
                SOCContext.this.plugins.executeAfterSetUpCallback(SOCContext.this);
            }

            @Override
            protected void after() {
                SOCContext.this.plugins.executeBeforeTearDownCallback(SOCContext.this);
                SOCContext.this.tearDown();
                SOCContext.this.plugins.executeAfterTearDownCallback(SOCContext.this);
            }
        };
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        return this.delegate.apply(base, description);
    }

}
