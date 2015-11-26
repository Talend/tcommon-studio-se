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
package org.talend.core.model.metadata.connection.hive;

import org.talend.core.hadoop.version.EHadoopDistributions;
import org.talend.core.hadoop.version.EHadoopVersion4Drivers;

/**
 * This class provides the info for Hive. There are 3 levels as follows:
 *
 * <pre>
 * <li> 0 -- stands for "Distribution"
 * <li> 1 -- stands for "Distro version"
 * <li> 2 -- stands for "Hive mode"
 * </pre>
 *
 * The <code>key</code> of this enum is matched to the config file of component.
 *
 * @author Marvin Wang
 * @version 1.0 jdk1.6
 * @date Aug 9, 2012
 */
public enum HiveConnVersionInfo {

    HORTONWORKS(0, EHadoopDistributions.HORTONWORKS.getName(), EHadoopDistributions.HORTONWORKS.getDisplayName(), true),

    APACHE(0, EHadoopDistributions.APACHE.getName(), EHadoopDistributions.APACHE.getDisplayName(), false),

    CLOUDERA(0, EHadoopDistributions.CLOUDERA.getName(), EHadoopDistributions.CLOUDERA.getDisplayName(), false),

    MAPR(0, EHadoopDistributions.MAPR.getName(), EHadoopDistributions.MAPR.getDisplayName(), false),

    AMAZON_EMR(0, EHadoopDistributions.AMAZON_EMR.getName(), EHadoopDistributions.AMAZON_EMR.getDisplayName(), false),

    PIVOTAL_HD(0, EHadoopDistributions.PIVOTAL_HD.getName(), EHadoopDistributions.PIVOTAL_HD.getDisplayName(), false),

    DISTRO_CUSTOM(0, EHadoopDistributions.CUSTOM.getName(), EHadoopDistributions.CUSTOM.getDisplayName(), false),

    HDP_2_3(
            1,
            EHadoopVersion4Drivers.HDP_2_3.getVersionValue(),
            EHadoopVersion4Drivers.HDP_2_3.getVersionDisplay(),
            true,
            false,
            true,
            true,
            HiveConnVersionInfo.HORTONWORKS),

    HDP_2_2(
            1,
            EHadoopVersion4Drivers.HDP_2_2.getVersionValue(),
            EHadoopVersion4Drivers.HDP_2_2.getVersionDisplay(),
            true,
            false,
            true,
            true,
            HiveConnVersionInfo.HORTONWORKS),

    HDP_2_1(
            1,
            EHadoopVersion4Drivers.HDP_2_1.getVersionValue(),
            EHadoopVersion4Drivers.HDP_2_1.getVersionDisplay(),
            true,
            false,
            true,
            true,
            HiveConnVersionInfo.HORTONWORKS),

    HDP_2_0(
            1,
            EHadoopVersion4Drivers.HDP_2_0.getVersionValue(),
            EHadoopVersion4Drivers.HDP_2_0.getVersionDisplay(),
            true,
            false,
            true,
            true,
            HiveConnVersionInfo.HORTONWORKS),

    HDP_1_3(
            1,
            EHadoopVersion4Drivers.HDP_1_3.getVersionValue(),
            EHadoopVersion4Drivers.HDP_1_3.getVersionDisplay(),
            true,
            true,
            HiveConnVersionInfo.HORTONWORKS),

    HDP_1_2(
            1,
            EHadoopVersion4Drivers.HDP_1_2.getVersionValue(),
            EHadoopVersion4Drivers.HDP_1_2.getVersionDisplay(),
            true,
            true,
            HiveConnVersionInfo.HORTONWORKS),

    APACHE_1_0_0(1, EHadoopVersion4Drivers.APACHE_1_0_0.getVersionValue(), EHadoopVersion4Drivers.APACHE_1_0_0
            .getVersionDisplay(), false, HiveConnVersionInfo.APACHE),

