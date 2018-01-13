package com.drfits.soc.test.core;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public final class TestMockModel {

    @Inject
    private String testLine;

    public TestMockModel() {
        // Default constructor to skip warnings.
    }

    public String getTestLine() {
        return this.testLine;
    }

    public void setTestLine(final String testLine) {
        this.testLine = testLine;
    }
}
