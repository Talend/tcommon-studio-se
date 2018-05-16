// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.maven;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;

public class DesignerMavenPlugin implements BundleActivator {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.talend.designer.maven"; //$NON-NLS-1$

    private static DesignerMavenPlugin plugin;

    private static BundleContext context;

    private ProjectPreferenceManager projectPreferenceManager;

    public BundleContext getContext() {
        return context;
    }

    public static DesignerMavenPlugin getPlugin() {
        return plugin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        this.plugin = this;
        DesignerMavenPlugin.context = bundleContext;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        DesignerMavenPlugin.context = null;
    }

    public ProjectPreferenceManager getProjectPreferenceManager() {
        if (projectPreferenceManager == null) {
            projectPreferenceManager = new ProjectPreferenceManager(PLUGIN_ID);
        }
        return projectPreferenceManager;
    }
}
