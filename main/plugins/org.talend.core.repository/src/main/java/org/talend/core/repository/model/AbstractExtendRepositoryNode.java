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
package org.talend.core.repository.model;

import org.talend.commons.ui.runtime.repository.IExtendRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractExtendRepositoryNode implements IExtendRepositoryNode {

    public AbstractExtendRepositoryNode() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.IExtendRepositoryNode#getChildren()
     */
    @Override
    public Object[] getChildren() {
        return new RepositoryNode[0];
    }

}
