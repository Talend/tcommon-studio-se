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

import java.util.Map;

import org.eclipse.m2m.atl.drivers.emf4atl.EMFModelLoader;
import org.eclipse.m2m.atl.engine.vm.ModelLoader;

/**
 * this is a work around a bug of ATL, where you cannot set any parameters for saving EMF models or we need to save them
 * in UTF-8 for instance which is not the default value
 * this class is the implementation of the extension point : org.eclipse.m2m.atl.engine.vm.modelhandler
 * */
public class AtlEMFModelHandlerPatched extends org.eclipse.m2m.atl.drivers.emf4atl.AtlEMFModelHandler {

    /**
     * EMFModelLoader is extended to add a setSaveoption method
     * 
     */
    public class EMFModelLoaderPatched extends EMFModelLoader {

        /**
         * DOC sgandon Comment method "setSaveOptions".
         * 
         * @param options
         */
        public void setSaveOptions(Map<String, Object> options) {
            saveOptions = options;
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.m2m.atl.engine.vm.AtlModelHandler#createModelLoader()
     */
    public ModelLoader createModelLoader() {
        return new EMFModelLoaderPatched();
    }

}
