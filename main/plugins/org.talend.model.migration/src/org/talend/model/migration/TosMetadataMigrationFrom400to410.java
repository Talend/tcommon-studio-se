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

package org.talend.model.migration;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.m2m.atl.common.ATLLogger;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.IModel;
import org.eclipse.m2m.atl.core.IReferenceModel;
import org.eclipse.m2m.atl.core.emf.EMFInjector;
import org.eclipse.m2m.atl.core.emf.EMFModel;
import org.eclipse.m2m.atl.core.emf.EMFModelFactory;
import org.eclipse.m2m.atl.engine.emfvm.launch.EMFVMLauncher;
import org.talend.model.emf.CwmResourceFactory;
import org.talend.model.migration.i18n.Messages;

/**
 * This perform the migration of TOS metadata file from version 4.0x to version 4.10
 * 
 */
public class TosMetadataMigrationFrom400to410 {

    private static final String IN_METADATA_400_URI = "platform:/plugin/org.talend.model.migration/migration_metamodel/metadata400forMigration.ecore"; //$NON-NLS-1$

    private static final String OUT_METADATA_410_URI = "platform:/plugin/org.talend.model.migration/migration_metamodel/metadata410forMigration.ecore"; //$NON-NLS-1$

    private static final String ATL_FILE_URI = "platform:/plugin/org.talend.model.migration/transformation/tos_metadata400to410.asm"; //$NON-NLS-1$

    private IReferenceModel inmodelMetamodel;

    private IReferenceModel outmodelMetamodel;

    private EMFModelFactory factory;

    private EMFInjector injector;

    private EMFVMLauncher launcher;

    private IModel inModel;

    private Object AtlModule;

    private static Logger log = Logger.getLogger(TosMetadataMigrationFrom400to410.class);

    private static Factory cwmResourceFactory = new CwmResourceFactory();

    /**
     * To enhance perfomance use one instance of this class to perform the migration of all the files
     */
    public TosMetadataMigrationFrom400to410() {
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl()); //$NON-NLS-1$
        // suppress the warning from the output console
        ATLLogger.getLogger().setLevel(Level.OFF);
        try {
            createMetamodels();
        } catch (ATLCoreException e) {
            log.error("Could not load Migration 410 to 400 metamodel", e); //$NON-NLS-1$
            throw new RuntimeException(e);
        }
    }

    /**
     * create ATL metamodels
     * 
     * @throws ATLCoreException if metamodel could not be loaded
     */
    private void createMetamodels() throws ATLCoreException {
        factory = new EMFModelFactory();
        Map<String, Object> etfm = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();
        etfm.put("*", cwmResourceFactory); //$NON-NLS-1$

        injector = new EMFInjector();
        inmodelMetamodel = factory.newReferenceModel();
        injector.inject(inmodelMetamodel, IN_METADATA_400_URI);
        outmodelMetamodel = factory.newReferenceModel();
        injector.inject(outmodelMetamodel, OUT_METADATA_410_URI);
        launcher = new EMFVMLauncher();
        try {
            AtlModule = launcher.loadModule(URI.create(ATL_FILE_URI).toURL().openStream());
        } catch (Exception e) {
            // this should never happend at runtime
            log.error(Messages.getString("TosMetadataMigrationFrom400to410_tos_error_log", ATL_FILE_URI), e); //$NON-NLS-1$
            throw new RuntimeException(e);
        }
    }

    /**
     * This performs the migration in memory from the input Uri and return a resource for the user to persist
     * 
     * @param inUri the input model URI to be migrated
     * @param monitor to monitor migration progress (untested)
     * @return the EMF Resource of the trasnformed model, for your convenience
     * @throws ATLCoreException if migration fails somehow
     */
    public Resource migrate(String inUri, IProgressMonitor monitor) throws ATLCoreException {
        inModel = factory.newModel(inmodelMetamodel);
        injector.inject(inModel, inUri);
        EMFModel outModel = (EMFModel) factory.newModel(outmodelMetamodel);
        // outModel.setEmfResourceFactory(cwmResourceFactory);
        launcher.initialize(Collections.EMPTY_MAP);
        launcher.addInModel(inModel, "IN", "INMODEL"); //$NON-NLS-1$//$NON-NLS-2$
        launcher.addOutModel(outModel, "OUT", "OUTMODEL"); //$NON-NLS-1$//$NON-NLS-2$
        // launcher.addOutModel(migModel, "MIG", "MIGMODEL");
        launcher.launch("run", monitor, Collections.EMPTY_MAP, AtlModule); //$NON-NLS-1$
        return outModel.getResource();
    }
}
