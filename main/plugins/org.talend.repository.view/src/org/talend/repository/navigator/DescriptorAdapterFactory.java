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
package org.talend.repository.navigator;

import org.eclipse.core.runtime.IAdapterFactory;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.repository.navigator.INavigatorDescriptor;
import org.talend.repository.navigator.TalendRepositoryRoot;

/**
 * DOC sgandon class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class DescriptorAdapterFactory implements IAdapterFactory {

    @Override
    public Object getAdapter(Object adaptableObject, Class adapterType) {
        if (adapterType == INavigatorDescriptor.class) {
            if (adaptableObject instanceof TalendRepositoryRoot) {
                return new INavigatorDescriptor() {

                    @Override
                    public String getDescriptor() {
                        return ProxyRepositoryFactory.getInstance().getNavigatorViewDescription();
                    }
                };
            }// else return null
        }// else return null
        return null;
    }

    @Override
    public Class[] getAdapterList() {
        return new Class[] { INavigatorDescriptor.class };
    }

}
