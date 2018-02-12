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
package org.talend.commons.ui.runtime.swt.tableviewer.tableeditor;

import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorColumnNotModifiable;

/**
 * DOC amaumont class global comment. Detailled comment <br/> $Id: TableEditorInitializer.java,v 1.1 2006/06/02 15:24:10
 * amaumont Exp $
 */
public abstract class TableEditorContentNotModifiable {

    private boolean layoutEnabled = true; // for performance use

    /**
     * You can override this method if necessary.
     * 
     * @param table
     * @return TableEditor
     */
    public TableEditor createTableEditor(final Table table) {
        return new TableEditor(table) {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.custom.TableEditor#layout()
             */
            @Override
            public void layout() {
                if (layoutEnabled) {
                    super.layout();
                }
            }

        };
    }

    public abstract Control initialize(Table table, TableEditor tableEditor, TableViewerCreatorColumnNotModifiable currentColumn,
            Object currentRowObject, Object currentCellValue);

    /**
     * Getter for layoutEnabled. For performance use
     * 
     * @return the layoutEnabled
     */
    public boolean isLayoutEnabled() {
        return this.layoutEnabled;
    }

    /**
     * Sets the layoutEnabled. For performance use
     * 
     * @param layoutEnabled the layoutEnabled to set
     */
    public void setLayoutEnabled(boolean layoutEnabled) {
        this.layoutEnabled = layoutEnabled;
    }

}
