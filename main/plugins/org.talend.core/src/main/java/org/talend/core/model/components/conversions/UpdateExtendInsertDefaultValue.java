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
package org.talend.core.model.components.conversions;

import org.talend.core.model.components.ComponentUtilities;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;

public class UpdateExtendInsertDefaultValue implements IComponentConversion {

    public void transform(NodeType node) {
        ElementParameterType propertyExtendInsert = ComponentUtilities.getNodeProperty(node, "EXTENDINSERT"); //$NON-NLS-1$
        ElementParameterType propertyDataAction = ComponentUtilities.getNodeProperty(node, "DATA_ACTION"); //$NON-NLS-1$
        ElementParameterType propertyEnableDebugMode = ComponentUtilities.getNodeProperty(node, "ENABLE_DEBUG_MODE"); //$NON-NLS-1$
        if (propertyDataAction != null && propertyDataAction.getValue().equalsIgnoreCase("INSERT")//$NON-NLS-1$
                && propertyEnableDebugMode != null && propertyEnableDebugMode.equals("false")) { //$NON-NLS-1$
            propertyExtendInsert.setValue("true");//$NON-NLS-1$
        }
    }
}
