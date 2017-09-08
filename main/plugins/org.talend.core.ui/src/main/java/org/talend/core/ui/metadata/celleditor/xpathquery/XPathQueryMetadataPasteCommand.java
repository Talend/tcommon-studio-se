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

import java.util.ArrayList;
import java.util.List;

import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.ui.metadata.editor.MetadataTableEditor;
import org.talend.core.ui.metadata.extended.command.MetadataPasteCommand;

/**
 * cli class global comment. Detailled comment
 */
public class XPathQueryMetadataPasteCommand extends MetadataPasteCommand {

    public XPathQueryMetadataPasteCommand(ExtendedTableModel extendedTable) {
        super(extendedTable);
    }

    public XPathQueryMetadataPasteCommand(ExtendedTableModel extendedTable, Integer indexStartAdd) {
        super(extendedTable, indexStartAdd);
    }

    @Override
    public List createPastableBeansList(ExtendedTableModel extendedTable, List copiedObjectsList) {
        ArrayList list = new ArrayList();
        ArrayList<String> labelsExisted = getLabelsExisted(extendedTable);
        for (Object current : copiedObjectsList) {
            if (current instanceof IMetadataColumn) {
                IMetadataColumn copy = ((IMetadataColumn) current).clone();
                String nextGeneratedColumnName = ((MetadataTableEditor) extendedTable)
                        .getNextGeneratedColumnName(copy.getLabel());
                if (labelsExisted.contains(nextGeneratedColumnName)) {
                    nextGeneratedColumnName = validateColumnName(nextGeneratedColumnName, labelsExisted);
                }
                labelsExisted.add(nextGeneratedColumnName);
                copy.setLabel(nextGeneratedColumnName);
                if (copy instanceof XPathQueryMetadataColumnExt) {
                    list.add(copy);
                }
            }
        }
        return list;
    }
}
