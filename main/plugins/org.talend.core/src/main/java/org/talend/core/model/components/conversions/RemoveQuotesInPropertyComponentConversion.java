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
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;

public class RemoveQuotesInPropertyComponentConversion implements IComponentConversion {

    private String propertyToModify;

    public RemoveQuotesInPropertyComponentConversion(String propertyToModify) {
        super();
        this.propertyToModify = propertyToModify;
    }

    public void transform(NodeType node) {
        String value = ComponentUtilities.getNodePropertyValue(node, propertyToModify);
        if (value != null && isWithinQuote(value)) {
            String newValue = value.substring(1, value.length() - 1);
            ComponentUtilities.setNodeValue(node, propertyToModify, newValue);
        }
    }

    private boolean isWithinQuote(String string) {
        if (string == null) {
            return true;
        }
        boolean isWithin = false;
        if (string.startsWith("\"") && string.endsWith("\"") && (string.length() > 1)) { //$NON-NLS-1$ //$NON-NLS-2$
            isWithin = true;
        } else if (string.startsWith("\'") && string.endsWith("\'") && (string.length() > 1)) { //$NON-NLS-1$ //$NON-NLS-2$
            isWithin = true;
        }
        return isWithin;
    }

}
