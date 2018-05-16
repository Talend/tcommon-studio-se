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
package org.talend.core.ui.context.nattableTree;

import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.validate.DataValidator;
import org.eclipse.nebula.widgets.nattable.data.validate.ValidationFailedException;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsDataProvider;
import org.talend.core.model.process.IContextManager;
import org.talend.core.ui.context.ContextTreeTable.ContextTreeNode;
import org.talend.core.ui.i18n.Messages;

/**
 * created by ldong on Jul 17, 2014 Detailled comment
 * 
 */
public class EventDataValidator extends DataValidator {

    private IDataProvider dataProvider;

    private IContextManager manager;

    EventDataValidator(IDataProvider bodyDataProvider, IContextManager manager) {
        this.dataProvider = bodyDataProvider;
        this.manager = manager;
    }

    @Override
    public boolean validate(int columnIndex, int rowIndex, Object newValue) {
        @SuppressWarnings("unchecked")
        ContextTreeNode rowNode = ((GlazedListsDataProvider<ContextTreeNode>) dataProvider).getRowObject(rowIndex);
        String oldName = ContextNatTableUtils.getCurrentContextModelName(rowNode.getTreeData());
        String newName = (String) newValue;
        if (oldName.equals(newName)) {
            return true;
        }
        if (manager.checkValidParameterName(rowNode.getName(), (String) newValue)) {
            return true;
        }
        if (null != newName && !"".equals(newName)) {
            if (newName.length() > 255) {
                throw new ValidationFailedException(Messages.getString("ContextValidator.ParameterNotValid")); //$NON-NLS-1$
            }
        }

        throw new ValidationFailedException(Messages.getString("ContextValidator.ParameterNotValid")); //$NON-NLS-1$
    }

}
