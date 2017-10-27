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
package org.talend.core.ui.utils;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;

/**
 * Util class about plugin.
 */
public class PluginUtil {

    /**
     * Get the absolute installation path of plugin.
     * 
     * @param pluginId
     * @return the plugin path or an empty string when it's not found.
     */
    public static String getPluginInstallPath(String pluginId) {
        String pluginPath = "";
        try {
            boolean running = Platform.isRunning();
            if (!running) {
                return pluginPath;
            }
            URL url = FileLocator.resolve(Platform.getBundle(pluginId).getEntry("/")); //$NON-NLS-1$
            if (url == null) {
                return pluginPath;
            }
            pluginPath = url.getFile();
            String protoPath = url.getProtocol();
            if (pluginPath.startsWith("file:")) { //$NON-NLS-1$
                pluginPath = pluginPath.substring(pluginPath.indexOf("file:") + 6); //$NON-NLS-1$
            }
            if ("jar".equals(protoPath) && pluginPath.endsWith("!/")) { //$NON-NLS-1$ //$NON-NLS-2$
                pluginPath = pluginPath.substring(0, pluginPath.lastIndexOf("!/")); //$NON-NLS-1$
            }
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }
        return pluginPath;
    }

    /**
     * DOC ycbai Get the install path of product.
     * 
     * @return
     */
    public static String getProductInstallPath() {
        return (Platform.getInstallLocation().getURL()).getFile();
    }

    /**
     * @return true if esb editor opened
     */
    public static boolean isMediation() {
        return "org.talend.camel.designer.core.ui.CamelMultiPageTalendEditor".equals(getActiveEditorId()); //$NON-NLS-1$
    }

    /**
     * DOC yyan Comment method "isRouteTestEditor".
     * 
     * @return true if esb route test case editor opened
     */
    public static boolean isRouteTestEditor() {
        return "org.talend.camel.testcontainer.ui.editor.CamelTestContainerMultiPageEditor".equals(getActiveEditorId()); //$NON-NLS-1$
    }

    /**
     * DOC yyan Get active editor ID.
     * 
     * @return Active editor ID
     */
    public static String getActiveEditorId() {
        IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (null == workbenchWindow) {
            return null;
        }
        IWorkbenchPage workbenchPage = workbenchWindow.getActivePage();
        if (null == workbenchPage) {
            return null;
        }
        IEditorPart part = workbenchPage.getActiveEditor();
        if (part == null) {
            return null;
        }
        return part.getEditorSite().getId();
    }
}
