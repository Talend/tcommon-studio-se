// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

public abstract class AbstractRepositoryNodeRepositorySettingTester implements IRepositorySettingTester {

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
