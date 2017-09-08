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
package org.talend.repository.items.importexport.ui.i18n;

import java.util.ResourceBundle;

import org.talend.commons.i18n.MessagesCore;

/**
 * Default implementation of MessageCore from org.talend.commons plug-in.<br/>
 * 
 * Developpers can copy this class in their plug-in and change :
 * <ul>
 * <li>the BUNDLE_NAME constant</li>
 * </ul>
 * 
 * $Id: Messages.java 96654 2013-01-10 12:40:34Z mhirt $
 * 
 */
public class Messages extends MessagesCore {

    private static final String PLUGIN_ID = "org.talend.repository.items.importexport.ui"; //$NON-NLS-1$

    private static final String BUNDLE_NAME = "org.talend.repository.items.importexport.ui.messages"; //$NON-NLS-1$

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);

    /**
     * Returns the i18n formatted message for <i>key</i> and <i>args</i> in the specified bundle.
     * 
     * @param key - the key for the desired string
     * @param args - arg to include in the string
     * @return the string for the given key in the given resource bundle
     * @see MessagesCore#getString(String, ResourceBundle, Object[])
     */
    public static String getString(String key, Object... args) {
        return getString(key, PLUGIN_ID, resourceBundle, args);
    }
}
