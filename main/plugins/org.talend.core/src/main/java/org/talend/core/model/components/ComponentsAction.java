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
package org.talend.core.model.components;

import org.talend.core.model.components.filters.IComponentFilter;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

/**
 * created by rdubois on 9 juil. 2015 Detailled comment
 *
 */
public class ComponentsAction {

    public static NodeType search(ProcessType processType, IComponentFilter filter) {
        if (processType == null || filter == null) {
            return null;
        }
        for (Object o : processType.getNode()) {
            NodeType node = (NodeType) o;
            if (filter.accept(node)) {
                return node;
            }
        }
        return null;
    }
}
