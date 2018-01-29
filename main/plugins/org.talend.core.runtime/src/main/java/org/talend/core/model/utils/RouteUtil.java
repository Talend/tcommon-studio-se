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
package org.talend.core.model.utils;

import java.io.FileWriter;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.Property;
import org.talend.repository.ProjectManager;

public final class RouteUtil {

    private static final String URI_PREFIX = "platform:/resource/";
    private static final String ROUTELET_PATH = "/routelets/";
    private static final int URI_PREFIX_LENGTH = URI_PREFIX.length();

    public static String resolveClassName(IProcess2 process) {
        return resolvePackageName(process) + "." + process.getName(); //$NON-NLS-1$
    }

    public static String resolvePackageName(IProcess2 process) {
        Item processItem = process.getProperty().getItem();
        return getProjectFolderName(processItem) + '.' + getRouteFolderName(processItem);
    }

    private static String getRouteFolderName(Item processItem) {
        Property itemProperty = processItem.getProperty();
        String jobName = escapeFileName(itemProperty.getLabel()).toLowerCase();
        String version = itemProperty.getVersion();
        return version == null ? jobName : jobName + '_' + version.replace('.', '_'); 
    }

    private static String getProjectFolderName(Item processItem) {
        String result = getRouteletProjectTechnicalLabel(processItem);
        if (result == null) {
            result = ProjectManager.getInstance().getProject(processItem).getTechnicalLabel();
        }
        return result.toLowerCase();
    }

    private static String getRouteletProjectTechnicalLabel(Item processItem) {
        String uri = getProxyURIString(processItem);
        if (uri != null && uri.startsWith(URI_PREFIX)) {
            String pathString = uri.substring(URI_PREFIX_LENGTH);
            if (!pathString.contains(ROUTELET_PATH)) {
                return null;
            }
            String projectLabel = pathString.substring(0, pathString.indexOf("/"));
            ProjectManager projectManager = ProjectManager.getInstance();
            Project currentProject = projectManager.getCurrentProject();
            if (isNameOfProject(projectLabel, currentProject)) {
                return currentProject.getTechnicalLabel();
            }
            for (Project project : projectManager.getAllReferencedProjects()) {
                if (isNameOfProject(projectLabel, project)) {
                    return project.getTechnicalLabel();
                }
            }
        }
        return null;
    }

    private static String getProxyURIString(Item processItem) {
        ItemState state = processItem.getState();
        if (state instanceof EObjectImpl) {
            URI proxyURI = ((EObjectImpl) state).eProxyURI();
            return proxyURI == null ? null : proxyURI.toString();
        }
        return null;
    }

    private static String escapeFileName(final String fileName) {
        return fileName != null ? fileName.replace(' ', '_') : ""; //$NON-NLS-1$
    }

    private static boolean isNameOfProject(String name, Project project) {
        if (name == null) {
            return false;
        }
        if (name.equals(project.getTechnicalLabel())) {
            return true;
        }
        if (name.equalsIgnoreCase(project.getLabel())) {
            return true;
        }
        return false;
    }

    private RouteUtil() {
        super();
    }
}
