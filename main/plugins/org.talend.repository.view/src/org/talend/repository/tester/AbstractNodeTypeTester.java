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
package org.talend.repository.tester;

import java.util.Map;

import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.RepositoryNode;

public abstract class AbstractNodeTypeTester extends AbstractNodeTester {

    protected abstract Map<String, ERepositoryObjectType> getPropertyMapping();

    @Override
    public final boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
        final ERepositoryObjectType type = getPropertyMapping().get(property);
        if (null != type && receiver instanceof RepositoryNode) {
            return isTypeNode((RepositoryNode) receiver, type);
        }
        return false;
    }

    @Override
    protected final Boolean testProperty(Object receiver, String property, Object[] args, Object expectedValue) {
        return null;
    }

}
