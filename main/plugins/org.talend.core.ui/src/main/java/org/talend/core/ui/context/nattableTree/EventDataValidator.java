// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.ui.context.nattableTree;

import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.validate.DataValidator;

/**
 * created by ldong on Jul 17, 2014 Detailled comment
 * 
 */
public class EventDataValidator extends DataValidator {

    private IDataProvider dataProvider;

    EventDataValidator(IDataProvider bodyDataProvider) {
        this.dataProvider = bodyDataProvider;
    }

    @Override
    public boolean validate(int columnIndex, int rowIndex, Object newValue) {
        // ContextTreeNode rowNode = ((GlazedListsDataProvider<ContextTreeNode>) dataProvider).getRowObject(rowIndex);
        // TODO:in case will add some validate for the context
        return true;
    }
}
