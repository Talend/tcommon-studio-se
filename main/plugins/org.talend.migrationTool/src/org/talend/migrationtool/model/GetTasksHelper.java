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
package org.talend.migrationtool.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.workbench.extensions.ExtensionImplementationProvider;
import org.talend.commons.utils.workbench.extensions.ExtensionPointLimiterImpl;
import org.talend.commons.utils.workbench.extensions.IExtensionPointLimiter;
import org.talend.migration.IProjectMigrationTask;
import org.talend.migration.IWorkspaceMigrationTask;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class GetTasksHelper {

    private static GetTasksHelper instance = null;

    private Map<String, IProjectMigrationTask> migrationsInstances = null;

    public static GetTasksHelper getInstance() {
        if (instance == null) {
            instance = new GetTasksHelper();
            instance.migrationsInstances = new HashMap<String, IProjectMigrationTask>();
        }
        return instance;
    }

    /**
     * Dispose not called yet, since it could enhance the next import items done (mostly for commandline or even TUJs).
     * This could be called if needed, but migration tasks shouldn't take too much memory
     */
    public void dispose() {
        migrationsInstances = null;
    }

    /**
     * Recreate the instance will lose many times if import many items who need many migration tasks. So don't recreate
     * the instance if not needed (migrations task shouldn't hold much things in memory in all cases).
     */
    public IProjectMigrationTask getProjectTask(final String taskId) {
        if (!migrationsInstances.containsKey(taskId)) {
            IExtensionPointLimiter actionExtensionPoint = new ExtensionPointLimiterImpl(
                    "org.talend.core.migrationTask", "projecttask"); //$NON-NLS-1$ //$NON-NLS-2$

            ExtensionImplementationProvider<IProjectMigrationTask> provider = new ExtensionImplementationProvider<IProjectMigrationTask>(
                    actionExtensionPoint) {

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.talend.commons.utils.workbench.extensions.ExtensionImplementationProvider#createImplementation
                 * (org .eclipse.core.runtime.IExtension,
                 * org.talend.commons.utils.workbench.extensions.IExtensionPointLimiter)
                 */
                @Override
                protected IProjectMigrationTask createImplementation(IExtension extension,
                        IExtensionPointLimiter extensionPointLimiter, IConfigurationElement configurationElement) {
                    try {
                        if (configurationElement.getAttribute("id").equals(taskId)) { //$NON-NLS-1$
                            IProjectMigrationTask currentAction = (IProjectMigrationTask) configurationElement
                                    .createExecutableExtension("class"); //$NON-NLS-1$
                            currentAction.setId(configurationElement.getAttribute("id")); //$NON-NLS-1$
                            currentAction.setName(configurationElement.getAttribute("name")); //$NON-NLS-1$
                            currentAction.setDescription(configurationElement.getAttribute("description")); //$NON-NLS-1$
                            currentAction.setVersion(configurationElement.getAttribute("version"));
                            currentAction.setBreaks(configurationElement.getAttribute("breaks"));
                            return currentAction;
                        }
                    } catch (CoreException e) {
                        ExceptionHandler.process(e);
                    }
                    return null;
                }

            };

            List<IProjectMigrationTask> createInstances = provider.createInstances();
            if (createInstances.isEmpty()) {
                return null;
            } else {
                migrationsInstances.put(taskId, createInstances.get(0));
            }
        }
        return migrationsInstances.get(taskId);
    }

    public static List<IProjectMigrationTask> getProjectTasks(final boolean beforeLogon) {
        IExtensionPointLimiter actionExtensionPoint = new ExtensionPointLimiterImpl(
                "org.talend.core.migrationTask", "projecttask"); //$NON-NLS-1$ //$NON-NLS-2$

        ExtensionImplementationProvider<IProjectMigrationTask> provider = new ExtensionImplementationProvider<IProjectMigrationTask>(
                actionExtensionPoint) {

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.talend.commons.utils.workbench.extensions.ExtensionImplementationProvider#createImplementation(org
             * .eclipse.core.runtime.IExtension, org.talend.commons.utils.workbench.extensions.IExtensionPointLimiter)
             */
            @Override
            protected IProjectMigrationTask createImplementation(IExtension extension,
                    IExtensionPointLimiter extensionPointLimiter, IConfigurationElement configurationElement) {
                try {
                    if (new Boolean(configurationElement.getAttribute("beforeLogon")) == beforeLogon) { //$NON-NLS-1$
                        IProjectMigrationTask currentAction = (IProjectMigrationTask) configurationElement
                                .createExecutableExtension("class"); //$NON-NLS-1$
                        currentAction.setId(configurationElement.getAttribute("id")); //$NON-NLS-1$
                        currentAction.setName(configurationElement.getAttribute("name")); //$NON-NLS-1$
                        currentAction.setDescription(configurationElement.getAttribute("description")); //$NON-NLS-1$
                        currentAction.setVersion(configurationElement.getAttribute("version"));
                        currentAction.setBreaks(configurationElement.getAttribute("breaks"));
                        return currentAction;
                    }
                } catch (CoreException e) {
                    ExceptionHandler.process(e);
                }
                return null;
            }

        };

        return provider.createInstances();
    }

    public static List<IWorkspaceMigrationTask> getWorkspaceTasks() {
        IExtensionPointLimiter actionExtensionPoint = new ExtensionPointLimiterImpl(
                "org.talend.core.migrationTask", "workspacetask"); //$NON-NLS-1$ //$NON-NLS-2$

        ExtensionImplementationProvider<IWorkspaceMigrationTask> provider = new ExtensionImplementationProvider<IWorkspaceMigrationTask>(
                actionExtensionPoint) {

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.talend.commons.utils.workbench.extensions.ExtensionImplementationProvider#createImplementation(org
             * .eclipse.core.runtime.IExtension, org.talend.commons.utils.workbench.extensions.IExtensionPointLimiter)
             */
            @Override
            protected IWorkspaceMigrationTask createImplementation(IExtension extension,
                    IExtensionPointLimiter extensionPointLimiter, IConfigurationElement configurationElement) {
                try {
                    IWorkspaceMigrationTask currentAction = (IWorkspaceMigrationTask) configurationElement
                            .createExecutableExtension("class"); //$NON-NLS-1$
                    currentAction.setId(configurationElement.getAttribute("id")); //$NON-NLS-1$
                    currentAction.setName(configurationElement.getAttribute("name")); //$NON-NLS-1$
                    currentAction.setVersion(configurationElement.getAttribute("version"));
                    currentAction.setBreaks(configurationElement.getAttribute("breaks"));
                    return currentAction;
                } catch (CoreException e) {
                    ExceptionHandler.process(e);
                }
                return null;
            }

        };
        return provider.createInstances();
    }
}
