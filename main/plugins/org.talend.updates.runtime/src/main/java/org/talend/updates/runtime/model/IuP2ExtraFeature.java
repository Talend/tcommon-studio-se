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
package org.talend.updates.runtime.model;

import org.eclipse.equinox.p2.metadata.IInstallableUnit;

/**
 * this will use the p2 repository url found the licence to look for features that will have a specifal p2 property that
 * has the names of the product it is compatible with.
 * 
 */
public class IuP2ExtraFeature extends P2ExtraFeature {
    
    public IuP2ExtraFeature( String uri) {
        baseRepoUriStr = uri;
    }

    public IuP2ExtraFeature(IInstallableUnit iu, String uri) {
        name = iu.getProperty(IInstallableUnit.PROP_NAME);
        description = iu.getProperty(IInstallableUnit.PROP_DESCRIPTION);
        version = iu.getVersion().getOriginal();
        p2IuId = iu.getId();
        baseRepoUriStr = uri;
        useLegacyP2Install = true;

    }
}
