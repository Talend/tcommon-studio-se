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
package org.talend.commons.ui.utils;

import org.eclipse.ui.PlatformUI;

public class InLineHelpUtil {

    private static final String BASE_HELP_ID_PREFIX = "org.talend.help";

    private static final String EXTERNAL_HELP_ID_PREFIX = "org.talend.help.external";

    public static void displayHelp(String helpId) {
        if (helpId.startsWith(BASE_HELP_ID_PREFIX)) {
            PlatformUI.getWorkbench().getHelpSystem()
                    .displayHelp(EXTERNAL_HELP_ID_PREFIX + helpId.substring(BASE_HELP_ID_PREFIX.length()));
            return;
        }
        PlatformUI.getWorkbench().getHelpSystem().displayHelp(helpId);
    }
}
