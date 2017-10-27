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
package org.talend.migration;

import org.talend.migration.IMigrationTask.ExecutionResult;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public abstract class AbstractMigrationTask {

    private String id;

    private String name;

    private String description = ""; //$NON-NLS-1$

    private String version;

    private String breaks;

    private ExecutionResult status;

    /**
     * DOC smallet AbstractMigrationTask constructor comment.
     */
    public AbstractMigrationTask() {
        super();
    }

    /**
     * DOC smallet AbstractMigrationTask constructor comment.
     * 
     * @param id
     * @param name
     */
    public AbstractMigrationTask(String id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        if (description != null) {
            this.description = description;
        }
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBreaks() {
        return this.breaks;
    }

    public void setBreaks(String breaks) {
        this.breaks = breaks;
    }

    public void setStatus(ExecutionResult status) {
        this.status = status;
    }

    public ExecutionResult getStatus() {
        return status;
    }

    public boolean isDeprecated() {
        return false;
    }
}
