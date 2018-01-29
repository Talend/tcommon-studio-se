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

package org.talend.model.migration.atl;

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.IModel;
import org.eclipse.m2m.atl.core.ui.vm.asm.ASMExtractor;
import org.eclipse.m2m.atl.core.ui.vm.asm.ASMModelWrapper;
import org.eclipse.m2m.atl.engine.vm.nativelib.ASMModel;
import org.talend.model.migration.atl.AtlEMFModelHandlerPatched.EMFModelLoaderPatched;

/**
 * this is a patched devivatif of atl ASMExtractor in oder to provide a way to set options during the save of a migrated model.
 * <br/>
 *
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 *
 */
public class ASMExtractorPatched extends ASMExtractor {

    /**
     * this is overriden to set the save option befaore calling the extract(IModel sourceModel, String target).
     * 
     * @see org.eclipse.m2m.atl.core.IExtractor#extract(org.eclipse.m2m.atl.core.IModel, java.lang.String,
     * java.util.Map)
     */
    public void extract(IModel sourceModel, String target, Map<String, Object> options) throws ATLCoreException {
        ASMModelWrapper modelWrapper = (ASMModelWrapper) sourceModel;
        //we assume the model loader was created according to the EMF_PATCHED definition
        EMFModelLoaderPatched modelLoader = (EMFModelLoaderPatched) modelWrapper.getModelLoader();
        modelLoader.setSaveOptions(options);
        extract(sourceModel, target);
    }

}
