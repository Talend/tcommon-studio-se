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
package org.talend.core.runtime.repository.build;

/**
 * DOC ggu class global comment. Detailled comment
 */
class BuildProviderRegistry {

    AbstractBuildProvider provider;

    String id, description, overrideId;

    int getOrder() {
        if (provider.buildType != null) {
            return provider.getBuildType().order;
        }
        return 0;
    }
}
