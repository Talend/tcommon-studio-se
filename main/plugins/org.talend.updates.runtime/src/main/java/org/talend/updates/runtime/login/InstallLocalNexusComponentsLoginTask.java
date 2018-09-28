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

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.INexusService;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;
import org.talend.login.AbstractLoginTask;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;
import org.talend.updates.runtime.UpdatesRuntimePlugin;
import org.talend.updates.runtime.engine.component.ComponentNexusP2ExtraFeature;
import org.talend.updates.runtime.engine.component.InstallComponentMessages;
import org.talend.updates.runtime.engine.factory.ComponentsNexusInstallFactory;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.model.FeatureCategory;
import org.talend.updates.runtime.model.interfaces.ITaCoKitCarFeature;
import org.talend.updates.runtime.utils.OsgiBundleInstaller;

/**
 * 
 * DOC ggu class global comment. Detailled comment
 */
public class InstallLocalNexusComponentsLoginTask extends AbstractLoginTask {

    private static Logger log = Logger.getLogger(InstallLocalNexusComponentsLoginTask.class);

    class ComponentsLocalNexusInstallFactory extends ComponentsNexusInstallFactory {

        @Override
        protected Set<ExtraFeature> getAllExtraFeatures(IProgressMonitor monitor) {
            IProgressMonitor progress = monitor;
            if (progress == null) {
                progress = new NullProgressMonitor();
            }
            try {
                ProjectPreferenceManager prefManager = new ProjectPreferenceManager(UpdatesRuntimePlugin.BUNDLE_ID);
                boolean enableShare = prefManager.getBoolean("repository.share.enable"); //$NON-NLS-1$
                if (!enableShare) {
                    return Collections.emptySet();
                }
                String repositoryId = prefManager.getValue("repository.share.repository.id"); //$NON-NLS-1$
                if (StringUtils.isBlank(repositoryId)) {
                    return Collections.emptySet();
                }
                INexusService nexusService = null;
                if (GlobalServiceRegister.getDefault().isServiceRegistered(INexusService.class)) {
                    nexusService = (INexusService) GlobalServiceRegister.getDefault().getService(INexusService.class);
                }
                if (nexusService == null) {
                    return Collections.emptySet();
                }
                ArtifactRepositoryBean artifactRepisotory = nexusService.getArtifactRepositoryFromServer();
                if (artifactRepisotory == null) {
                    return Collections.emptySet();
                }
                artifactRepisotory.setRepositoryId(repositoryId);
                if (monitor.isCanceled()) {
                    throw new OperationCanceledException();
                }

                return retrieveComponentsFromIndex(monitor, artifactRepisotory);
            } catch (Exception e) {
                if (CommonsPlugin.isDebugMode()) {
                    ExceptionHandler.process(e);
                }
                return Collections.emptySet();
            }
        }

    }

    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2017, 6, 7, 12, 0, 0);
        return gc.getTime();
    }

    @Override
    public boolean isCommandlineTask() {
        return true;
    }

    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
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
        try {
            ComponentsLocalNexusInstallFactory compInstallFactory = new ComponentsLocalNexusInstallFactory();

            Set<ExtraFeature> uninstalledExtraFeatures = new LinkedHashSet<ExtraFeature>();
            InstallComponentMessages messages = new InstallComponentMessages();

            compInstallFactory.retrieveUninstalledExtraFeatures(monitor, uninstalledExtraFeatures);
            for (ExtraFeature feature : uninstalledExtraFeatures) {
                install(monitor, feature, messages);
            }

            if (messages.isOk()) {
                log.info(messages.getInstalledMessage());
                if (!messages.isNeedRestart()) {
                    OsgiBundleInstaller.reloadComponents();
                } else {
                    System.setProperty("update.restart", Boolean.TRUE.toString()); //$NON-NLS-1$
                }
            }
            if (StringUtils.isNotEmpty(messages.getFailureMessage())) {
                log.error(messages.getFailureMessage());
            }
        } catch (Exception e) {
            throw new InvocationTargetException(e);
        }
    }

    private void install(IProgressMonitor monitor, ExtraFeature feature, InstallComponentMessages messages)
            throws Exception {
        if (feature instanceof FeatureCategory) {
            Set<ExtraFeature> children = ((FeatureCategory) feature).getChildren();
            for (ExtraFeature f : children) {
                install(monitor, f, messages);
            }
        }
        if (feature instanceof ComponentNexusP2ExtraFeature || feature instanceof ITaCoKitCarFeature) {
            if (feature.canBeInstalled(monitor)) {
                messages.analyzeStatus(feature.install(monitor, null));
                messages.setNeedRestart(feature.needRestart());
            }
        }
    }
}
