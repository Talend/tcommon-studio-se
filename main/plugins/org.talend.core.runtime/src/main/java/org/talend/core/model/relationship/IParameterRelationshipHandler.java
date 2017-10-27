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
package org.talend.core.model.relationship;

import java.util.Map;
import java.util.Set;

import org.talend.core.model.properties.Item;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;

/**
 * DOC ggu class global comment. Detailled comment
 */
public interface IParameterRelationshipHandler {

    /**
     * try to find the relations from parameters.
     * 
     * @param parametersMap for one node's parameters
     * @param options
     * @return
     */
    Map<Relation, Set<Relation>> find(Item baseItem, Map<String, ElementParameterType> parametersMap, Map<?, ?> options);
}
