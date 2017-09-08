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
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;

/**
 * DOC qwei class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class UpdateLookupColumnConversion implements IComponentConversion {

    private String propertyToModify;

    public UpdateLookupColumnConversion(String propertyToModify) {
        super();
        this.propertyToModify = propertyToModify;
    }

    public void transform(NodeType node) {
        String value = ComponentUtilities.getNodePropertyValue(node, propertyToModify);
        if (value != null) {
            String newValue = value.substring(value.indexOf(".") + 1, value.length()); //$NON-NLS-1$
            ComponentUtilities.setNodeValue(node, propertyToModify, newValue);
        }
    }
}
