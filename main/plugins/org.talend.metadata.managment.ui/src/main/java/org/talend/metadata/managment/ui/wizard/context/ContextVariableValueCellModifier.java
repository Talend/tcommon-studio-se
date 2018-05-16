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
package org.talend.metadata.managment.ui.wizard.context;

import java.util.List;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.ui.context.model.template.ContextConstant;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;

public class ContextVariableValueCellModifier implements ICellModifier {

    private TableViewer tv;

    private List<IContextParameter> contextParameters;

    public ContextVariableValueCellModifier(TableViewer tv) {
        this.tv = tv;
    }

    @Override
    public boolean canModify(Object element, String property) {
        if (ContextConstant.VARIABLE_COLUMN_VALUE.equals(property)) {
            return true;
        }
        return false;
    }

    @Override
    public Object getValue(Object element, String property) {
        ContextParameterType o = (ContextParameterType) element;
        if (property.equals(ContextConstant.VARIABLE_COLUMN_NAME)) {
            return o.getName();
        } else if (property.equals(ContextConstant.VARIABLE_COLUMN_VALUE)) {
            return o.getRawValue();
        }
        return null;
    }

    @Override
    public void modify(Object element, String property, Object value) {
        boolean isChanged = false;
        TableItem item = (TableItem) element;
        ContextParameterType o = (ContextParameterType) item.getData();
        if (property.equals(ContextConstant.VARIABLE_COLUMN_VALUE)) {
            o.setRawValue((String) value);
            isChanged = true;
        }
        if (isChanged) {
            tv.update(o, null);
        }
    }

}
