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
package org.talend.repository.items.importexport.handlers.imports;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.repository.items.importexport.handlers.model.ImportItem;
import org.talend.repository.items.importexport.manager.ResourcesManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractImportExecutableHandler implements IImportItemsHandler {

    protected final static Logger log = Logger.getLogger(AbstractImportExecutableHandler.class);

    protected boolean enableProductChecking;

    @Override
    public boolean isEnableProductChecking() {
        return this.enableProductChecking;
    }

    @Override
    public void setEnableProductChecking(boolean enableProductChecking) {
        this.enableProductChecking = enableProductChecking;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.items.importexport.handlers.imports.IImportItemsHandler#findRelatedImportItems(org.eclipse
     * .core .runtime.IProgressMonitor, org.talend.repository.items.importexport.manager.ResourcesManager,
     * org.talend.repository.items.importexport.handlers.model.ImportItem,
     * org.talend.repository.items.importexport.handlers.model.ImportItem[])
     */
    @Override
    public List<ImportItem> findRelatedImportItems(IProgressMonitor monitor, ResourcesManager resManager, ImportItem importItem,
            ImportItem[] allImportImportItems) throws Exception {
        return Collections.emptyList(); // default, no related items
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.items.importexport.handlers.imports.IImportItemsHandler#isPriorImportRelatedItem()
     */
    @Override
    public boolean isPriorImportRelatedItem() {
        return true; // default, import related item prior.
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.items.importexport.handlers.imports.IImportItemsHandler#afterImportingItems(org.eclipse
     * .core.runtime.IProgressMonitor, org.talend.repository.items.importexport.manager.ResourcesManager,
     * org.talend.repository.items.importexport.handlers.model.ImportItem)
     */
    @Override
    public void afterImportingItems(IProgressMonitor monitor, ResourcesManager resManager, ImportItem importItem)
            throws Exception {
        // default, nothing to do
    }

    @Override
    public boolean isValidSystemItem(ImportItem importItem) {
        return valid(importItem);
    }
}
