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
package org.talend.designer.maven;

import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.routines.CodesJarInfo;
import org.talend.core.runtime.services.IDesignerMavenService;
import org.talend.designer.maven.tools.CodesJarM2CacheManager;
import org.talend.designer.maven.utils.CodesJarMavenUtil;

public class DesignerMavenService implements IDesignerMavenService {

    @Override
    public String getCodesJarPackageByInnerCode(RoutineItem innerCodeItem) {
        return CodesJarMavenUtil.getCodesJarPackageByInnerCode(innerCodeItem);
    }

    @Override
    public String getImportGAVPackageForCodesJar(CodesJarInfo info) {
        return CodesJarMavenUtil.getImportGAVPackageForCodesJar(info);
    }

    @Override
    public void updateCodeJarMavenProject(CodesJarInfo info, boolean needReSync) throws Exception {
        CodesJarM2CacheManager.updateCodesJarProject(info, needReSync);
    }

}
