// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.hadoop.conf;

/**
 * created by ycbai on Jul 31, 2014 Detailled comment
 *
 */
public enum EHadoopProperties {

    NAMENODE_URI,

    JOBTRACKER,

    RESOURCE_MANAGER,

    NAMENODE_PRINCIPAL,

    HIVE_PRINCIPAL,

    DATABASE,

    PORT,

    ;

    public String getName() {
        return this.name();
    }

}
