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
package org.talend.core.utils;

import java.util.regex.Pattern;

import org.talend.repository.model.RepositoryConstants;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public class ProjectUtils {

    public static final String[] JAVA_KEY_WORDS = {
            "abstract", "break", "case", "catch", "continue", "default", "do", "else", "extends", "final", "finally", "for", "if", "implements", "instanceof", "native", "new", "private", "protected", "public", "return", "static", "switch", "synchronized", "throw", "throws", "transient", "try", "volatile", "while", "assert", "enum", "strictfp", "package", "import", "boolean", "byte", "char", "class", "double", "float", "int", "interface", "long", "short", "void", "java", "org", "String", "etc", "com", "net", "fr", "sf", "routines", "javax", "false", "null", "super", "this", "true", "goto", "const" }; //$NON-NLS-1$ //$NON-NLS-2$ 

    public static boolean isNotValidProjectName(String projectName) {
        for (String keyword : JAVA_KEY_WORDS) {
            if (keyword.equalsIgnoreCase(projectName)) {
                return true;
            }
        }
        return !Pattern.matches(RepositoryConstants.PROJECT_PATTERN, projectName);
    }

    /**
     * for the tac side DOC hcyi Comment method "isValidProjectName".
     * 
     * @param projectLabel
     * @return
     */
    public static boolean isValidProjectName(String projectLabel) {
        return !isNotValidProjectName(projectLabel);
    }
}
