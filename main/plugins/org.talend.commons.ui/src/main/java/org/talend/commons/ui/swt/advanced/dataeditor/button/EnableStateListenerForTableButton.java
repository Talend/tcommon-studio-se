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
package org.talend.commons.ui.swt.advanced.dataeditor.button;

import org.talend.commons.ui.runtime.swt.tableviewer.selection.ILineSelectionListener;
import org.talend.commons.ui.runtime.swt.tableviewer.selection.LineSelectionEvent;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class EnableStateListenerForTableButton {

    private IExtendedTablePushButton pushButton;

    /**
     * DOC amaumont EnableStateHandlerForTableButton constructor comment.
     * 
     * @param extendedTableViewer
     * @param button
     */
    public EnableStateListenerForTableButton(final IExtendedTablePushButton button) {
        this.pushButton = button;

        final TableViewerCreator tableViewerCreator = button.getExtendedTableViewer().getTableViewerCreator();
        tableViewerCreator.getSelectionHelper().addAfterSelectionListener(new ILineSelectionListener() {

            public void handle(LineSelectionEvent e) {
                if (tableViewerCreator.getTable().getSelectionCount() > 0 && button.getEnabledState()) {
                    pushButton.getButton().setEnabled(true);
                } else {
                    pushButton.getButton().setEnabled(false);
                }
            }

        });

    }

    protected boolean getEnabledState() {
        TableViewerCreator tableViewerCreator = pushButton.getExtendedTableViewer().getTableViewerCreator();
        if (tableViewerCreator != null && tableViewerCreator.getTable() != null) {
            return tableViewerCreator.getTable().getSelectionCount() > 0;
        }
        return false;
    }

}
