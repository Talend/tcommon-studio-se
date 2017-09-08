// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.repository;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.talend.core.repository.model.ChangeProcessor;
import org.talend.repository.IRepositoryChangedListener;
import org.talend.repository.IRepositoryElementDelta;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: RepositoryPlugin.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public class CoreRepositoryPlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.talend.core.repository"; //$NON-NLS-1$

    // The shared instance
    private static CoreRepositoryPlugin plugin;

    private boolean rcpMode = false;

    private ChangeProcessor changeProcessor = new ChangeProcessor();;

    public CoreRepositoryPlugin() {
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    public static CoreRepositoryPlugin getDefault() {
        return plugin;
    }

    /**
     * 
     * ggu Comment method "isRCPMode".
     * 
     * @return
     */
    public boolean isRCPMode() {
        return rcpMode;
    }

    public void setRCPMode() {
        rcpMode = true;
    }

    /**
     * 
     * ggu Comment method "registerRepositoryChangedListener".
     * 
     * @param listener
     */
    public void registerRepositoryChangedListener(IRepositoryChangedListener listener) {
        changeProcessor.addRepositoryChangedListener(listener);
    }

    public void registerRepositoryChangedListenerAsFirst(IRepositoryChangedListener listener) {
        changeProcessor.registerRepositoryChangedListenerAsFirst(listener);
    }

    public void removeRepositoryChangedListener(IRepositoryChangedListener listener) {
        changeProcessor.removeRepositoryChangedListener(listener);
    }

    public void repositoryChanged(IRepositoryElementDelta delta) {
        changeProcessor.repositoryChanged(delta);
    }

}
