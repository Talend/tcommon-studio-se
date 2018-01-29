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
package org.talend.core.runtime.util;

import java.util.Locale;

import org.talend.components.api.component.ComponentDefinition;
import org.talend.core.runtime.i18n.Messages;

/**
 * created by nrousseau on Jun 21, 2016 Detailled comment
 *
 */
public class ComponentReturnVariableUtils {

    public static String getStudioNameFromVariable(String variableName) {
        switch (variableName) {
        case ComponentDefinition.RETURN_ERROR_MESSAGE:
            return "ERROR_MESSAGE"; //$NON-NLS-1$
        case ComponentDefinition.RETURN_REJECT_RECORD_COUNT:
            return "NB_REJECT"; //$NON-NLS-1$
        case ComponentDefinition.RETURN_SUCCESS_RECORD_COUNT:
            return "NB_SUCCESS"; //$NON-NLS-1$
        case ComponentDefinition.RETURN_TOTAL_RECORD_COUNT:
            return "NB_LINE"; //$NON-NLS-1$
        }
        String inputString = variableName;

        String outputString = ""; //$NON-NLS-1$

        for (int i = 0; i < inputString.length(); i++) {
            Character c = inputString.charAt(i);
            outputString += Character.isUpperCase(c) && i> 0 ? "_"+ c : c;  //$NON-NLS-1$
        }
        outputString = outputString.toUpperCase(Locale.ENGLISH);
        return outputString;
    }

    /**
     * DOC nrousseau Comment method "getTranslationForVariable".
     * @param name
     * @param displayName
     * @return
     */
    public static String getTranslationForVariable(String variableName, String displayName) {
        switch (variableName) {
        case ComponentDefinition.RETURN_ERROR_MESSAGE:
            return Messages.getString("ComponentReturnVariableUtils.ErrorMessage"); //$NON-NLS-1$
        case ComponentDefinition.RETURN_REJECT_RECORD_COUNT:
            return Messages.getString("ComponentReturnVariableUtils.NbReject"); //$NON-NLS-1$
        case ComponentDefinition.RETURN_SUCCESS_RECORD_COUNT:
            return Messages.getString("ComponentReturnVariableUtils.NbSuccess"); //$NON-NLS-1$
        case ComponentDefinition.RETURN_TOTAL_RECORD_COUNT:
            return Messages.getString("ComponentReturnVariableUtils.NbLine"); //$NON-NLS-1$
        }
        return displayName;
    }
}
