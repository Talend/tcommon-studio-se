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
package org.talend.core.model.components.filters;

import org.talend.core.model.components.ComponentUtilities;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;

/**
 * Filter components by property. Property and value are specified in constructor.<br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class PropertyComponentFilter extends NameComponentFilter implements IComponentFilter {

    private String property;

    private String value;

    public PropertyComponentFilter(String name, String property, String value) {
        super(name);
        this.property = property;
        this.value = value;
    }

    @Override
    public boolean accept(NodeType node) {
        boolean toReturn = (name == null ? true : super.accept(node));
        if (toReturn) {
            String pValue = ComponentUtilities.getNodePropertyValue(node, property);
            // toReturn = pValue.startsWith(value);
            toReturn = (pValue != null) && (pValue.equals(value));
        }
        return toReturn;
    }

}
