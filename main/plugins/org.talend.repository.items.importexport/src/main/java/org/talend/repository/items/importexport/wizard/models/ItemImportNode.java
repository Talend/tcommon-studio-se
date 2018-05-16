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
package org.talend.repository.items.importexport.wizard.models;

import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.items.importexport.handlers.model.ImportItem;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ItemImportNode extends ImportNode {

    public ItemImportNode(ImportItem itemRecord) {
        super();
        this.itemRecord = itemRecord;
        this.setVisible(itemRecord.isVisible() && itemRecord.getErrors().isEmpty());
    }

    @Override
    public String getName() {
        StringBuffer sb = new StringBuffer();
        ImportNode parentNode = this.getParentNode();
        if (parentNode != null) {
            sb.append(parentNode.getName());
        }
        sb.append('/');
        sb.append(getItemRecord().getLabel());

        ERepositoryObjectType repositoryType = getItemRecord().getRepositoryType();
        if (repositoryType != null) {
            sb.append('(');
            sb.append(repositoryType.getType());
            sb.append(')');
        }
        return sb.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.items.importexport.ui.wizard.imports.models.ImportNode#getDisplayLabel()
     */
    @Override
    public String getDisplayLabel() {
        return getItemRecord().getLabel();
    }

}
