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
package org.talend.themes.core.elements.utils;

import org.talend.themes.core.elements.constants.TalendThemeConstants;

/**
 * created by cmeng on Jun 30, 2015 Detailled comment
 *
 */
public class TalendThemeUtils {

    public static boolean isThemeFromTalend(String themeId) {
        if (themeId == null) {
            return false;
        }
        return themeId.startsWith(TalendThemeConstants.TALEND_THEME_PREFIX);
    }
}
