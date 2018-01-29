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

import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.impl.ElementValueTypeImpl;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class UpdateAttributesFortFilterRow implements IComponentConversion {

    private String value;

    private String column;

    private String name;

    public UpdateAttributesFortFilterRow(String name, String column, String value) {
        super();
        this.value = value;
        this.column = column;
        this.name = name;
    }

    public void transform(NodeType node) {
        for (Object o : node.getElementParameter()) {
            ElementParameterType t = (ElementParameterType) o;
            if (t.getName().equals(name)) {
                for (Object ob : t.getElementValue()) {
                    ElementValueTypeImpl s = (ElementValueTypeImpl) ob;
                    if (s.getElementRef().equals(column)) {
                        s.setValue(value);
                        break;
                    }
                }
            }
        }
    }
}
