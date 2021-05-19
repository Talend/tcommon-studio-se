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
package org.talend.core.model.components;

import org.talend.core.GlobalServiceRegister;
import org.talend.core.IService;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 *
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 *
 */
public interface IComponentsService extends IService {

    public IComponentsFactory getComponentsFactory();

    public IComponentFileNaming getComponentFileNaming();

    public static IComponentsService get() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IComponentsService.class)) {
            return GlobalServiceRegister.getDefault().getService(IComponentsService.class);
        }
        return null;
    }

}
