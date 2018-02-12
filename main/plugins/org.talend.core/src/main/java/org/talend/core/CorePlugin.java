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
package org.talend.core;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.BundleContext;
import org.talend.core.context.Context;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.migration.IMigrationToolService;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.service.IDesignerMapperService;
import org.talend.core.service.IDesignerSparkMapperService;
import org.talend.core.service.IDesignerXMLMapperService;
import org.talend.core.service.IWebService;
import org.talend.core.service.IWebServiceTos;
import org.talend.core.services.ICreateXtextProcessService;
import org.talend.core.services.IRcpService;
import org.talend.core.services.resource.IResourceService;
import org.talend.designer.business.diagram.custom.IDiagramModelService;
import org.talend.designer.codegen.ICodeGeneratorService;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.model.IMetadataService;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryLocalProviderService;
import org.talend.repository.model.IRepositoryService;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class CorePlugin extends Plugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.talend.core"; //$NON-NLS-1$

    public static final String PROJECT_LANGUAGE_TYPE = "PROJECT_LANGUAGE_TYPE"; //$NON-NLS-1$

    // The shared instance
    private static CorePlugin plugin;

    public CorePlugin() {
        plugin = this;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start(BundleContext contextP) throws Exception {
        super.start(contextP);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(BundleContext contextP) throws Exception {
        plugin = null;
        super.stop(contextP);
    }

    public static CorePlugin getDefault() {
        return plugin;
    }

    private ScopedPreferenceStore preferenceStore;

    /**
     * 
     * DOC ggu Comment method "getPreferenceStore".
     * 
     * just want to remove the extended AbstractUIPlugin for this class.
     * 
     * @return
     */
    public IPreferenceStore getPreferenceStore() {
        // Create the preference store lazily.
        if (preferenceStore == null) {
            preferenceStore = new ScopedPreferenceStore(new InstanceScope(), getBundle().getSymbolicName());

        }
        return preferenceStore;
    }

    /**
     * Getter for context.
     * 
     * @return the context
     */
    public static Context getContext() {
        return CoreRuntimePlugin.getInstance().getContext();
    }

    public IProxyRepositoryFactory getProxyRepositoryFactory() {
        IRepositoryService service = getRepositoryService();
        return service.getProxyRepositoryFactory();
    }

    /**
     * DOC get a implement of RunProcessService.
     * 
     * @return a implement of RunProcessService
     */
    public IRunProcessService getRunProcessService() {
        IService service = GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
        return (IRunProcessService) service;
    }

    public IDesignerCoreService getDesignerCoreService() {
        IService service = GlobalServiceRegister.getDefault().getService(IDesignerCoreService.class);
        return (IDesignerCoreService) service;
    }

    public ILibrariesService getLibrariesService() {
        return (ILibrariesService) GlobalServiceRegister.getDefault().getService(ILibrariesService.class);
    }

    public ILibraryManagerService getRepositoryBundleService() {
        return (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(ILibraryManagerService.class);
    }

    public IRepositoryService getRepositoryService() {
        IService service = GlobalServiceRegister.getDefault().getService(IRepositoryService.class);
        return (IRepositoryService) service;
    }

    public IMetadataService getMetadataService() {
        IService service = GlobalServiceRegister.getDefault().getService(IMetadataService.class);
        return (IMetadataService) service;
    }

    public IRepositoryLocalProviderService getRepositoryLocalProviderService() {
        return (IRepositoryLocalProviderService) GlobalServiceRegister.getDefault().getService(
                IRepositoryLocalProviderService.class);
    }

    public ICodeGeneratorService getCodeGeneratorService() {
        return (ICodeGeneratorService) GlobalServiceRegister.getDefault().getService(ICodeGeneratorService.class);
    }

    public IMigrationToolService getMigrationToolService() {
        return (IMigrationToolService) GlobalServiceRegister.getDefault().getService(IMigrationToolService.class);
    }

    public IDiagramModelService getDiagramModelService() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IDiagramModelService.class)) {
            return (IDiagramModelService) GlobalServiceRegister.getDefault().getService(IDiagramModelService.class);
        }
        return null;
    }

    public IResourceService getResourceService() {
        return (IResourceService) GlobalServiceRegister.getDefault().getService(IResourceService.class);
    }

    public IRcpService getRcpService() {
        return (IRcpService) GlobalServiceRegister.getDefault().getService(IRcpService.class);
    }

    /*
     * public boolean useSQLTemplate() { return (Boolean) CorePlugin.getContext().getProperty("useSQLTemplate"); }
     * 
     * public boolean useRefproject() { return (Boolean) CorePlugin.getContext().getProperty("useRefProject"); }
     */

    public IDesignerMapperService getMapperService() {
        return (IDesignerMapperService) GlobalServiceRegister.getDefault().getService(IDesignerMapperService.class);
    }

    public IDesignerXMLMapperService getXMLMapperService() {
        return (IDesignerXMLMapperService) GlobalServiceRegister.getDefault().getService(IDesignerXMLMapperService.class);
    }

    public IDesignerSparkMapperService getSparkMapperService() {
        return (IDesignerSparkMapperService) GlobalServiceRegister.getDefault().getService(IDesignerSparkMapperService.class);
    }

    public IWebService getWebService() {
        return (IWebService) GlobalServiceRegister.getDefault().getService(IWebService.class);
    }

    public IWebServiceTos getWebServiceTos() {
        return (IWebServiceTos) GlobalServiceRegister.getDefault().getService(IWebServiceTos.class);
    }

    public ICreateXtextProcessService getCreateXtextProcessService() {
        return (ICreateXtextProcessService) GlobalServiceRegister.getDefault().getService(ICreateXtextProcessService.class);
    }

}
