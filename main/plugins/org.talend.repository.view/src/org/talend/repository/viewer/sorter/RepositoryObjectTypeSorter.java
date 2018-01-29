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
package org.talend.repository.viewer.sorter;

import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.view.sorter.RepositoryRootNodeCompareSorter;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class RepositoryObjectTypeSorter extends RepositoryRootNodeCompareSorter {

    @Override
    protected int compareNode(RepositoryNode n1, RepositoryNode n2) {
        ERepositoryObjectType type1 = n1.getContentType();
        ERepositoryObjectType type2 = n2.getContentType();
        if (type1 == null) { // null, will be front, seems only recycle bin will be null.
            return 1;
        }
        if (type2 == null) {
            return -1;
        }

        return type1.compareTo(type2);
    }
}
