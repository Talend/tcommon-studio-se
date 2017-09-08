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
package org.talend.repository.documentation;

/**
 * ftang class global comment. Detailled comment.
 */
public enum ERepositoryActionName {

    RESTORE("restore"), //$NON-NLS-1$
    MOVE("move"), //$NON-NLS-1$
    COPY("copy"), //$NON-NLS-1$
    IMPORT("import"), //$NON-NLS-1$

    CREATE("create"), //$NON-NLS-1$
    SAVE("save"), //$NON-NLS-1$
    PROPERTIES_CHANGE("properties change"), //$NON-NLS-1$

    FOLDER_CREATE("folder create"), //$NON-NLS-1$

    DELETE_TO_RECYCLE_BIN("delete to recycle bin"), //$NON-NLS-1$
    DELETE_FOREVER("delete forever"), //$NON-NLS-1$

    // these actions bellow are only for jobs and joblet actually, need to review.

    // for jobs only
    FOLDER_DELETE("folder delete"), //$NON-NLS-1$
    FOLDER_RENAME("folder rename"), //$NON-NLS-1$
    FOLDER_MOVE("folder move"), //$NON-NLS-1$

    // for joblet only
    JOBLET_FOLDER_DELETE("joblet.folder.delete"), //$NON-NLS-1$
    JOBLET_FOLDER_RENAME("joblet.folder.rename"), //$NON-NLS-1$
    JOBLET_FOLDER_MOVE("joblet.folder.move"), //$NON-NLS-1$

    PROJECT_PREFERENCES_RELOAD("reload project preferences") //$NON-NLS-1$
    ;

    private String name;

    /**
     * ftang ERepositoryActionName constructor comment.
     * 
     * @param name
     */
    private ERepositoryActionName(String name) {
        this.name = name;
    }

    /**
     * ftang Comment method "getName".
     * 
     * @return
     */
    public String getName() {
        return this.name;
    }
}
