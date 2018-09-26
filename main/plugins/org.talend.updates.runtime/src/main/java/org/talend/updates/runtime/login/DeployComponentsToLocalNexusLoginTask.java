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
package org.talend.updates.runtime.login;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.login.AbstractLoginTask;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;
import org.talend.updates.runtime.nexus.component.ComponentsDeploymentManager;
import org.talend.updates.runtime.utils.PathUtils;

/**
 *
 * created by ycbai on 2017年5月23日 Detailled comment
 *
 */
public class DeployComponentsToLocalNexusLoginTask extends AbstractLoginTask {

    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2017, 5, 23, 12, 0, 0);
        return gc.getTime();
    }

    @Override
    public boolean isCommandlineTask() {
        return true;
    }

    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        File installedComponentFolder = PathUtils.getComponentsInstalledFolder();
        depoloyComponentsFromFolder(monitor, installedComponentFolder);
    }

    protected void depoloyComponentsFromFolder(final IProgressMonitor monitor, final File componentsBaseFolder) {
        if (componentsBaseFolder == null || !componentsBaseFolder.exists() || !componentsBaseFolder.isDirectory()) {
            return;
        }
        boolean isRemote = false;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRepositoryService.class)) {
            IRepositoryService repositoryService = (IRepositoryService) GlobalServiceRegister.getDefault()
                    .getService(IRepositoryService.class);
            IProxyRepositoryFactory repositoryFactory = repositoryService.getProxyRepositoryFactory();
            try {
                boolean isLocalProject = repositoryFactory.isLocalConnectionProvider();
                boolean isOffline = false;
                if (!isLocalProject) {
                    RepositoryContext repositoryContext = (RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                            .getProperty(Context.REPOSITORY_CONTEXT_KEY);
                    isOffline = repositoryContext.isOffline();
                }
                isRemote = !isLocalProject && !isOffline;
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
        }
        if (!isRemote) {
            return;
        }
        ComponentsDeploymentManager deployManager = new ComponentsDeploymentManager();
        final File[] updateFiles = componentsBaseFolder.listFiles();
        if (updateFiles != null && updateFiles.length > 0) {
            for (File f : updateFiles) {
                try {
                    deployManager.deployComponentsToArtifactRepository(monitor, f);
                } catch (Exception e) {
                    // won't block others to install.
                    if (!CommonsPlugin.isHeadless()) {
                        ExceptionHandler.process(e);
                    }
                }
            }
        }
    }

}
