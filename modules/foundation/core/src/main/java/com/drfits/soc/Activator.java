package com.drfits.soc;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Setup SOC application:
 * <ul>
 * <li>delete default content</li>
 * </ul>
 * Created by Evgeniy Fitsner <drfits@drfits.com> on 11/16/16.
 */
public class Activator implements BundleActivator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Activator.class);

    @Override
    public void start(BundleContext context) throws Exception {
    }

    @Override
    public void stop(BundleContext context) throws Exception {

    }
}
