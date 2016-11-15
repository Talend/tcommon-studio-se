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
package org.talend.core.model.migration;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.core.IService;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.MigrationTask;

public interface IMigrationToolService extends IService {

    public void executeWorspaceTasks();

    public void initNewProjectTasks(Project project);

    public boolean needExecutemigration();

    public void executeMigration(boolean pluginModel);

    public void executeMigrationTasksForLogon(Project project, boolean beforeLogon, IProgressMonitor monitorWrap);

    public void executeMigrationTasksForImport(Project project, Item item, List<MigrationTask> migrationTasksToApply,
            final IProgressMonitor monitor) throws Exception;

    public boolean checkMigrationTasks(org.talend.core.model.properties.Project project);

    public void updateMigrationSystem(org.talend.core.model.properties.Project project, boolean persistence);

    public String getTaskId();

}
