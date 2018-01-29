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
package org.talend.repository.view.sorter;

import java.util.Arrays;
import java.util.Comparator;

import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class RepositoryCompareSorter implements IRepositoryNodeSorter {

    protected void sortChildren(Object[] children) {
        if (children == null) {
            return;
        }
        Arrays.sort(children, new Comparator<Object>() {

            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof RepositoryNode && o2 instanceof RepositoryNode) {
                    return compareNode((RepositoryNode) o1, (RepositoryNode) o2);
                }
                return 0; // no sort for others
            }
        });
    }

    protected void swap(Object[] children, int source, int target) {
        if (source != target && source > -1 && target > -1 && source < children.length && target < children.length) {
            if (source > target) {
                Object sourceObject = children[source];
                for (int i = source; i > target; i--) {
                    children[i] = children[i - 1]; // move previous one to bottom
                }
                children[target] = sourceObject;
            } else if (source < target) {
                Object sourceObject = children[source];
                for (int i = source; i < target; i++) {
                    children[i] = children[i + 1]; // move next one to up
                }
                children[target] = sourceObject;
            }
        }
    }

    protected int compareNode(RepositoryNode n1, RepositoryNode n2) {
        return 0; // keep order
    }
}
