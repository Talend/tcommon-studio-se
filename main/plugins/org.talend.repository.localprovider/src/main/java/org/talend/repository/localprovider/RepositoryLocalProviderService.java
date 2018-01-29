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
package org.talend.repository.localprovider;

import org.talend.repository.localprovider.model.LocalRepositoryFactory;
import org.talend.repository.model.IRepositoryLocalProviderService;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class RepositoryLocalProviderService implements IRepositoryLocalProviderService {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryLocalProviderService#resetXmiResourceSet()
     */
    public void resetXmiResourceSet() {
        LocalRepositoryFactory.getInstance().xmiResourceManager.resetResourceSet();
    }
}
