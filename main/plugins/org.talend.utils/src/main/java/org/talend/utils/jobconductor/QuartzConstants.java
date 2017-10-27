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
package org.talend.utils.jobconductor;

/**
 * 
 * DOC amaumont class global comment. Detailled comment
 */
public final class QuartzConstants {

    private QuartzConstants() {
        super();
    }

    public static final String GROUP_JOB_CONDUCTOR = "JOB_CONDUCTOR";

    public static final String GROUP_SYSTEM = "SYSTEM";

    public static final String GROUP_DATABASE_SVN_BACKUP = "DATABASE_SVN_BACKUP";

    public static boolean equals(String group1, String group2) {
        return group1 == group2 || group1 != null && group1.equals(group2) || group1 == null && "DEFAULT".equals(group2)
                || "DEFAULT".equals(group1) && group2 == null;
    }

}
