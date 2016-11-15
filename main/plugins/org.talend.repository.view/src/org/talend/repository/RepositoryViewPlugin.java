// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.talend.repository.navigator.DescriptorAdapterFactory;
import org.talend.repository.navigator.TalendRepositoryRoot;
import org.talend.repository.viewer.content.ContentAdapterFactory;

public class RepositoryViewPlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.talend.repository.view"; //$NON-NLS-1$

    // The shared instance
    private static RepositoryViewPlugin plugin;

    public RepositoryViewPlugin() {
        plugin = this;
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        registerAdapters();
    }

    private void registerAdapters() {
        Platform.getAdapterManager().registerAdapters(new DescriptorAdapterFactory(), TalendRepositoryRoot.class);
        // TDI-20850
        Platform.getAdapterManager().registerAdapters(new ContentAdapterFactory(), TalendRepositoryRoot.class);
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    public static RepositoryViewPlugin getDefault() {
        return plugin;
    }

}
