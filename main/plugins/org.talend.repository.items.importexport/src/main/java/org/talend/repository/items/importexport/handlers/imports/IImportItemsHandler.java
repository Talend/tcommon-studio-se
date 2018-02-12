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

import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.repository.items.importexport.handlers.model.ImportItem;
import org.talend.repository.items.importexport.manager.ResourcesManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public interface IImportItemsHandler extends IExecutableExtension {

    /**
     * 
     * DOC ggu Comment method "isEnableProductChecking".
     * 
     * @return if need recheck the items is under current product or not.
     */
    boolean isEnableProductChecking();

    /**
     * 
     * DOC ggu Comment method "setEnableProductChecking".
     * 
     * @param enableProductChecking
     */
    void setEnableProductChecking(boolean enableProductChecking);

    /**
     * If enable to import normal files which can't be loaded via *.properties and not valid, will try to check via the
     * path of resource to import.
     * 
     */
    boolean valid(ResourcesManager resManager, IPath path);

    /**
     * If the items can be loaded to EMF via *.properties, will use this valid to test the import item first.
     */
    boolean valid(ImportItem importItem);

    /**
     * If it is a valid and builtin/system item
     * 
     * @param importItem
     * @return
     */
    boolean isValidSystemItem(ImportItem importItem);

    /**
     * According to the valid resource path, resolve the EMF properties file, and check the overwrite status or be valid
     * item or not, etc.
     * 
     * @param monitor
     * @param resManager
     * @param resourcePath according to the this path to calculate the item record.
     * @param overwrite
     * @param existedImportItems this items list has been calculated. will check the duplicated name or not.
     * @return if not valid, will return null.
     * 
     */
    ImportItem createImportItem(IProgressMonitor monitor, ResourcesManager resManager, IPath resourcePath, boolean overwrite,
            List<ImportItem> existedImportItems) throws Exception;

    /**
     * 
     * Almost seem with createImportItem, but this method won't do check
     * 
     * @param monitor
     * @param resManager
     * @param resourcePath
     * @param overwrite
     * @param existedImportItems
     * @return
     * @throws Exception
     */
    ImportItem generateImportItem(IProgressMonitor monitor, ResourcesManager resManager, IPath resourcePath, boolean overwrite,
            List<ImportItem> existedImportItems) throws Exception;

    /**
     * Find out current item's related items. in order to implictly import the related items.
     * 
     * @param monitor
     * @param resManager
     * @param importItem the item will be imported.
     * @param allImportItems the list of items are all populated items.
     * @return the related item for selected items.
     */
    List<ImportItem> findRelatedImportItems(IProgressMonitor monitor, ResourcesManager resManager, ImportItem importItem,
            ImportItem[] allImportItems) throws Exception;

    /**
     * Will import the valid selected items to current project.
     * 
     * 
     * @param importItem the importing item
     * @param overwrite If true will overwrite the original item. else will ignore to import the selected item.
     * @param destinationPath, only used for contextual import otherwise it is null. If not null it is to the handler
     * responsibility to use it if the items it handles should be placed in this contextual folder. Handlers that do not
     * recognize this destination path may import their items by ignoring this value.
     */
    void doImport(IProgressMonitor monitor, ResourcesManager resManager, ImportItem importItem, boolean overwrite,
            IPath destinationPath, Set<String> overwriteDeletedItems, Set<String> idDeletedBeforeImport) throws Exception;

    /**
     * When there are some related items to be import, will use this flag.
     * 
     * If true, will import the related items before current item.
     * 
     * if false, will after import current item.
     * 
     */
    boolean isPriorImportRelatedItem();

    /**
     * Maybe, need some disposed actions(like EMF Resources), sync functions, etc.
     * 
     */
    void afterImportingItems(IProgressMonitor monitor, ResourcesManager resManager, ImportItem importItem) throws Exception;
}
