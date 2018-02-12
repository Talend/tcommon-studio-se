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
package org.talend.designer.maven.ui.setting.repository.tester;

import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.IRepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractRepositoryNodeRepositorySettingTester implements IRepositorySettingTester {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.maven.ui.setting.repository.tester.IRepositorySettingTester#valid(java.lang.Object)
     */
    @Override
    public boolean valid(Object object) {
        if (object instanceof IRepositoryNode) {
            ERepositoryObjectType contentType = ((IRepositoryNode) object).getContentType();
            if (contentType != null) {
                if (contentType.equals(getType())) {
                    return true;
                }
            }
        }
        return false;
    }

    protected abstract ERepositoryObjectType getType();
}
