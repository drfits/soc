package com.drfits.soc.pub.login;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.time.LocalDate;

@Model(adaptables = SlingHttpServletRequest.class)
public final class LoginForm {

    private static final Logger LOG = LoggerFactory.getLogger(LoginForm.class);

    private static final String ACTION_PATH = "/j_security_check";

    private static final String RESOURCE_PARAMETER = "resource";

    private static final String ANONYMOUS_USER = "anonymous";

    @Self
    private SlingHttpServletRequest self;

    @Inject
    private SlingHttpServletResponse response;

    @PostConstruct
    protected void init() {
        LOG.debug("Init login form");
        if (!ANONYMOUS_USER.equals(this.self.getResourceResolver().getUserID())) {
            try {
                this.response.sendRedirect(this.getResourcePath());
            } catch (final IOException e) {
                LOG.error("Cannot redirect to: {}", this.getResourcePath());
            }
        }
    }

    public String getFormAction() {
        return this.self.getContextPath() + ACTION_PATH;
    }

    public String getResourcePath() {
        return String.valueOf(this.self.getRequestParameterMap().getValue(RESOURCE_PARAMETER));
    }

    public String getYear() {
        return String.valueOf(LocalDate.now().getYear());
    }

}
