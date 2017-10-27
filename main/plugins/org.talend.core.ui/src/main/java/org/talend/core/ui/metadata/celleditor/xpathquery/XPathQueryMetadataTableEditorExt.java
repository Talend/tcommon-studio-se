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
package org.talend.core.ui.metadata.celleditor.xpathquery;

import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.ui.metadata.editor.MetadataTableEditor;

/**
 * cli class global comment. Detailled comment
 */
public class XPathQueryMetadataTableEditorExt extends MetadataTableEditor {

    public XPathQueryMetadataTableEditorExt(IMetadataTable metadataTable, String titleName) {
        super(metadataTable, titleName);
    }

    public IMetadataColumn createNewMetadataColumn() {
        final XPathQueryMetadataColumnExt metadataColumnExt = new XPathQueryMetadataColumnExt((MetadataColumn) super
                .createNewMetadataColumn());
        // PTODO
        return metadataColumnExt;
    }
}
