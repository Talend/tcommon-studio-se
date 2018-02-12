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
package org.talend.repository.items.importexport.handlers.model.internal;

import org.talend.repository.items.importexport.handlers.imports.IImportResourcesHandler;

/**
 * DOC ggu class global comment. Detailled comment
 * 
 * work for resourceImportHandler with extension point "org.talend.repository.items.importexport.handler".
 */
public class ImportResourcesProviderRegistry extends BasicRegistry {

    private IImportResourcesHandler importResourcesHandler;

    public ImportResourcesProviderRegistry(String bundleId, String id) {
        super(bundleId, id);
    }

    /**
     * Getter for importResourcesHandler.
     * 
     * @return the importResourcesHandler
     */
    public IImportResourcesHandler getImportResourcesHandler() {
        return this.importResourcesHandler;
    }

    /**
     * Sets the importResourcesHandler.
     * 
     * @param handler the importResourcesHandler to set
     */
    public void setImportResourcesHandler(IImportResourcesHandler handler) {
        this.importResourcesHandler = handler;
    }

}
