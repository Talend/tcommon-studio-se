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

import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.ui.context.model.table.ContextTableConstants;
import org.talend.core.ui.context.model.table.ContextTableTabChildModel;
import org.talend.core.ui.context.model.table.ContextTableTabParentModel;

/**
 * created by ldong on Jul 28, 2014 Detailled comment
 * 
 */
public class ContextNatTableUtils {

    public static boolean isEmptyTreeNode(Object treeData) {
        if (treeData instanceof ContextTableTabParentModel) {
            if (((ContextTableTabParentModel) treeData).getContextParameter() == null
                    && ((ContextTableTabParentModel) treeData).getChildren().size() == 0) {
                return true;
            }
        }
        return false;
    }

    public static IContextParameter getRealParameter(IContextManager manager, String property, Object element) {
        IContextParameter para = null;
        IContext context = null;
        if (manager != null) {
            if (!(property.equals(ContextTableConstants.COLUMN_NAME_PROPERTY))) {
                context = manager.getContext(property);
            }
            if (context == null) {
                return null;
            }

            if (element instanceof ContextTableTabParentModel) {
                if (IContextParameter.BUILT_IN.equals(((ContextTableTabParentModel) element).getSourceId())) {
                    IContextParameter builtContextParameter = ((ContextTableTabParentModel) element).getContextParameter();
                    if (builtContextParameter != null) {
                        para = context.getContextParameter(builtContextParameter.getName());
                    }
                }
            } else if (element instanceof ContextTableTabChildModel) {
                ContextTableTabChildModel child = (ContextTableTabChildModel) element;
                String sourceId = child.getContextParameter().getSource();
                para = context.getContextParameter(sourceId, ((ContextTableTabChildModel) element).getContextParameter()
                        .getName());
            }
        }
        return para;
    }

}
