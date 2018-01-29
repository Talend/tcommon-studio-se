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

import java.util.Iterator;
import java.util.List;

import org.talend.core.model.properties.MigrationStatus;
import org.talend.core.model.properties.MigrationTask;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.migration.IMigrationTask;
import org.talend.migration.IMigrationTask.ExecutionResult;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class MigrationUtil {

    public static final String ADAPT_NEW_MIGRATION_TASK_SYSTEM_ID = "org.talend.repository.model.migration.UpdateExistentMigrationTasksToAdaptNewMigrationSystemMigrationTask"; //$NON-NLS-1$

    public static final String DEFAULT_VERSION = "5.1.2"; //$NON-NLS-1$

    public static final String DEFAULT_BREAKS = "5.1.1"; //$NON-NLS-1$

    public static final MigrationStatus DEFAULT_STATUS = MigrationStatus.OK_LITERAL;

    public static MigrationTask findMigrationTask(List<? extends MigrationTask> tasks, IMigrationTask task) {
        if (tasks == null || task == null) {
            return null;
        }
        return findMigrationTaskById(tasks, task.getId());
    }

    public static MigrationTask findMigrationTaskById(List<? extends MigrationTask> tasks, String taskId) {
        if (tasks == null || taskId == null) {
            return null;
        }
        for (MigrationTask task : tasks) {
            if (task != null && taskId.equals(task.getId())) {
                return task;
            }
        }

        return null;
    }

    public static void removeMigrationTaskById(List<? extends MigrationTask> tasks, String taskId) {
        if (tasks == null || taskId == null) {
            return;
        }
        Iterator<? extends MigrationTask> taskIter = tasks.iterator();
        while (taskIter.hasNext()) {
            MigrationTask task = taskIter.next();
            if (task != null && taskId.equals(task.getId())) {
                taskIter.remove();
            }
        }
    }

    public static void removeMigrationTaskByIds(List<? extends MigrationTask> tasks, List<String> taskIds) {
        if (taskIds == null) {
            return;
        }
        for (String taskId : taskIds) {
            removeMigrationTaskById(tasks, taskId);
        }
    }

    public static void removeMigrationTaskByMigrationTasks(List<? extends MigrationTask> tasks,
            List<? extends MigrationTask> removedTasks) {
        if (removedTasks == null) {
            return;
        }
        for (MigrationTask migrationTask : removedTasks) {
            if (migrationTask == null) {
                continue;
            }
            removeMigrationTaskById(tasks, migrationTask.getId());
        }
    }

    public static boolean containsTask(List<? extends MigrationTask> tasks, String taskId) {
        return findMigrationTaskById(tasks, taskId) != null;
    }

    public static boolean containsTask(List<? extends MigrationTask> tasks, IMigrationTask task) {
        if (tasks == null || task == null) {
            return false;
        }
        return containsTask(tasks, task.getId());
    }

    public static MigrationTask convertMigrationTask(IMigrationTask task) {
        MigrationStatus migrationStatus = DEFAULT_STATUS;
        ExecutionResult status = task.getStatus();
        if (status != null) {
            switch (status) {
            case SUCCESS_WITH_ALERT:
            case SUCCESS_NO_ALERT:
                migrationStatus = MigrationStatus.OK_LITERAL;
                break;
            case NOTHING_TO_DO:
            case SKIPPED:
                migrationStatus = MigrationStatus.NOIMPACT_LITERAL;
                break;
            case FAILURE:
                migrationStatus = MigrationStatus.FAILED_LITERAL;
                break;
            default:
                migrationStatus = DEFAULT_STATUS;
                break;
            }
        }

        return createMigrationTask(task.getId(), task.getVersion(), task.getBreaks(), migrationStatus);
    }

    public static MigrationTask createMigrationTask(String id, String version, String breaks, MigrationStatus status) {
        MigrationTask task = PropertiesFactory.eINSTANCE.createMigrationTask();
        task.setId(id);
        task.setVersion(version);
        task.setBreaks(breaks);
        task.setStatus(status);

        return task;
    }

    public static MigrationTask createDefaultMigrationTask(String id) {
        return createMigrationTask(id, DEFAULT_VERSION, DEFAULT_BREAKS, DEFAULT_STATUS);
    }

}
