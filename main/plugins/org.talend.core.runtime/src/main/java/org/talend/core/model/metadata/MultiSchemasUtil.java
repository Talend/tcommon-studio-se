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
package org.talend.core.model.metadata;

import java.util.Arrays;
import java.util.List;

import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.types.JavaType;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.PerlTypesManager;

/**
 * cLi class global comment. Detailled comment
 */
public final class MultiSchemasUtil {

    public static String getConnectionBaseName(String base) {
        final String space = "_"; //$NON-NLS-1$
        if (base == null | "".equals(base)) { //$NON-NLS-1$
            base = ""; //$NON-NLS-1$
        } else {
            base = base + space;
        }

        return "row" + space + base; //$NON-NLS-1$
    }

    public static String validateValue(String value) {
        if (value == null) {
            value = ""; //$NON-NLS-1$
        }
        return value;
    }

    public static int getTalendTypeIndex(String talendType) {
        if (talendType == null || "".equals(talendType)) { //$NON-NLS-1$
            return 0;
        } else {
            List<String> types = null;
            if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) {
                talendType = JavaTypesManager.getJavaTypeFromId(talendType).getLabel();
                types = Arrays.asList(JavaTypesManager.getJavaTypesLabels());
            } else { // perl
                types = Arrays.asList(PerlTypesManager.getPerlTypes());
            }
            return types.indexOf(talendType);
        }
    }

    public static String getAndCheckIntgerValue(Integer value) {
        if (value != null) {
            return value.toString();
        } else {
            return ""; //$NON-NLS-1$
        }
    }

    public static String getTalendTypeByIndex(Integer index) {
        if (index > -1) {
            if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) {
                final JavaType javaType = JavaTypesManager.getJavaTypes()[index];
                return javaType.getId();
            } else {
                final String perlType = PerlTypesManager.getPerlTypes()[index];
                return perlType; // perl
            }
        }
        return null;
    }

    public static String[] getTalendTypeLabel() {
        String[] typeLabels = null;
        if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) {
            typeLabels = JavaTypesManager.getJavaTypesLabels();
        } else {
            typeLabels = PerlTypesManager.getPerlTypes();
        }
        return typeLabels;
    }
}
