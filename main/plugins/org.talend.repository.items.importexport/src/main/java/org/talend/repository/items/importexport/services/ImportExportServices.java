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
package org.talend.repository.items.importexport.services;

import org.talend.core.model.properties.Item;
import org.talend.core.ui.IImportExportServices;
import org.talend.repository.items.importexport.wizard.models.ItemImportNode;

/**
 * created by Talend on Dec 30, 2015 Detailled comment
 *
 */
public class ImportExportServices implements IImportExportServices {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.IImportExportServices#getItem()
     */
    @Override
    public Item getItem(Object importNode) {
        if (importNode instanceof ItemImportNode) {
            ItemImportNode itemImportNode = (ItemImportNode) importNode;
            if (itemImportNode.getItemRecord() != null) {
                return itemImportNode.getItemRecord().getItem();
            }
        }
        return null;
    }

}
