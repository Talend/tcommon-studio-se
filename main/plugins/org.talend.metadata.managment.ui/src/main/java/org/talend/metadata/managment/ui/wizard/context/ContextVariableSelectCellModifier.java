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
package org.talend.metadata.managment.ui.wizard.context;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;
import org.talend.core.ui.context.model.table.ConectionAdaptContextVariableModel;
import org.talend.core.ui.context.model.template.ContextConstant;

/**
 * ldong class global comment. Detailled comment
 */
public class ContextVariableSelectCellModifier implements ICellModifier {

    private TableViewer tv;

    private String[] connectionFields;

    public ContextVariableSelectCellModifier(TableViewer tv, String[] connectionFields) {
        this.tv = tv;
        this.connectionFields = connectionFields;
    }

    @Override
    public boolean canModify(Object element, String property) {
        ConectionAdaptContextVariableModel o = (ConectionAdaptContextVariableModel) element;
        if (ContextConstant.CONNECTION_FIELD_NAME.equals(property)) {
            return true;
        }
        if (ContextConstant.VARIABLE_COLUMN_NAME.equals(property)) {
            return false;
        }
        return false;
    }

    @Override
    public Object getValue(Object element, String property) {
        ConectionAdaptContextVariableModel o = (ConectionAdaptContextVariableModel) element;
        if (property.equals(ContextConstant.VARIABLE_COLUMN_NAME)) {
            return o.getName();
        } else if (property.equals(ContextConstant.CONNECTION_FIELD_NAME)) {
            return getValueIndex(o.getValue());
        }
        return null;
    }

    @Override
    public void modify(Object element, String property, Object value) {
        boolean isChanged = false;
        TableItem item = (TableItem) element;
        ConectionAdaptContextVariableModel o = (ConectionAdaptContextVariableModel) item.getData();
        System.out.println(o.getValue());

        if (property.equals(ContextConstant.CONNECTION_FIELD_NAME)) {
            // the second column is the combo,need the set the new value
            Integer comboIndex = (Integer) value;
            if (comboIndex != -1) {
                o.setValue(connectionFields[comboIndex]);
                isChanged = true;
            }
        }

        if (property.equals(ContextConstant.CUSTOM_VARIABLE_NAME)) {
            o.setCustomValue((String) value);
            isChanged = true;
        }
        if (isChanged) {
            tv.update(o, null);
        }
    }

    private int getValueIndex(String value) {
        for (int i = 0; i < connectionFields.length; i++) {
            if (connectionFields[i].equals(value))
                return i;
        }
        return -1;
    }
}
