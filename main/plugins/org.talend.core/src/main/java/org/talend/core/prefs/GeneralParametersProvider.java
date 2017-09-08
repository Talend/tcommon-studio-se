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
package org.talend.core.prefs;

import java.util.Arrays;
import java.util.ResourceBundle;

import org.talend.commons.i18n.MessagesCore;

/**
 * Use to retrieve general application parameters.<br/>
 * 
 * $Id: Messages.java 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class GeneralParametersProvider extends MessagesCore {

    private static final String BUNDLE_NAME = "parameters"; //$NON-NLS-1$

    private static final String PLUGIN_ID = "org.talend.core"; //$NON-NLS-1$

    private static ResourceBundle resourceBundle;

    private static ResourceBundle getBundle() {
        if (resourceBundle == null) {
            try {
                resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
            } catch (Exception e) {
                // Nothing to do (return null)
            }
        }
        return resourceBundle;
    }

    /**
     * Returns the value corresponding to the specified key.
     */
    public static String getString(GeneralParameters key) {
        return getString(key.getParamName(), PLUGIN_ID, getBundle());
    }

    /**
     * Returns a sorted string array containing values corresponding to the specified key.
     */
    public static String[] getStrings(GeneralParameters key) {
        String value = getString(key);
        String[] toReturn = value.split(","); //$NON-NLS-1$
        Arrays.sort(toReturn);
        return toReturn;
    }

    /**
     * DOC smallet GeneralParametersProvider class global comment. Detailled comment <br/>
     * 
     * $Id$
     */
    public enum GeneralParameters {
        AUTHORIZED_LANGUAGE("param.authorizedlanguage"), //$NON-NLS-1$
        DEFAULT_PERL_INTERPRETER_WIN32("param.defaultPerlInterpreterPath.win32"), //$NON-NLS-1$
        DEFAULT_PERL_INTERPRETER_LINUX("param.defaultPerlInterpreterPath.linux"), //$NON-NLS-1$
        DEFAULT_PERL_INTERPRETER_EMBEDDED_SUFFIX_WIN32("param.defaultPerlInterpreterEmbeddedSuffix.win32"), //$NON-NLS-1$
        DEFAULT_JAVA_INTERPRETER_SUFFIX_WIN32("param.defaultJavaInterpreterSuffix.win32"), //$NON-NLS-1$
        DEFAULT_JAVA_INTERPRETER_SUFFIX_LINUX("param.defaultJavaInterpreterSuffix.linux"), //$NON-NLS-1$
        PROJECTS_EXCLUDED_FROM_EXPORT("param.projectsExcludedFromExport"); //$NON-NLS-1$

        private String paramName;

        GeneralParameters(String paramName) {
            this.paramName = paramName;
        }

        public String getParamName() {
            return this.paramName;
        }
    }
}
