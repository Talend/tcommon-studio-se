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
package org.talend.designer.maven.template;

import java.util.Map;

/**
 * DOC ggu class global comment. Detailled comment
 */
public enum ETalendMavenVariables {
    ProductVersion,
    JavaVersion,

    ProjectGroupId,
    ProjectArtifactId,
    ProjectVersion,

    ProjectName,
    ProjectId,

    CodesGroupId,
    CodesArtifactId,
    CodesVersion,
    CodesName,
    CodesPackage,

    JobGroupId,
    JobArtifactId,
    JobVersion,

    JobletGroupId,
    JobletArtifactId,
    JobletVersion,
    JobletId,
    JobletName,

    JobId,
    JobName,
    JobType,
    JobFinalName,
    JobPath,
    JobDateFormat,
    JobContext,
    JobStat,
    JobApplyContextToChildren,
    Framework,
    TalendJobVersion,
    RoutineGroupId,
    PigudfGroupId
    //
    ;

    public String getName() {
        return name();
    }

    public String getExpression() {
        return '@' + getName() + '@';
    }

    public static String replaceVariables(String originalContent, Map<ETalendMavenVariables, String> variablesValuesMap) {
        return replaceVariables(originalContent, variablesValuesMap, true);
    }

    public static String replaceVariables(String originalContent, Map<ETalendMavenVariables, String> variablesValuesMap,
            boolean cleanup) {
        if (originalContent == null || originalContent.trim().length() == 0) {
            return originalContent;
        }
        String content = originalContent;
        if (variablesValuesMap != null) {
            for (ETalendMavenVariables var : variablesValuesMap.keySet()) {
                // won't do clean up here, must be after replaced
                content = replaceVariable(content, var, variablesValuesMap.get(var), false);
            }
        }
        if (cleanup) {
            content = cleanupVariable(content);
        }
        return content;
    }

    public static String replaceVariable(String originalContent, ETalendMavenVariables var, String value, boolean cleanup) {
        if (originalContent == null || originalContent.trim().length() == 0) {
            return originalContent;
        }

        String content = originalContent;
        if (value == null) {
            value = ""; //$NON-NLS-1$
        }
        if (var != null) {
            content = content.replace(var.getExpression(), value);
        }
        if (cleanup) {
            content = cleanupVariable(content);
        }
        return content;
    }

    /**
     * 
     * Will clean up all variables "@XXX@" to replace to empty.
     */
    public static String cleanupVariable(String originalContent) {
        if (originalContent == null || originalContent.length() == 0 //
                || originalContent.indexOf('@') == -1) {// no variable
            return originalContent;
        }
        String content = originalContent;

        content = content.replaceAll("@(.*?)@", ""); //$NON-NLS-1$ //$NON-NLS-2$

        return content;
    }
}
