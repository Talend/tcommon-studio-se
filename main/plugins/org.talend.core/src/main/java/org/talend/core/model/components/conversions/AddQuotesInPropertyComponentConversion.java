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
package org.talend.core.model.components.conversions;

import org.talend.core.model.components.ComponentUtilities;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;

/**
 * DOC s class global comment. Detailled comment
 */
public class AddQuotesInPropertyComponentConversion implements IComponentConversion {

    private String propertyToModify;

    public AddQuotesInPropertyComponentConversion(String propertyToModify) {
        super();
        this.propertyToModify = propertyToModify;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.components.conversions.IComponentConversion#transform(org.talend.designer.core.model.utils.emf.talendfile.NodeType)
     */
    public void transform(NodeType node) {
        String value = ComponentUtilities.getNodePropertyValue(node, propertyToModify);
        if (!isWithinQuote(value)) {
            String newValue = "\"" + value + "\""; //$NON-NLS-1$ //$NON-NLS-2$
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
