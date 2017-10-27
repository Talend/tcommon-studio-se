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
package org.talend.repository.viewer.content.code;

import org.talend.repository.model.RepositoryNode;
import org.talend.repository.tester.CodeNodeTester;
import org.talend.repository.viewer.content.SubEmptyTopNodeContentProvider;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractCodeContentProvider extends SubEmptyTopNodeContentProvider {

    CodeNodeTester nodeTester = new CodeNodeTester();

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.viewer.content.SingleTopLevelContentProvider#isRootNodeType(java.lang.Object)
     */
    @Override
    protected boolean isRootNodeType(Object element) {
        if (element instanceof RepositoryNode) {
            return nodeTester.isCodeTopNode((RepositoryNode) element);
        } else {
            return false;
        }

    }

}
