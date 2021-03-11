// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.migrationtool;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.talend.commons.utils.VersionUtils;
import org.talend.migration.IMigrationTask;
import org.talend.migration.IProjectMigrationTask;
import org.talend.migration.IWorkspaceMigrationTask;
import org.talend.migrationtool.model.GetTasksHelper;
import org.talend.utils.ProductVersion;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
@SuppressWarnings("nls")
public class MigrationTaskValidatorTest {

    private static final List<String> ignoreList = Arrays.asList(
            "org.talend.designer.core.ui.preferences.migration.ChangeMysqlVersionForProjectSetting",
            "org.talend.repository.model.migration.AddDieOnErrorOnSqoopComponents",
            "org.talend.repository.model.migration.ChangeMysqlJarReference4MysqlComponents",
            "org.talend.repository.model.migration.UpdateJobSettingsForMysqlMigrationTask",
            "org.talend.repository.model.migration.FixWrongDbTypesMigrationTask");

    @Test
    public void testValidateMigrationTaskVersions() {
        List<String> problemList = new ArrayList<>();
        List<IMigrationTask> migrationTasks = new LinkedList<>();

        List<IProjectMigrationTask> beforeLogonProjectTasks = GetTasksHelper.getProjectTasks(true);
        List<IProjectMigrationTask> afterLogonProjectTasks = GetTasksHelper.getProjectTasks(false);
        List<IWorkspaceMigrationTask> workspaceTasks = GetTasksHelper.getWorkspaceTasks();

        if (beforeLogonProjectTasks != null) {
            migrationTasks.addAll(beforeLogonProjectTasks);
        }
        if (afterLogonProjectTasks != null) {
            migrationTasks.addAll(afterLogonProjectTasks);
        }
        if (workspaceTasks != null) {
            migrationTasks.addAll(workspaceTasks);
        }

        if (migrationTasks != null) {
            final ProductVersion studioVersion = ProductVersion.fromString(VersionUtils.getTalendVersion());

            for (IMigrationTask migrationTask : migrationTasks) {
                String taskId = migrationTask.getId();
                if (ignoreList.contains(taskId)) {
                    continue;
                }
                String breaks = migrationTask.getBreaks();
                String version = migrationTask.getVersion();

                // 1. check [breaks]
                ProductVersion breaksVersion = null;
                if (StringUtils.isNotBlank(breaks)) {
                    breaksVersion = ProductVersion.fromString(breaks);
                    if (breaksVersion == null) {
                        problemList.add(taskId + ": [breaks](" + breaks + ") is not a valid version string!"); //$NON-NLS-1$//$NON-NLS-2$
                    } else {
                        if (0 < breaksVersion.compareTo(studioVersion)) {
                            // if you really need to specify it like this, please add it to the whiteList
                            problemList.add(taskId + " : [breaks] should be lower than product version!"); //$NON-NLS-1$
                        }
                    }
                }

                // 2. check [version]
                ProductVersion addVersion = null;
                if (StringUtils.isBlank(version)) {
                    problemList.add(taskId + " : [version] is not specified!"); //$NON-NLS-1$
                } else {
                    addVersion = ProductVersion.fromString(version);
                    if (addVersion == null) {
                        problemList.add(taskId + ": [version](" + version + ") is not a valid version string!"); //$NON-NLS-1$//$NON-NLS-2$
                    } else {
                        if (0 < addVersion.compareTo(studioVersion)) {
                            problemList.add(taskId + " : [version] should be lower than product version!"); //$NON-NLS-1$
                        }
                    }
                }

                // 3. check whether [breaks] is higher than [version]
                if (breaksVersion != null && addVersion != null) {
                    if (0 < breaksVersion.compareTo(addVersion)) {
                        // if you really need to specify it like this, please add it to the whiteList
                        problemList.add(taskId + " : [breaks] should be lower than [version]!"); //$NON-NLS-1$
                    }
                }
            }
        }

        if (!problemList.isEmpty()) {
            Collections.sort(problemList);
            StringBuffer problems = new StringBuffer();
            problems.append("MigrationTask configuration problems:").append("\n");
            for (String problem : problemList) {
                problems.append(problem).append("\n");
            }
            fail(problems.toString());
        }
    }

}
