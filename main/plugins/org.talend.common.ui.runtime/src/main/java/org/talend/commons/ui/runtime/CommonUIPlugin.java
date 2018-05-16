// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.commons.ui.runtime;

import java.util.Hashtable;

import org.eclipse.swt.widgets.Display;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionService;
import org.talend.commons.ui.runtime.exception.ExceptionServiceImpl;

public class CommonUIPlugin implements BundleActivator {

    private static Boolean fullyHeadless = null;

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start(BundleContext context) throws Exception {
        Hashtable props = new Hashtable();
        context.registerService(ExceptionService.class.getName(), new ExceptionServiceImpl(), props);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(BundleContext context) throws Exception {
    }

    /**
     * To check if there is GUI in the build of not, not especially means we're in commandline mode. (but for
     * commandline, this one will return true)<br>
     * <br>
     * To rename later to isHeadless later once the other function is renamed as well.
     * 
     * @return
     */
    public static boolean isFullyHeadless() {
        if (fullyHeadless != null) {
            return fullyHeadless;
        }
        fullyHeadless = CommonsPlugin.isHeadless();
        if (!CommonsPlugin.isHeadless()) {
            try {
                Display.getDefault();
            } catch (Throwable e) {
                fullyHeadless = true;
            }
        }

        return fullyHeadless;
    }

}
