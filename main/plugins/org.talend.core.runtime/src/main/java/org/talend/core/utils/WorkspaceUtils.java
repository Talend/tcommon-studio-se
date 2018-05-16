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
package org.talend.core.utils;

import java.io.File;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.talend.model.bridge.ReponsitoryContextBridge;
import org.talend.repository.model.RepositoryConstants;

/**
 * created by msjian on 2014-1-2 Detailled comment
 * 
 */
public final class WorkspaceUtils {

    public static File ifolderToFile(IFolder ifolder) {
        IPath location = ifolder.getLocation() == null ? ReponsitoryContextBridge.getRootProject().getLocation()
                .append(ifolder.getFullPath()) : ifolder.getLocation();
        return location.toFile();
    }

    /**
     * check the file name whether it contains special characters.
     * 
     * @param fileName
     * @return boolean: OK means not contains
     */
    public static boolean checkNameIsOK(String fileName) {
        for (String str : RepositoryConstants.ITEM_FORBIDDEN_IN_LABEL) {
            if (org.apache.commons.lang.StringUtils.contains(fileName, str)) {
                return false;
            }
        }
        return true;
    }
}
