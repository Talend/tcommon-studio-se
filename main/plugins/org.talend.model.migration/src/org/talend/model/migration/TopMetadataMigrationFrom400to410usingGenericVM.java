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
import java.util.HashMap;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.m2m.atl.common.ATLLogger;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.IModel;
import org.eclipse.m2m.atl.core.IReferenceModel;
import org.eclipse.m2m.atl.core.launch.ILauncher;
import org.eclipse.m2m.atl.core.ui.vm.RegularVMLauncher;
import org.eclipse.m2m.atl.core.ui.vm.asm.ASMExtractor;
import org.eclipse.m2m.atl.core.ui.vm.asm.ASMFactory;
import org.eclipse.m2m.atl.core.ui.vm.asm.ASMInjector;
import org.eclipse.m2m.atl.core.ui.vm.asm.ASMModelWrapper;
import org.talend.model.migration.atl.ASMExtractorPatched;
import org.talend.model.migration.i18n.Messages;

/**
 * class that perform the migration for TOP metadata from version 4.0x to 4.10 <br/>
 * 
 */
public class TopMetadataMigrationFrom400to410usingGenericVM {

    private static final String IN_METADATA_400_URI = "platform:/plugin/org.talend.model.migration/migration_metamodel/org.talend.cwm.ext400andCWMandAnalysis.ecore"; //$NON-NLS-1$

    private static final String OUT_METADATA_410_URI = "platform:/plugin/org.talend.model.migration/migration_metamodel/metadata410referingLocalCwm400andAnalysis.ecore"; //$NON-NLS-1$

    private static final String ATL_FILE_URI = "platform:/plugin/org.talend.model.migration/transformation/top_metadata400to410.asm"; //$NON-NLS-1$

    private IReferenceModel inmodelMetamodel;

    private IReferenceModel outmodelMetamodel;

    private ASMFactory factory;

    private ASMInjector injector;

    private ILauncher launcher;

    private IModel inModel;

    private Object AtlModule;

    private HashMap<String, Object> saveOptions;

    private ASMExtractor extractor;

    private HashMap<String, Object> inModelHM;

    private HashMap<String, Object> outModelHM;

    private static Logger log = Logger.getLogger(TopMetadataMigrationFrom400to410usingGenericVM.class);

    /**
     * default constructor, please use only one instance of this file to perform all the transformations.
     */
    public TopMetadataMigrationFrom400to410usingGenericVM() {
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl()); //$NON-NLS-1$
        // suppress the warning from the output console
        ATLLogger.getLogger().setLevel(Level.OFF);
        try {
            createMetamodels();
        } catch (ATLCoreException e) {
            // this should never happend
            log.error("Could not load Migration 410 to 400 metamodel", e); //$NON-NLS-1$
            throw new RuntimeException(e);
        }
    }

    private void createMetamodels() throws ATLCoreException {
        launcher = new RegularVMLauncher();
        factory = new ASMFactory();
        injector = new ASMInjector();
        // WARNING, the following 2 blocks must be kept in this order
        // otherwise the loading of an input model fails, I do not know why
        {// output metamodel
            HashMap<String, Object> metaOutHM = new HashMap<String, Object>(3);
            metaOutHM.put(ASMFactory.OPTION_MODEL_HANDLER, "EMF_PATCHED"); //$NON-NLS-1$
            metaOutHM.put(ASMFactory.OPTION_MODEL_NAME, "OUTMODEL"); //$NON-NLS-1$
            metaOutHM.put(ASMFactory.OPTION_MODEL_PATH, OUT_METADATA_410_URI);
            outmodelMetamodel = factory.newReferenceModel(metaOutHM);
            injector.inject(outmodelMetamodel, OUT_METADATA_410_URI);
        }

        {// input metamodel
            HashMap<String, Object> metaInHM = new HashMap<String, Object>(3);
            metaInHM.put(ASMFactory.OPTION_MODEL_HANDLER, "EMF"); //$NON-NLS-1$
            metaInHM.put(ASMFactory.OPTION_MODEL_NAME, "INMODEL"); //$NON-NLS-1$
            metaInHM.put(ASMFactory.OPTION_MODEL_PATH, IN_METADATA_400_URI);
            inmodelMetamodel = factory.newReferenceModel(metaInHM);
            injector.inject(inmodelMetamodel, IN_METADATA_400_URI);
        }
        {// input model properties
            inModelHM = new HashMap<String, Object>(3);
            inModelHM.put(ASMFactory.OPTION_MODEL_NAME, "IN"); //$NON-NLS-1$
            inModelHM.put(ASMFactory.OPTION_NEW_MODEL, false);
        }
        {// output model properties
            outModelHM = new HashMap<String, Object>(3);
            outModelHM.put(ASMFactory.OPTION_MODEL_NAME, "OUT"); //$NON-NLS-1$
            outModelHM.put(ASMFactory.OPTION_NEW_MODEL, true);
        }
        launcher = new RegularVMLauncher();
        try {
            AtlModule = launcher.loadModule(URI.create(ATL_FILE_URI).toURL().openStream());
        } catch (Exception e) {
            // this should never happend at runtime
            log.error(Messages.getString("TopMetadataMigrationFrom400to410usingGenericVM_top_error_log", ATL_FILE_URI), e); //$NON-NLS-1$
            throw new RuntimeException(e);
        }
        extractor = new ASMExtractorPatched();
        saveOptions = new HashMap<String, Object>();
        saveOptions.put(XMIResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
    }

    /**
     * Perform the migration of the input URI and save it to the outUri. WARNING, the file refering to external file
     * such as .softwaresystem.softwaredeployment will fail to migrate if the refering file is found (YES you red it
     * well). For the migration to succeed the refered file must NOT be found. <br>
     * One solution is to rename the TOP major folders <br>
     * \TDQ_Libraries , \TDQ_Data Profiling, TQD_Metadata into something else before performing the migration and then
     * rename those folders back to their original names.(use a try {}finally{} to be sure to revert back the folder
     * names. )
     * 
     * @param inUri URI that will be loaded for migration
     * @param outUri URI that will be created as a result of the migration
     * @param monitor, monitor the progress of the migration (untested)
     * @throws ATLCoreException if the migration fails somehow
     */
    @SuppressWarnings("unchecked")
    public void migrate(String inUri, String outUri, IProgressMonitor monitor) throws ATLCoreException {
        // launcher.initialize(Collections.singletonMap("allowInterModelReferences", (Object) true));
        launcher.initialize(Collections.EMPTY_MAP);
        // input model
        inModelHM.remove(ASMFactory.OPTION_MODEL_PATH);// removes the key if it was added at previous call of this
        // method
        inModelHM.put(ASMFactory.OPTION_MODEL_PATH, inUri);
        inModel = factory.newModel(inmodelMetamodel, inModelHM);
        injector.inject(inModel, inUri);

        // output model
        IModel outModel = factory.newModel(outmodelMetamodel, outModelHM);
        launcher.addInModel(inModel, "IN", "INMODEL"); //$NON-NLS-1$//$NON-NLS-2$
        launcher.addOutModel(outModel, "OUT", "OUTMODEL"); //$NON-NLS-1$//$NON-NLS-2$
        launcher.launch("run", monitor, Collections.EMPTY_MAP, AtlModule); //$NON-NLS-1$
        ASMModelWrapper modelWrapper = (ASMModelWrapper) outModel;
        // modelWrapper.getModelLoader().
        extractor.extract(outModel, outUri, saveOptions);
    }
}
