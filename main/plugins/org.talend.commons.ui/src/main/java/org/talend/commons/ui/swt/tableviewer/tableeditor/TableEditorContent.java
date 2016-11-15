// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.commons.ui.swt.tableviewer.tableeditor;

import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorColumnNotModifiable;
import org.talend.commons.ui.runtime.swt.tableviewer.tableeditor.TableEditorContentNotModifiable;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;

public abstract class TableEditorContent extends TableEditorContentNotModifiable {

    public abstract Control initialize(Table table, TableEditor tableEditor, TableViewerCreatorColumn currentColumn,
            Object currentRowObject, Object currentCellValue);

    @Override
    public Control initialize(Table table, TableEditor tableEditor, TableViewerCreatorColumnNotModifiable currentColumn,
            Object currentRowObject, Object currentCellValue) {
        return initialize(table, tableEditor, (TableViewerCreatorColumn) currentColumn, currentRowObject, currentCellValue);
    }

}
