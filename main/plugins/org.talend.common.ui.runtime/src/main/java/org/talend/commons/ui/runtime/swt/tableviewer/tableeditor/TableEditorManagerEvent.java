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

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 */
public class TableEditorManagerEvent {

    private TableEditor tableEditor;

    private ITableEditorManagerEventType eventType;

    /**
     * DOC amaumont EVENT constructor comment.
     */
    public TableEditorManagerEvent(ITableEditorManagerEventType eventType, TableEditor tableEditor) {
        super();
        this.eventType = eventType;
        this.tableEditor = tableEditor;
    }

    public TableEditor getTableEditor() {
        return this.tableEditor;
    }

    public ITableEditorManagerEventType getEventType() {
        return this.eventType;
    }

}
