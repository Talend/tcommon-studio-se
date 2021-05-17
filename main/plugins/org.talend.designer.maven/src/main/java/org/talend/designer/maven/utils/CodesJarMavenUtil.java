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
package org.talend.designer.maven.utils;

import org.apache.commons.lang3.StringUtils;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.routines.CodesJarInfo;
import org.talend.core.model.routines.RoutinesUtil;
import org.talend.repository.ProjectManager;

public class CodesJarMavenUtil {

    public static String getCodesJarPackageByInnerCode(RoutineItem innerCodeItem) {
        String codesJarName = RoutinesUtil.getCodesJarLabelByInnerCode(innerCodeItem);
        String projectTechName = ProjectManager.getInstance().getProject(innerCodeItem).getTechnicalLabel();
        return StringUtils.replace(PomIdsHelper.getCodesJarGroupIdByInnerCode(projectTechName, innerCodeItem), ".", "/") + "/" //$NON-NLS-1$
                + codesJarName.toLowerCase();
    }

    public static String getGAVPackageForCodesJar(Item codesJarItem) {
        return StringUtils.replace(PomIdsHelper.getCodesJarGroupId(CodesJarInfo.create(codesJarItem.getProperty())), ".", "/")
                + "/" + codesJarItem.getProperty().getLabel().toLowerCase();
    }

    public static String getImportGAVPackageForCodesJar(CodesJarInfo info) {
        return PomIdsHelper.getCodesJarGroupId(info) + "." + info.getLabel().toLowerCase() + ".*";
    }

}
