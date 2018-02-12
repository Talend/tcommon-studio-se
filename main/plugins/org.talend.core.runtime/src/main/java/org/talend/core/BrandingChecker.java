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
package org.talend.core;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.swt.widgets.Display;
import org.osgi.service.prefs.BackingStoreException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.CommonUIPlugin;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.ui.branding.IBrandingService;

/**
 * DOC talend class global comment. Detailled comment
 */
public class BrandingChecker {

    private static boolean initialized = false;

    private static boolean isBrandingChanged;

    private static final String LAST_STARTED_PRODUCT = "last_started_product"; //$NON-NLS-1$

    public static boolean isBrandingChanged() {
        if (!initialized) {
            if (CommonUIPlugin.isFullyHeadless()) {
                isBrandingChanged = false;
            } else {
                IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                        IBrandingService.class);
                final String fullProductName = brandingService.getFullProductName();
                IEclipsePreferences node = new InstanceScope().getNode(CoreRuntimePlugin.PLUGIN_ID);
                String oldBrandingName = node.get(LAST_STARTED_PRODUCT, "");
                if (oldBrandingName == null || oldBrandingName.equals("") || !oldBrandingName.equals(fullProductName)) { //$NON-NLS-1$
                    isBrandingChanged = true;
                    node.put(LAST_STARTED_PRODUCT, fullProductName);
                    try {
                        node.flush();
                    } catch (BackingStoreException e) {
                        ExceptionHandler.process(e);
                    }
                }
            }
            initialized = true;
        }
        return isBrandingChanged;
    }
}
