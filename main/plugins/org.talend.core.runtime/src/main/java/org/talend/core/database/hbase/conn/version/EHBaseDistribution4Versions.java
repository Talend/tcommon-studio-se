// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.database.hbase.conn.version;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.hadoop.version.EHadoopVersion4Drivers;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public enum EHBaseDistribution4Versions {
    HDP_2_3(EHBaseDistributions.HORTONWORKS, EHadoopVersion4Drivers.HDP_2_3.getVersionDisplay(), EHadoopVersion4Drivers.HDP_2_3
            .getVersionValue()),

    HDP_2_2(EHBaseDistributions.HORTONWORKS, EHadoopVersion4Drivers.HDP_2_2.getVersionDisplay(), EHadoopVersion4Drivers.HDP_2_2
            .getVersionValue()),

    HDP_2_1(EHBaseDistributions.HORTONWORKS, EHadoopVersion4Drivers.HDP_2_1.getVersionDisplay(), EHadoopVersion4Drivers.HDP_2_1
            .getVersionValue()),

    HDP_2_0(EHBaseDistributions.HORTONWORKS, EHadoopVersion4Drivers.HDP_2_0.getVersionDisplay(), EHadoopVersion4Drivers.HDP_2_0
            .getVersionValue()),

    HDP_1_3(EHBaseDistributions.HORTONWORKS, EHadoopVersion4Drivers.HDP_1_3.getVersionDisplay(), EHadoopVersion4Drivers.HDP_1_3
            .getVersionValue()),

    HDP_1_2(EHBaseDistributions.HORTONWORKS, EHadoopVersion4Drivers.HDP_1_2.getVersionDisplay(), EHadoopVersion4Drivers.HDP_1_2
            .getVersionValue()),

    APACHE_0_20_203(
                    EHBaseDistributions.APACHE,
                    EHadoopVersion4Drivers.APACHE_0_20_203.getVersionDisplay(),
                    EHadoopVersion4Drivers.APACHE_0_20_203.getVersionValue()),

    APACHE_2_4_0_EMR(
                     EHBaseDistributions.AMAZON_EMR,
                     EHadoopVersion4Drivers.APACHE_2_4_0_EMR.getVersionDisplay(),
                     EHadoopVersion4Drivers.APACHE_2_4_0_EMR.getVersionValue()),

    APACHE_1_0_3_EMR(
                     EHBaseDistributions.AMAZON_EMR,
                     EHadoopVersion4Drivers.APACHE_1_0_3_EMR.getVersionDisplay(),
                     EHadoopVersion4Drivers.APACHE_1_0_3_EMR.getVersionValue()),

    APACHE_1_0_0(
                 EHBaseDistributions.APACHE,
                 EHadoopVersion4Drivers.APACHE_1_0_0.getVersionDisplay(),
                 EHadoopVersion4Drivers.APACHE_1_0_0.getVersionValue()),

    CLOUDERA_CDH5_5(
                    EHBaseDistributions.CLOUDERA,
                    EHadoopVersion4Drivers.CLOUDERA_CDH5_5.getVersionDisplay(),
                    EHadoopVersion4Drivers.CLOUDERA_CDH5_5.getVersionValue()),

    CLOUDERA_CDH5_4(
                    EHBaseDistributions.CLOUDERA,
                    EHadoopVersion4Drivers.CLOUDERA_CDH5_4.getVersionDisplay(),
                    EHadoopVersion4Drivers.CLOUDERA_CDH5_4.getVersionValue()),

    CLOUDERA_CDH5_1(
                    EHBaseDistributions.CLOUDERA,
                    EHadoopVersion4Drivers.CLOUDERA_CDH5_1.getVersionDisplay(),
                    EHadoopVersion4Drivers.CLOUDERA_CDH5_1.getVersionValue()),

    CLOUDERA_CDH5_1_MR1(
                        EHBaseDistributions.CLOUDERA,
                        EHadoopVersion4Drivers.CLOUDERA_CDH5_1_MR1.getVersionDisplay(),
                        EHadoopVersion4Drivers.CLOUDERA_CDH5_1_MR1.getVersionValue()),

    CLOUDERA_CDH5(
                  EHBaseDistributions.CLOUDERA,
                  EHadoopVersion4Drivers.CLOUDERA_CDH5.getVersionDisplay(),
                  EHadoopVersion4Drivers.CLOUDERA_CDH5.getVersionValue()),

    CLOUDERA_CDH4_YARN(
                       EHBaseDistributions.CLOUDERA,
                       EHadoopVersion4Drivers.CLOUDERA_CDH4_YARN.getVersionDisplay(),
                       EHadoopVersion4Drivers.CLOUDERA_CDH4_YARN.getVersionValue()),

    CLOUDERA_CDH4(
                  EHBaseDistributions.CLOUDERA,
                  EHadoopVersion4Drivers.CLOUDERA_CDH4.getVersionDisplay(),
                  EHadoopVersion4Drivers.CLOUDERA_CDH4.getVersionValue()),

    MAPR_5_0_0(EHBaseDistributions.MAPR, EHadoopVersion4Drivers.MAPR500.getVersionDisplay(), EHadoopVersion4Drivers.MAPR500
            .getVersionValue()),

    MAPR_4_1_0(EHBaseDistributions.MAPR, EHadoopVersion4Drivers.MAPR410.getVersionDisplay(), EHadoopVersion4Drivers.MAPR410
            .getVersionValue()),

    MAPR_4_0_1(EHBaseDistributions.MAPR, EHadoopVersion4Drivers.MAPR401.getVersionDisplay(), EHadoopVersion4Drivers.MAPR401
            .getVersionValue()),

    MAPR_3_1_0(EHBaseDistributions.MAPR, EHadoopVersion4Drivers.MAPR310.getVersionDisplay(), EHadoopVersion4Drivers.MAPR310
            .getVersionValue()),

    MAPR_3_0_1(EHBaseDistributions.MAPR, EHadoopVersion4Drivers.MAPR301.getVersionDisplay(), EHadoopVersion4Drivers.MAPR301
            .getVersionValue()),

    MAPR_2_1_3(EHBaseDistributions.MAPR, EHadoopVersion4Drivers.MAPR213.getVersionDisplay(), EHadoopVersion4Drivers.MAPR213
            .getVersionValue()),

    MAPR_2_1_2(EHBaseDistributions.MAPR, EHadoopVersion4Drivers.MAPR212.getVersionDisplay(), EHadoopVersion4Drivers.MAPR212
            .getVersionValue()),

    MAPR(EHBaseDistributions.MAPR, EHadoopVersion4Drivers.MAPR2.getVersionDisplay(), EHadoopVersion4Drivers.MAPR2
            .getVersionValue()),

    PIVOTAL_HD_2_0(
                   EHBaseDistributions.PIVOTAL_HD,
                   EHadoopVersion4Drivers.PIVOTAL_HD_2_0.getVersionDisplay(),
                   EHadoopVersion4Drivers.PIVOTAL_HD_2_0.getVersionValue()),

    PIVOTAL_HD_1_0_1(
                     EHBaseDistributions.PIVOTAL_HD,
                     EHadoopVersion4Drivers.PIVOTAL_HD_1_0_1.getVersionDisplay(),
                     EHadoopVersion4Drivers.PIVOTAL_HD_1_0_1.getVersionValue()),

    CUSTOM(EHBaseDistributions.CUSTOM, EHadoopVersion4Drivers.CUSTOM.getVersionDisplay(), EHadoopVersion4Drivers.CUSTOM
            .getVersionValue());

    private EHBaseDistributions distribution;

    private String versionDisplayName;

    private String versionValue;

    EHBaseDistribution4Versions(EHBaseDistributions distribution, String versionDisplayName, String versionValue) {
        this.distribution = distribution;
        this.versionDisplayName = versionDisplayName;
        this.versionValue = versionValue;
    }

    public static List<EHBaseDistribution4Versions> indexOfByDistribution(String distribution) {
        List<EHBaseDistribution4Versions> distribution4Versions = new ArrayList<EHBaseDistribution4Versions>();
        if (distribution != null) {
            for (EHBaseDistribution4Versions d4v : EHBaseDistribution4Versions.values()) {
                if (d4v.getDistribution().getName().equals(distribution)
                        || d4v.getDistribution().getDisplayName().equals(distribution)) {
                    distribution4Versions.add(d4v);
                }
            }
        }
        return distribution4Versions;
    }

    public static EHBaseDistribution4Versions indexOfByVersionDisplay(String displayName) {
        return indexOf(displayName, true);
    }

    public static EHBaseDistribution4Versions indexOfByVersion(String value) {
        return indexOf(value, false);
    }

    private static EHBaseDistribution4Versions indexOf(String name, boolean display) {
        if (name != null) {
            for (EHBaseDistribution4Versions version : EHBaseDistribution4Versions.values()) {
                if (display) {
                    if (name.equalsIgnoreCase(version.getVersionDisplayName())) {
                        return version;
                    }
                } else {
                    if (name.equalsIgnoreCase(version.getVersionValue())) {
                        return version;
                    }
                }
            }
        }
        return null;
    }

    public static List<String> getHadoopDistributionVersions(String distribution) {
        return getHadoopDistributionVersions(distribution, true);
    }

    public static List<String> getHadoopDistributionVersions(String distribution, boolean display) {
        List<String> result = new ArrayList<String>();
        List<EHBaseDistribution4Versions> d4vList = EHBaseDistribution4Versions.indexOfByDistribution(distribution);
        for (EHBaseDistribution4Versions d4v : d4vList) {
            if (display) {
                result.add(d4v.getVersionDisplayName());
            } else {
                result.add(d4v.getVersionValue());
            }
        }
        return result;
    }

    public EHBaseDistributions getDistribution() {
        return this.distribution;
    }

    public String getVersionDisplayName() {
        return this.versionDisplayName;
    }

    public String getVersionValue() {
        return this.versionValue;
    }

}
