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
package org.talend.core.runtime.repository.build;

import java.util.Map;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractBuildProvider implements IBuildParametes, IBuildPomCreatorParameters, IBuildJobParameters {

    BuildType buildType;

    public BuildType getBuildType() {
        return buildType;
    }

    public boolean valid(Map<String, Object> parameters) {
        return false;
    }

    public IMavenPomCreator createPomCreator(Map<String, Object> parameters) {
        return null;
    }

    public IBuildExportHandler createBuildExportHandler(Map<String, Object> parameters) {
        return null;
    }
}
