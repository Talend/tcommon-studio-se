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
package org.talend.repository.items.importexport.ui.handlers.manager;

import org.talend.repository.items.importexport.ui.handlers.IImportExportItemsActionHelper;

/**
 * DOC ggu class global comment. Detailled comment
 */
public final class ImportExportUiHandlersManager {

    private static final ImportExportUiHandlersManager instance = new ImportExportUiHandlersManager();

    private final ImportExportUiHandlersRegistryReader registryReader;

    private ImportExportUiHandlersManager() {
        registryReader = new ImportExportUiHandlersRegistryReader();
        registryReader.init();
    }

    public static ImportExportUiHandlersManager getInstance() {
        return instance;
    }

    public IImportExportItemsActionHelper[] getActionsHelpers() {
        return registryReader.getActionsHelpers();
    }
}
