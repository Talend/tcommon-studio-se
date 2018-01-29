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

/**
 * DOC ycbai class global comment. Detailled comment
 * 
 * @deprecated @link HadoopComponent via osgi service instead.
 */
public enum EHadoopDistributions {

    HORTONWORKS("HortonWorks"), //$NON-NLS-1$

    CLOUDERA("Cloudera"), //$NON-NLS-1$

    ALTUS("Altus"), //$NON-NLS-1$

    MAPR("MapR"), //$NON-NLS-1$

    APACHE("Apache"), //$NON-NLS-1$

    AMAZON_EMR("Amazon EMR"), //$NON-NLS-1$

    PIVOTAL_HD("Pivotal HD"), //$NON-NLS-1$

    MICROSOFT_HD_INSIGHT("Microsoft HD Insight"), //$NON-NLS-1$

    GOOGLE_CLOUD_DATAPROC("Google Cloud Dataproc"), //$NON-NLS-1$

    CUSTOM("Custom - Unsupported"); //$NON-NLS-1$

    private String displayName;

    EHadoopDistributions(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name();
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static EHadoopDistributions getDistributionByName(String name, boolean display) {
        if (name != null) {
            for (EHadoopDistributions distribution : values()) {
                if (display) {
                    if (name.equalsIgnoreCase(distribution.getDisplayName())) {
                        return distribution;
                    }
                } else {
                    if (name.equalsIgnoreCase(distribution.getName())) {
                        return distribution;
                    }
                }
            }
        }
        return null;
    }

}
