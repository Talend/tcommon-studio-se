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
package org.talend.core.model.migration;

import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Item;
import org.talend.migration.AbstractMigrationTask;
import org.talend.migration.IProjectMigrationTask;

/**
 * DOC stephane class global comment. Detailled comment
 */
public abstract class AbstractProjectMigrationTask extends AbstractMigrationTask implements IProjectMigrationTask {

    public final ExecutionResult execute(Project project, Item item) {
        return ExecutionResult.NOTHING_TO_DO;
    }

    public ExecutionResult execute(Project project, boolean doSave) {
        return execute(project);
    }

    public final boolean isApplicableOnItems() {
        return false;
    }

    public boolean isDeprecated() {
        return false;
    }

}
