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
package org.talend.repository.localprovider.model;

import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.User;
import org.talend.core.repository.model.FolderHelper;

/**
 * DOC matthieu class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class LocalFolderHelper extends FolderHelper {

    protected LocalFolderHelper(Project project, User connectedUser) {
        super(project, connectedUser);
    }

    public static FolderHelper createInstance(Project project, User connectedUser) {
        return new LocalFolderHelper(project, connectedUser);
    }

    protected void removeFromResource(FolderItem folder) {
        // do nothing
    }

    protected void doCreateFolder(FolderItem folderItem) {
        // do nothing
    }

    public void doCreateItemState(FolderItem folder) {
        // do nothing
    }
}
