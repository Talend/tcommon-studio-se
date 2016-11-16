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
package org.talend.commons.ui.runtime.swt.tableviewer.behavior;

import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorColumnNotModifiable;

public class ColumnCellModifier implements IColumnCellModifier {

    private TableViewerCreatorColumnNotModifiable column;

    public ColumnCellModifier(TableViewerCreatorColumnNotModifiable column) {
        this.column = column;
    }

    public boolean canModify(Object bean) {
        return column.isModifiable();
    }

    public Object getValue(Object bean) {
        return null;
    }

    public boolean modify(Object bean, Object value) {
        return false;
    }

}
