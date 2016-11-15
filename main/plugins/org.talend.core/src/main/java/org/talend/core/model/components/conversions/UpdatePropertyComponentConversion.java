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

public class UpdatePropertyComponentConversion implements IComponentConversion {

    private String value;

    private String name;

    public UpdatePropertyComponentConversion(String name, String value) {
        super();
        this.value = value;
        this.name = name;
    }

    public void transform(NodeType node) {
        ComponentUtilities.setNodeValue(node, name, value);
    }

}