    APACHE_0_20_203(1, EHadoopVersion4Drivers.APACHE_0_20_203.getVersionValue(), EHadoopVersion4Drivers.APACHE_0_20_203
            .getVersionDisplay(), false, HiveConnVersionInfo.APACHE),

    Cloudera_CDH5_5(1, EHadoopVersion4Drivers.CLOUDERA_CDH5_5.getVersionValue(), EHadoopVersion4Drivers.CLOUDERA_CDH5_5
            .getVersionDisplay(), true, false, true, true, HiveConnVersionInfo.CLOUDERA),

    Cloudera_CDH5_4(1, EHadoopVersion4Drivers.CLOUDERA_CDH5_4.getVersionValue(), EHadoopVersion4Drivers.CLOUDERA_CDH5_4
            .getVersionDisplay(), true, false, true, true, HiveConnVersionInfo.CLOUDERA),

    Cloudera_CDH5_1(1, EHadoopVersion4Drivers.CLOUDERA_CDH5_1.getVersionValue(), EHadoopVersion4Drivers.CLOUDERA_CDH5_1
            .getVersionDisplay(), true, false, true, true, HiveConnVersionInfo.CLOUDERA),

    Cloudera_CDH5_1_MR1(
                        1,
                        EHadoopVersion4Drivers.CLOUDERA_CDH5_1_MR1.getVersionValue(),
                        EHadoopVersion4Drivers.CLOUDERA_CDH5_1_MR1.getVersionDisplay(),
                        true,
                        false,
                        true,
                        true,
                        HiveConnVersionInfo.CLOUDERA),

    Cloudera_CDH5(1, EHadoopVersion4Drivers.CLOUDERA_CDH5.getVersionValue(), EHadoopVersion4Drivers.CLOUDERA_CDH5
            .getVersionDisplay(), true, false, true, true, HiveConnVersionInfo.CLOUDERA),

    Cloudera_CDH4_YARN(1, EHadoopVersion4Drivers.CLOUDERA_CDH4_YARN.getVersionValue(), EHadoopVersion4Drivers.CLOUDERA_CDH4_YARN
            .getVersionDisplay(), true, false, true, false, HiveConnVersionInfo.CLOUDERA),

    Cloudera_CDH4(1, EHadoopVersion4Drivers.CLOUDERA_CDH4.getVersionValue(), EHadoopVersion4Drivers.CLOUDERA_CDH4
            .getVersionDisplay(), true, true, HiveConnVersionInfo.CLOUDERA),

    MAPR5_0_0(
              1,
              EHadoopVersion4Drivers.MAPR500.getVersionValue(),
              EHadoopVersion4Drivers.MAPR500.getVersionDisplay(),
              true,
              false,
              HiveConnVersionInfo.MAPR),

    MAPR4_1_0(
              1,
              EHadoopVersion4Drivers.MAPR410.getVersionValue(),
              EHadoopVersion4Drivers.MAPR410.getVersionDisplay(),
              true,
              false,
              HiveConnVersionInfo.MAPR),

    MAPR4_0_1(
              1,
              EHadoopVersion4Drivers.MAPR401.getVersionValue(),
              EHadoopVersion4Drivers.MAPR401.getVersionDisplay(),
              true,
              false,
              HiveConnVersionInfo.MAPR),

    MAPR3_1_0(
              1,
              EHadoopVersion4Drivers.MAPR310.getVersionValue(),
              EHadoopVersion4Drivers.MAPR310.getVersionDisplay(),
              true,
              false,
              HiveConnVersionInfo.MAPR),

    MAPR3_0_1(
              1,
              EHadoopVersion4Drivers.MAPR301.getVersionValue(),
              EHadoopVersion4Drivers.MAPR301.getVersionDisplay(),
              true,
              false,
              HiveConnVersionInfo.MAPR),

    MAPR2_1_3(
              1,
              EHadoopVersion4Drivers.MAPR213.getVersionValue(),
              EHadoopVersion4Drivers.MAPR213.getVersionDisplay(),
              true,
              false,
              HiveConnVersionInfo.MAPR),

