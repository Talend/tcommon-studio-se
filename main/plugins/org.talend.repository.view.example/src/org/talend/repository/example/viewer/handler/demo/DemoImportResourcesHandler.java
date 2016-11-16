// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.example.viewer.handler.demo;

import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.repository.items.importexport.handlers.imports.IImportResourcesHandler;
import org.talend.repository.items.importexport.handlers.model.ImportItem;
import org.talend.repository.items.importexport.manager.ResourcesManager;

public class DemoImportResourcesHandler implements IImportResourcesHandler {

    @Override
    public void preImport(IProgressMonitor monitor, ResourcesManager resManager, ImportItem[] checkedItemRecords,
            ImportItem[] allImportItemRecords) {
        if (resManager.getPaths().isEmpty()) {
            ExceptionHandler.log("There is no resource to import."); //$NON-NLS-1$
        } else {
            ExceptionHandler.log("The items have been prepared to do import."); //$NON-NLS-1$
        }
    }

    @Override
    public void postImport(IProgressMonitor monitor, ResourcesManager resManager, ImportItem[] importedItemRecords) {
        ExceptionHandler.log("The items have been imported successfully."); //$NON-NLS-1$
    }

    @Override
    public void prePopulate(IProgressMonitor monitor, ResourcesManager resManager) {
        // TODO Auto-generated method stub

    }

    @Override
    public void postPopulate(IProgressMonitor monitor, ResourcesManager resManager, ImportItem[] populatedItemRecords) {
        // nothing to do
    }

}
