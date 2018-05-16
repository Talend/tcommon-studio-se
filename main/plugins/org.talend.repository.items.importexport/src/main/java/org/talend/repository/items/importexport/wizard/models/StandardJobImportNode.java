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
package org.talend.repository.items.importexport.wizard.models;

import java.util.HashMap;
import java.util.Map;

import org.talend.core.model.repository.ERepositoryObjectType;

/**
 * created by kongxiaohan on Sep 28, 2015 Detailled comment
 *
 */
public class StandardJobImportNode extends FolderImportNode {

    private Map<ERepositoryObjectType, TypeImportNode> typeNodesChildrenMap = new HashMap<ERepositoryObjectType, TypeImportNode>();

    private ERepositoryObjectType type;

    public StandardJobImportNode(ERepositoryObjectType type) {
        super(type.getFolder());
        this.type = type;
    }

    public ERepositoryObjectType getType() {
        return this.type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.items.importexport.ui.wizard.imports.models.ImportNode#getDisplayLabel()
     */
    @Override
    public String getDisplayLabel() {
        return "Standard";
        // return type.getLabel();
    }

    public Map<ERepositoryObjectType, TypeImportNode> getTypeNodesChildrenMap() {
        return this.typeNodesChildrenMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.items.importexport.wizard.models.ImportNode#getName()
     */
    @Override
    public String getName() {
        ImportNode parentNode = this.getParentNode();
        if (parentNode instanceof StandardJobImportNode) {
            ProjectImportNode projectNode = this.getProjectNode();
            if (projectNode != null) {
                return projectNode.getName() + '/' + getPath();
            }
        }
        return super.getName();
    }

}
