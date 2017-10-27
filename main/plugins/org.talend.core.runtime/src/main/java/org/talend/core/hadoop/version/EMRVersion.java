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
package org.talend.core.hadoop.version;

import java.util.ArrayList;
import java.util.List;

/**
 * created by ycbai on 2013-9-16 Detailled comment
 * 
 * @deprecated @link EHadoopVersion instead.
 */
public enum EMRVersion {

    MR1, // EHadoopVersion.HADOOP_1

    YARN, // EHadoopVersion.HADOOP_2

    ;

    public String getName() {
        return name();
    }

    public static List<String> getMRVersions() {
        List<String> versions = new ArrayList<String>();
        EMRVersion[] values = values();
        for (EMRVersion version : values) {
            versions.add(version.getName());
        }

        return versions;
    }

}
