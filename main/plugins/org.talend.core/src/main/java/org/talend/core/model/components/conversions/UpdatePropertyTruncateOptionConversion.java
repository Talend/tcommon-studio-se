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
package org.talend.core.model.components.conversions;

import org.talend.core.model.components.ComponentUtilities;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;

/**
 * DOC s class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2007-10-25 17:06:40 +0000 rshi $
 */
public class UpdatePropertyTruncateOptionConversion implements IComponentConversion {

    private static final String OLD_TABLE_ACTION_OPTION = "CLEAR"; //$NON-NLS-1$

    private static final String NEW_TABLE_ACTION_OPTION = "TRUNCATE"; //$NON-NLS-1$

    public void transform(NodeType node) {
        // TODO Auto-generated method stub
        ElementParameterType property = ComponentUtilities.getNodeProperty(node, "TABLE_ACTION"); //$NON-NLS-1$
        if (property != null && property.getValue().equals(OLD_TABLE_ACTION_OPTION)) { //$NON-NLS-1$
            property.setValue(NEW_TABLE_ACTION_OPTION);
        }
    }

}
