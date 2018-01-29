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
package org.talend.core.runtime.projectsetting;

import java.util.List;

import org.eclipse.jface.preference.IPreferenceNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
public interface IProjectSettingContainer {

    /**
     * 
     * add the child nodes under the parent id of node.
     */
    boolean addChildrenPreferenceNodes(String parentId, List<IPreferenceNode> childrenNodes);

    /**
     * 
     * remove the child nodes of parent id
     */
    boolean removeChildrenPreferenceNodes(String parentId, List<String> childrenIds);

    boolean openPage(String nodeId, Object data);

}
