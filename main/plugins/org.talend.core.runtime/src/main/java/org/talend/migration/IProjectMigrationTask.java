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

import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Item;

/**
 * Define an atomic migration task to run on a project to assure comptibility trough Talend versions. See
 * org.talend.core.migrationTask extension point.<br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public interface IProjectMigrationTask extends IMigrationTask {

    public String getDescription();

    public void setDescription(String desc);

    public boolean isApplicableOnItems();

    public ExecutionResult execute(Project project);

    public ExecutionResult execute(Project project, boolean doSave);

    public ExecutionResult execute(Project project, Item item);

}
