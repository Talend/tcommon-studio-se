// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.ui.metadata.editor;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.advanced.dataeditor.button.QuotePushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.QuotePushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.button.CasePushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.CasePushButtonForExtendedTable;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.core.ui.metadata.extended.command.MetadataCaseCommand;
import org.talend.core.ui.metadata.extended.command.MetadataQuoteCommand;


public class MetadataQuoteToolbarEditorView extends MetadataToolbarEditorView {


    public MetadataQuoteToolbarEditorView(Composite parent, int style, AbstractExtendedTableViewer extendedTableViewer) {
        super(parent, style, extendedTableViewer);
    }

    public MetadataQuoteToolbarEditorView(Composite parent, int style, AbstractExtendedTableViewer extendedTableViewer, String dbmsId) {
        super(parent, style, extendedTableViewer, dbmsId);
        this.getQuoteButton().setDbmsId(dbmsId);
        
    }
    
    @Override
    public CasePushButton createCasePushButton() {
        return new CasePushButtonForExtendedTable(toolbar, extendedTableViewer) {

            @Override
            protected Command getCommandToExecute(ExtendedTableModel extendedTable, List beansToConvertCase, int[] selectionIndices, boolean isUpperCase) {
                return new MetadataCaseCommand(extendedTable, beansToConvertCase, selectionIndices, isUpperCase);
            }

        };
    }
  
    @Override
    protected QuotePushButton createQuotePushButton() {
        return new QuotePushButtonForExtendedTable(toolbar, extendedTableViewer) {

            @Override
            protected Command getCommandToExecute(ExtendedTableModel extendedTable, List beansToQuote, int[] selectionIndices, String quote, boolean isAddingQuote) {
                return new MetadataQuoteCommand(extendedTable, beansToQuote, selectionIndices, quote, isAddingQuote);
            }

        };
    }
}
