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

import org.talend.designer.core.model.utils.emf.talendfile.NodeType;

/**
 * Filter components by name. Name is specified in constructor.<br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class NameComponentFilter implements IComponentFilter {

    protected String name;

    public NameComponentFilter(String name) {
        super();
        this.name = name;
    }

    public boolean accept(NodeType node) {
        return node.getComponentName().equals(name);
    }

}
