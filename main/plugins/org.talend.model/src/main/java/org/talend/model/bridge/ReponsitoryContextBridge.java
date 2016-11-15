// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.model.bridge;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.User;

public final class ReponsitoryContextBridge {

    private static String PROJECT_DEFAULT_NAME = "TOP_DEFAULT_PRJ";

    private static Project project;

    private static User user;

    private ReponsitoryContextBridge() {

    }

    public static String getProjectName() {
        return isDefautProject() ? PROJECT_DEFAULT_NAME : project.getTechnicalLabel();
    }

    // ADD msjian 2011-8-5 TDQ-3165: get the Project Description
    public static String getProjectDescription() {
        return (isDefautProject() || (project.getDescription() == null) || "".equals(project.getDescription().trim())) ? "EMPTY (TDQ)" : project.getDescription(); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static String getAuthor() {
        // MOD mzhao bug 12646, 2010-04-21, Handle NPE.
        String author = "";
        // MOD qiongli bug 13824,2010-6-30,change the order of "if...else.. "
        if (user != null) {
            author = user.getLogin();
        } else if (project != null && project.getAuthor() != null) {
            author = project.getAuthor().getLogin();
        }
        return isDefautProject() ? "" : author;
        // ~
    }

    public static IProject getRootProject() {
        return ResourcesPlugin.getWorkspace().getRoot().getProject(getProjectName());
    }

    /**
     * 
     * find the special project which name is projectName
     * 
     * @param projectName the name of project which you finding
     * @return
     */
    public static IProject findProject(String projectName) {
        return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
    }

    public static void initialized(Project aProject, User aUser) {
        project = aProject;
        user = aUser;
    }

    public static Project getProject() {
        return project;
    }

    public static void setProject(Project project) {
        ReponsitoryContextBridge.project = project;
    }

    public static User getUser() {
        return user;
    }

    public static boolean isDefautProject() {
        return project == null || project.getTechnicalLabel().equals(PROJECT_DEFAULT_NAME);
    }

    public static void setDefaultProjectName(String projectName) {
        PROJECT_DEFAULT_NAME = projectName;
    }

}