    MAPR2_1_2(
              1,
              EHadoopVersion4Drivers.MAPR212.getVersionValue(),
              EHadoopVersion4Drivers.MAPR212.getVersionDisplay(),
              false,
              HiveConnVersionInfo.MAPR),

    MAPR2(
          1,
          EHadoopVersion4Drivers.MAPR2.getVersionValue(),
          EHadoopVersion4Drivers.MAPR2.getVersionDisplay(),
          false,
          HiveConnVersionInfo.MAPR),

    MAPR1(
          1,
          EHadoopVersion4Drivers.MAPR1.getVersionValue(),
          EHadoopVersion4Drivers.MAPR1.getVersionDisplay(),
          false,
          HiveConnVersionInfo.MAPR),

    EMR_4_0_0(1, EHadoopVersion4Drivers.EMR_4_0_0.getVersionValue(), "EMR 4.0.0 (Hive 1.0.0)", //$NON-NLS-1$
              true,
              false,
              HiveConnVersionInfo.AMAZON_EMR),

    APACHE_2_4_0_EMR_0_13_1(1, "APACHE_2_4_0_EMR_0_13_1", "Apache 2.4.0 (Hive 0.13.1)", false, HiveConnVersionInfo.AMAZON_EMR), //$NON-NLS-1$ //$NON-NLS-2$

    APACHE_2_4_0_EMR(
                     1,
                     EHadoopVersion4Drivers.APACHE_2_4_0_EMR.getVersionValue(),
                     "Apache 2.4.0 (Hive 0.11.0)", false, HiveConnVersionInfo.AMAZON_EMR), //$NON-NLS-1$

    APACHE_1_0_3_EMR(
                     1,
                     EHadoopVersion4Drivers.APACHE_1_0_3_EMR.getVersionValue(),
                     "Apache 1.0.3 (Hive 0.8.1)", false, HiveConnVersionInfo.AMAZON_EMR), //$NON-NLS-1$

    PIVOTAL_HD_2_0(1, EHadoopVersion4Drivers.PIVOTAL_HD_2_0.getVersionValue(), EHadoopVersion4Drivers.PIVOTAL_HD_2_0
            .getVersionDisplay(), true, false, true, true, HiveConnVersionInfo.PIVOTAL_HD),

    PIVOTAL_HD_1_0_1(1, EHadoopVersion4Drivers.PIVOTAL_HD_1_0_1.getVersionValue(), EHadoopVersion4Drivers.PIVOTAL_HD_1_0_1
            .getVersionDisplay(), false, false, true, false, HiveConnVersionInfo.PIVOTAL_HD),

    DISTRO_VERSION_CUSTOM(1, "DISTRO_VERSION_CUSTOM", "Customized Version", true, true, HiveConnVersionInfo.DISTRO_CUSTOM), //$NON-NLS-1$//$NON-NLS-2$

    MODE_EMBEDDED(2, "EMBEDDED",//$NON-NLS-1$
                  "Embedded",//$NON-NLS-1$
                  false,
                  HiveConnVersionInfo.HDP_1_2,
                  HiveConnVersionInfo.HDP_1_3,
                  HiveConnVersionInfo.HDP_2_0,
                  HiveConnVersionInfo.HDP_2_1,
                  HiveConnVersionInfo.HDP_2_2,
                  // HiveConnVersionInfo.HDP_2_3,
                  HiveConnVersionInfo.APACHE_1_0_0,
                  HiveConnVersionInfo.Cloudera_CDH4,
                  HiveConnVersionInfo.Cloudera_CDH4_YARN,
                  HiveConnVersionInfo.Cloudera_CDH5,
                  HiveConnVersionInfo.Cloudera_CDH5_1,
                  HiveConnVersionInfo.Cloudera_CDH5_1_MR1,
                  HiveConnVersionInfo.Cloudera_CDH5_4,
                  HiveConnVersionInfo.MAPR2,
                  HiveConnVersionInfo.MAPR2_1_2,
                  HiveConnVersionInfo.MAPR2_1_3,
                  HiveConnVersionInfo.MAPR3_0_1,
                  HiveConnVersionInfo.MAPR3_1_0,
                  HiveConnVersionInfo.MAPR4_0_1,
                  HiveConnVersionInfo.MAPR4_1_0,
                  // HiveConnVersionInfo.MAPR5_0_0,
                  HiveConnVersionInfo.APACHE_1_0_3_EMR,
                  HiveConnVersionInfo.APACHE_2_4_0_EMR,
                  HiveConnVersionInfo.PIVOTAL_HD_1_0_1,
                  HiveConnVersionInfo.PIVOTAL_HD_2_0,
                  HiveConnVersionInfo.DISTRO_VERSION_CUSTOM),

