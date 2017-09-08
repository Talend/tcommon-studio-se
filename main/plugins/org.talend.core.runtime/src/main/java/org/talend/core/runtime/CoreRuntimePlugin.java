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
package org.talend.core.runtime;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.BundleContext;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.AbstractDQModelService;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ICoreService;
import org.talend.core.IRepositoryContextService;
import org.talend.core.IService;
import org.talend.core.IStatusPreferenceInitService;
import org.talend.core.context.Context;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;
import org.talend.core.service.IWebService;
import org.talend.core.service.IWebServiceTos;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.repository.model.IMetadataService;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IProxyRepositoryService;
import org.talend.repository.model.IRepositoryService;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class CoreRuntimePlugin extends Plugin {

    public static final String PLUGIN_ID = "org.talend.core.runtime"; //$NON-NLS-1$

    // The data profiling perspective id.
    protected static final String DATA_PROFILING_PERSPECTIVE_ID = "org.talend.dataprofiler.DataProfilingPerspective"; //$NON-NLS-1$

    public static final String EMPTY_STRING = "";

    /** Context. */
    private final Context context;

    private static CoreRuntimePlugin plugin = null;

    private static IRepositoryContextService repositoryContextService = null;

    protected ProjectPreferenceManager projectPreferenceManager;

    public CoreRuntimePlugin() {
        context = new Context();
    }

    public static CoreRuntimePlugin getInstance() {
        if (plugin == null && !Platform.isRunning()) {
            plugin = new CoreRuntimePlugin();
        }
        return plugin;
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
    }

    /**
     * Getter for context.
     * 
     * @return the context
     */
    public Context getContext() {
        return this.context;
    }

    public IProxyRepositoryFactory getProxyRepositoryFactory() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IProxyRepositoryService.class)) {
            IProxyRepositoryService service = (IProxyRepositoryService) GlobalServiceRegister.getDefault().getService(
                    IProxyRepositoryService.class);
            return service.getProxyRepositoryFactory();
        }
        return null;
    }

    public IRepositoryService getRepositoryService() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRepositoryService.class)) {
            IService service = GlobalServiceRegister.getDefault().getService(IRepositoryService.class);
            return (IRepositoryService) service;
        }
        return null;
    }

    public IRepositoryContextService getRepositoryContextService() {
        if (repositoryContextService == null) {
            if (Platform.isRunning()) {
                if (GlobalServiceRegister.getDefault().isServiceRegistered(IRepositoryContextService.class)) {
                    IService service = GlobalServiceRegister.getDefault().getService(IRepositoryContextService.class);
                    repositoryContextService = (IRepositoryContextService) service;
                }
            } else {
                try {
                    final String serviceClassName = "org.talend.core.repository.services.StandaloneRepositoryContextService"; //$NON-NLS-1$
                    repositoryContextService = (IRepositoryContextService) Class.forName(serviceClassName).newInstance();
                } catch (InstantiationException e) {
                    ExceptionHandler.process(e);
                } catch (IllegalAccessException e) {
                    ExceptionHandler.process(e);
                } catch (ClassNotFoundException e) {
                    ExceptionHandler.process(e);
                }
            }
        }
        return repositoryContextService;
    }

    public IMetadataService getMetadataService() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IMetadataService.class)) {
            IService service = GlobalServiceRegister.getDefault().getService(IMetadataService.class);
            return (IMetadataService) service;
        }
        return null;
    }

    public AbstractDQModelService getDQModelService() {
        if (GlobalServiceRegister.getDefault().isDQModelServiceRegistered(AbstractDQModelService.class)) {
            IService service = GlobalServiceRegister.getDefault().getDQModelService(AbstractDQModelService.class);
            return (AbstractDQModelService) service;
        }
        return null;
    }

    public IDesignerCoreService getDesignerCoreService() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreService.class)) {
            IService service = GlobalServiceRegister.getDefault().getService(IDesignerCoreService.class);
            return (IDesignerCoreService) service;
        }
        return null;
    }

    public ICoreService getCoreService() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ICoreService.class)) {
            IService service = GlobalServiceRegister.getDefault().getService(ICoreService.class);
            return (ICoreService) service;
        }
        return null;
    }

    public ILibrariesService getLibrariesService() {
        return (ILibrariesService) GlobalServiceRegister.getDefault().getService(ILibrariesService.class);
    }

    public IStatusPreferenceInitService getStatusPreferenceInitService() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IStatusPreferenceInitService.class)) {
            return (IStatusPreferenceInitService) GlobalServiceRegister.getDefault().getService(
                    IStatusPreferenceInitService.class);
        }
        return null;
    }

    public IWebService getWebService() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IWebService.class)) {
            return (IWebService) GlobalServiceRegister.getDefault().getService(IWebService.class);
        }
        return null;
    }

    public IWebServiceTos getWebServiceTos() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IWebServiceTos.class)) {
            return (IWebServiceTos) GlobalServiceRegister.getDefault().getService(IWebServiceTos.class);
        }
        return null;
    }

    /**
     * DOC bZhou Comment method "isDataProfilePerspectiveSelected".
     * 
     * @return
     */
    public boolean isDataProfilePerspectiveSelected() {
        IPerspectiveDescriptor curPerspective = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .getPerspective();
        return curPerspective.getId().equals(DATA_PROFILING_PERSPECTIVE_ID);
    }

    public String getActivedPerspectiveId() {
        if (CommonsPlugin.isHeadless()) {
            return null; // for headless, no perspective, should be for all.
        }
        final String[] perspectiveId = new String[1];
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                if (activeWorkbenchWindow != null) {
                    IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
                    if (activePage != null) {
                        IPerspectiveDescriptor curPerspective = activePage.getPerspective();
                        if (curPerspective != null) {
                            perspectiveId[0] = curPerspective.getId();
                        }
                    }
                }

            }
        });
        return perspectiveId[0];
    }

    public ProjectPreferenceManager getProjectPreferenceManager() {
        if (projectPreferenceManager == null) {
            projectPreferenceManager = new ProjectPreferenceManager(PLUGIN_ID);
        }
        return projectPreferenceManager;
    }
    
}
