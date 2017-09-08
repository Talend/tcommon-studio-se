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
package org.talend.commons.ui.runtime.swt.tableviewer.behavior;

import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorColumnNotModifiable;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 */
public class ColumnCellModifier implements IColumnCellModifier {

    private TableViewerCreatorColumnNotModifiable column;

    /**
     * DOC amaumont ColumnCellModifier constructor comment.
     * 
     * @param column
     */
    public ColumnCellModifier(TableViewerCreatorColumnNotModifiable column) {
        this.column = column;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.tableviewer.behavior.IColumnCellModifier#canModify(java.lang.Object)
     */
    public boolean canModify(Object bean) {
        return column.isModifiable();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.tableviewer.behavior.IColumnCellModifier#getValue(java.lang.Object)
     */
    public Object getValue(Object bean) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.tableviewer.behavior.IColumnCellModifier#modify(java.lang.Object,
     * java.lang.Object)
     */
    public boolean modify(Object bean, Object value) {
        return false;
    }

}