    MODE_STANDALONE(2, "STANDALONE",//$NON-NLS-1$
                    "Standalone",//$NON-NLS-1$
                    false,
                    HiveConnVersionInfo.HDP_1_2,
                    HiveConnVersionInfo.HDP_1_3,
                    HiveConnVersionInfo.HDP_2_0,
                    HiveConnVersionInfo.HDP_2_1,
                    HiveConnVersionInfo.HDP_2_2,
                    HiveConnVersionInfo.HDP_2_3,
                    HiveConnVersionInfo.APACHE_0_20_203,
                    HiveConnVersionInfo.APACHE_1_0_0,
                    HiveConnVersionInfo.Cloudera_CDH4,
                    HiveConnVersionInfo.Cloudera_CDH4_YARN,
                    HiveConnVersionInfo.Cloudera_CDH5,
                    HiveConnVersionInfo.Cloudera_CDH5_1,
                    HiveConnVersionInfo.Cloudera_CDH5_1_MR1,
                    HiveConnVersionInfo.Cloudera_CDH5_4,
                    HiveConnVersionInfo.Cloudera_CDH5_5,
                    HiveConnVersionInfo.MAPR1,
                    HiveConnVersionInfo.MAPR2,
                    HiveConnVersionInfo.MAPR2_1_2,
                    HiveConnVersionInfo.MAPR2_1_3,
                    HiveConnVersionInfo.MAPR3_0_1,
                    HiveConnVersionInfo.MAPR3_1_0,
                    HiveConnVersionInfo.MAPR4_0_1,
                    HiveConnVersionInfo.MAPR4_1_0,
                    HiveConnVersionInfo.MAPR5_0_0,
                    HiveConnVersionInfo.APACHE_1_0_3_EMR,
                    HiveConnVersionInfo.APACHE_2_4_0_EMR,
                    HiveConnVersionInfo.APACHE_2_4_0_EMR_0_13_1,
                    HiveConnVersionInfo.EMR_4_0_0,
                    HiveConnVersionInfo.PIVOTAL_HD_1_0_1,
                    HiveConnVersionInfo.PIVOTAL_HD_2_0,
                    HiveConnVersionInfo.DISTRO_VERSION_CUSTOM);

    private int level;

    private String key;// The key is mapped to item name of component in t*_java.xml file.

    private String displayName;// That is mapped to the property of component in t*_messages.properties file.

    private HiveConnVersionInfo[] follows;// That stores all are followed by the current object.

    private boolean isSupportHive2; // Till now only MapR2, CDH4, and HDP1.2 support hive server2.

    private boolean isSupportMR1;

    private boolean isSupportYARN;

    private boolean supportSecurity;

