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
package org.talend.repository.items.importexport.handlers.imports;

import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.repository.items.importexport.handlers.model.ImportItem;
import org.talend.repository.items.importexport.manager.ResourcesManager;

/**
 * DOC ggu class global comment. Detailled comment
 * 
 * The resManager mostly is FilesManager(FileResourcesUnityManager).
 * 
 * If the original import resouce is zip or tar(.gz), The archieve will be decompressed into a temp folder for
 * FilesManager.
 * 
 * If the original import resouces is folder already. All resources will be copied to a temp folder too, in order to
 * avoid breaking the original resources.
 */
public interface IImportResourcesHandler {

    /**
     * 
     * Before do calc item records,or need prepare something before import.
     * 
     */
    void prePopulate(IProgressMonitor monitor, ResourcesManager resManager);

    /**
     * After populate the resouces for item records.
     */
    void postPopulate(IProgressMonitor monitor, ResourcesManager resManager, ImportItem[] populatedItemRecords);

    /**
     * If need do some operations before importing items. can do it in this.
     * 
     * 
     * @param monitor
     * @param resManager
     * @param checkedItemRecords which items are checked from the import tree.
     * @param allImportItemRecords which items are populated
     */
    void preImport(IProgressMonitor monitor, ResourcesManager resManager, ImportItem[] checkedItemRecords,
            ImportItem[] allImportItemRecords);

    /**
     * After all items have been imported, or some items should do some special operations after import.
     * 
     * also, provide the items which are imported already.
     */
    void postImport(IProgressMonitor monitor, ResourcesManager resManager, ImportItem[] importedItemRecords);
}
