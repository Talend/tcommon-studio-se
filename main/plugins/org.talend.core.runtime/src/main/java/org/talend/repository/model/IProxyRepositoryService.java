// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.model;

import org.talend.core.GlobalServiceRegister;
import org.talend.core.IService;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public interface IProxyRepositoryService extends IService {

    public IProxyRepositoryFactory getProxyRepositoryFactory();

    public static IProxyRepositoryService get() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IProxyRepositoryService.class)) {
            return GlobalServiceRegister.getDefault().getService(IProxyRepositoryService.class);
        }
        return null;
    }
}