    private static HiveConnVersionInfo[] hiveVersions = new HiveConnVersionInfo[] { HiveConnVersionInfo.Cloudera_CDH5,
            HiveConnVersionInfo.Cloudera_CDH5_1, HiveConnVersionInfo.Cloudera_CDH5_1_MR1, HiveConnVersionInfo.Cloudera_CDH5_4,
            HiveConnVersionInfo.Cloudera_CDH5_5, HiveConnVersionInfo.HDP_2_3, HiveConnVersionInfo.HDP_2_2,
            HiveConnVersionInfo.HDP_2_1, HiveConnVersionInfo.HDP_2_0, HiveConnVersionInfo.PIVOTAL_HD_2_0,
            HiveConnVersionInfo.APACHE_2_4_0_EMR, HiveConnVersionInfo.APACHE_2_4_0_EMR_0_13_1, HiveConnVersionInfo.EMR_4_0_0 };

    private static HiveConnVersionInfo[] hiveVersionsSupportingTez = new HiveConnVersionInfo[] { HiveConnVersionInfo.HDP_2_2,
            HiveConnVersionInfo.HDP_2_1, HiveConnVersionInfo.MAPR4_0_1, HiveConnVersionInfo.MAPR4_1_0,
            HiveConnVersionInfo.MAPR5_0_0, HiveConnVersionInfo.DISTRO_VERSION_CUSTOM };

    private HiveConnVersionInfo(int level, String key, String displayName, boolean supportSecurity,
            HiveConnVersionInfo... follows) {
        this(level, key, displayName, false, supportSecurity, follows);
    }

    private HiveConnVersionInfo(int level, String key, String displayName, boolean isSupportHive2, boolean supportSecurity,
            HiveConnVersionInfo... follows) {
        this(level, key, displayName, isSupportHive2, true, false, supportSecurity, follows);
    }

    private HiveConnVersionInfo(int level, String key, String displayName, boolean isSupportHive2, boolean isSupportMR1,
            boolean isSupportYARN, boolean supportSecurity, HiveConnVersionInfo... follows) {
        this.level = level;
        this.key = key;
        this.displayName = displayName;
        this.isSupportHive2 = isSupportHive2;
        this.isSupportMR1 = isSupportMR1;
        this.isSupportYARN = isSupportYARN;
        this.follows = follows;
        this.supportSecurity = supportSecurity;
    }

    public int getLevel() {
        return this.level;
    }

    public String getKey() {
        return this.key;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public HiveConnVersionInfo[] getFollows() {
        return this.follows;
    }

    /**
     * Getter for isSupportHive2.
     *
     * @return the isSupportHive2
     */
    public boolean isSupportHive2() {
        return this.isSupportHive2;
    }

    public boolean isSupportHiveServerVersion(HiveServerVersionInfo hiveServerVersion) {
        boolean isSupport = false;

        if (hiveServerVersion == null) {
            return isSupport;
        }

        switch (hiveServerVersion) {
        case HIVE_SERVER_1:
            switch (this) {
            // make them same with components
            case HDP_2_3:
            case EMR_4_0_0:
            case Cloudera_CDH5_4:
            case Cloudera_CDH5_5:
            case MAPR4_1_0:
            case MAPR5_0_0:
                isSupport = false;
                break;
            default:
                isSupport = true;
            }
            break;
        case HIVE_SERVER_2:
            isSupport = isSupportHive2();
            break;
        default:
            isSupport = false;
        }

        return isSupport;
    }

    public boolean isSupportMR1() {
        return this.isSupportMR1;
    }

    public boolean isSupportYARN() {
        return this.isSupportYARN;
    }

    public boolean isSupportSecurity() {
        return this.supportSecurity;
    }

    public static HiveConnVersionInfo[] getHiveVersionsNotSupportOnWindows() {
        return hiveVersions;
    }

    public static HiveConnVersionInfo[] getHiveVersionsSupportingTez() {
        return hiveVersionsSupportingTez;
    }

    public static HiveConnVersionInfo getVersionByKey(String key) {
        HiveConnVersionInfo[] values = values();
        for (HiveConnVersionInfo version : values) {
            if (version.getLevel() == 1 && version.getKey().equals(key)) {
                return version;
            }
        }

        return null;
    }

}
