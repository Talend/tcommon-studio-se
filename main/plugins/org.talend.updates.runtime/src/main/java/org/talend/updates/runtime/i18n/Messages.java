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
package org.talend.updates.runtime.i18n;

import java.util.ResourceBundle;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.talend.commons.i18n.MessagesCore;
import org.talend.updates.runtime.Constants;

/**
 * created by sgandon on 5 mars 2013 Detailled comment
 * 
 */
public class Messages extends MessagesCore {

    private static final String BUNDLE_NAME = "messages"; //$NON-NLS-1$

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);

    /**
     * Returns the i18n formatted message for <i>key</i> in the class bundle.
     * 
     * @param key - the key for the desired string
     * @return the string for the given key in the class resource bundle
     * @see MessagesCore#getString(String, ResourceBundle)
     */
    public static String getString(String key) {
        return getString(key, Constants.PLUGIN_ID, resourceBundle);
    }

    /**
     * Returns the i18n formatted message for <i>key</i> and <i>args</i> in the specified bundle.
     * 
     * @param key - the key for the desired string
     * @param args - arg to include in the string
     * @return the string for the given key in the given resource bundle
     * @see MessagesCore#getString(String, ResourceBundle, Object[])
     */
    public static String getString(String key, Object... args) {
        return MessagesCore.getString(key, Constants.PLUGIN_ID, resourceBundle, args);
    }

    /**
     * DOC sgandon Comment method "getPlugiId".
     * 
     * @return
     */
    static public String getPlugiId() {
        Bundle bundle = FrameworkUtil.getBundle(Messages.class);
        return bundle.getSymbolicName();
    }

    static public Status createErrorStatus(Throwable t, String i18nKey, String... params) {
        return new Status(IStatus.ERROR, Messages.getPlugiId(), Messages.getString(i18nKey, (Object[]) params), t);
    }

    static public Status createErrorStatus(Throwable t) {
        return new Status(IStatus.ERROR, Messages.getPlugiId(), t.getMessage(), t);
    }

    static public Status createCancelStatus(String i18nKey, String... params) {
        return new Status(IStatus.CANCEL, Messages.getPlugiId(), Messages.getString(i18nKey, (Object[]) params));
    }

    static public Status createOkStatus(String i18nKey, String... params) {
        return new Status(IStatus.OK, Messages.getPlugiId(), Messages.getString(i18nKey, (Object[]) params));
    }

}
