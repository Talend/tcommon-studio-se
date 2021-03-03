// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.updates.runtime;

import org.eclipse.equinox.internal.p2.core.helpers.ServiceHelper;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class UpdatesRuntimePlugin extends AbstractUIPlugin {

    public static final String BUNDLE_ID = Constants.PLUGIN_ID;

    private static UpdatesRuntimePlugin plugin;

    private IProvisioningAgent agent;

    @SuppressWarnings("restriction")
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        agent = ServiceHelper.getService(context, IProvisioningAgent.class);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    public static UpdatesRuntimePlugin getDefault() {
        return plugin;
    }

    public IProvisioningAgent getProvisioningAgent() {
        return agent;
    }

}
