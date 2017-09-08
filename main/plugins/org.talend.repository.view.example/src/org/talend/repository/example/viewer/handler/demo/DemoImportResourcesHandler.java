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
package org.talend.repository.example.viewer.handler.demo;

import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.repository.items.importexport.handlers.imports.IImportResourcesHandler;
import org.talend.repository.items.importexport.handlers.model.ImportItem;
import org.talend.repository.items.importexport.manager.ResourcesManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class DemoImportResourcesHandler implements IImportResourcesHandler {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.items.importexport.handlers.imports.IImportResourcesHandler#preImport(org.talend.repository
     * .items.importexport.manager.ResourcesManager)
     */
    @Override
    public void preImport(IProgressMonitor monitor, ResourcesManager resManager, ImportItem[] checkedItemRecords,
            ImportItem[] allImportItemRecords) {
        if (resManager.getPaths().isEmpty()) {
            ExceptionHandler.log("There is no resource to import."); //$NON-NLS-1$
        } else {
            ExceptionHandler.log("The items have been prepared to do import."); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.items.importexport.handlers.imports.IImportResourcesHandler#postImport(org.talend.repository
     * .items.importexport.manager.ResourcesManager)
     */
    @Override
    public void postImport(IProgressMonitor monitor, ResourcesManager resManager, ImportItem[] importedItemRecords) {
        ExceptionHandler.log("The items have been imported successfully."); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.items.importexport.handlers.imports.IImportResourcesHandler#prePopulate(org.eclipse.core
     * .runtime.IProgressMonitor, org.talend.repository.items.importexport.manager.ResourcesManager)
     */
    @Override
    public void prePopulate(IProgressMonitor monitor, ResourcesManager resManager) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.items.importexport.handlers.imports.IImportResourcesHandler#postPopulate(org.eclipse.core
     * .runtime.IProgressMonitor, org.talend.repository.items.importexport.manager.ResourcesManager,
     * org.talend.repository.items.importexport.handlers.model.ImportItem[])
     */
    @Override
    public void postPopulate(IProgressMonitor monitor, ResourcesManager resManager, ImportItem[] populatedItemRecords) {
        // nothing to do

    }

}
