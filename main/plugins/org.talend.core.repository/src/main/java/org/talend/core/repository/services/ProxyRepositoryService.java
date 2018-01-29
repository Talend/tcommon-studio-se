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
package org.talend.core.repository.services;

import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IProxyRepositoryService;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class ProxyRepositoryService implements IProxyRepositoryService {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryService#getProxyRepositoryFactory()
     */
    public IProxyRepositoryFactory getProxyRepositoryFactory() {
        return ProxyRepositoryFactory.getInstance();
    }

}
