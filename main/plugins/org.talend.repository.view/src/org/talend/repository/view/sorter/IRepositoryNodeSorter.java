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
package org.talend.repository.view.sorter;

import org.eclipse.jface.viewers.Viewer;

/**
 * DOC ggu class global comment. Detailled comment
 * 
 * NOTE: it's better to use the CNF way "appearsBefore" setting. Use this one to sort the final children. will do it for
 * RepoViewCommonViewer.getSortedChildren, when need some special cases. something like, 1) make sure the Standard job
 * on top. 2) "appearsBefore" can't be set for some products, because the id can't be found when the bundle is not
 * loaded.
 */
public interface IRepositoryNodeSorter {

    void sort(Viewer viewer, Object parentPath, Object[] children);
}
