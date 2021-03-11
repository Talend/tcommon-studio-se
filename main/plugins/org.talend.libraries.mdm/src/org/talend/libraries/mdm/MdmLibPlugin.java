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
package org.talend.libraries.mdm;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class MdmLibPlugin extends Plugin {

    public static final String ID = "org.talend.libraries.mdm"; //$NON-NLS-1$

    // The shared instance.
    private static MdmLibPlugin plugin;

    /**
     * The constructor.
     */
    public MdmLibPlugin() {
        plugin = this;
    }

    /**
     * This method is called upon plug-in activation
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        String jdkversion = System.getProperty("java.version"); //$NON-NLS-1$
        if (jdkversion.startsWith("11")) { //$NON-NLS-1$
            System.setProperty("javax.xml.ws.spi.Provider", "org.apache.cxf.jaxws.spi.ProviderImpl"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /**
     * This method is called when the plug-in is stopped
     */
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
    }

    /**
     * Returns the shared instance.
     */
    public static MdmLibPlugin getDefault() {
        return plugin;
    }

}
