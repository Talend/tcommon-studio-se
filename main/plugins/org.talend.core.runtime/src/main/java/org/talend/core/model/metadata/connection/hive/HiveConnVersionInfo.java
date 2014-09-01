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
package org.talend.core.model.metadata.connection.hive;

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

    HORTONWORKS(0, "HORTONWORKS", "HortonWorks", true), //$NON-NLS-1$//$NON-NLS-2$

    APACHE(0, "APACHE", "Apache", false), //$NON-NLS-1$//$NON-NLS-2$

    CLOUDERA(0, "CLOUDERA", "Cloudera", false), //$NON-NLS-1$//$NON-NLS-2$

    MAPR(0, "MAPR", "MapR", false), //$NON-NLS-1$//$NON-NLS-2$

    AMAZON_EMR(0, "AMAZON_EMR", "Amazon EMR", false), //$NON-NLS-1$//$NON-NLS-2$

    PIVOTAL_HD(0, "PIVOTAL_HD", "Pivotal HD", false), //$NON-NLS-1$ //$NON-NLS-2$

    DISTRO_CUSTOM(0, "CUSTOM", "Custom - Unsupported", false), //$NON-NLS-1$//$NON-NLS-2$

    HDP_2_1(1, "HDP_2_1", "Hortonworks Data Platform V2.1.0(Baikal)", true, false, true, true, HiveConnVersionInfo.HORTONWORKS), //$NON-NLS-1$//$NON-NLS-2$

    HDP_2_0(1, "HDP_2_0", "Hortonworks Data Platform V2.0.0(BigWheel)", true, false, true, true, HiveConnVersionInfo.HORTONWORKS), //$NON-NLS-1$//$NON-NLS-2$

    HDP_1_3(1, "HDP_1_3", "Hortonworks Data Platform V1.3.0(Condor)", true, true, HiveConnVersionInfo.HORTONWORKS), //$NON-NLS-1$//$NON-NLS-2$

    HDP_1_2(1, "HDP_1_2", "Hortonworks Data Platform V1.2.0(Bimota)", true, true, HiveConnVersionInfo.HORTONWORKS), //$NON-NLS-1$//$NON-NLS-2$

    HDP_1_0(1, "HDP_1_0", "Hortonworks Data Platform V1.0.0(deprecated)", true, HiveConnVersionInfo.HORTONWORKS), //$NON-NLS-1$//$NON-NLS-2$

    APACHE_1_0_0(1, "APACHE_1_0_0", "Apache 1.0.0 (Hive 0.9.0)", false, HiveConnVersionInfo.APACHE), //$NON-NLS-1$//$NON-NLS-2$

    APACHE_0_20_203(1, "APACHE_0_20_203", "Apache 0.20.203 (Hive 0.7.1)", false, HiveConnVersionInfo.APACHE), //$NON-NLS-1$//$NON-NLS-2$

    Cloudera_CDH5(1, "Cloudera_CDH5", "Cloudera CDH5", true, false, true, true, HiveConnVersionInfo.CLOUDERA), //$NON-NLS-1$//$NON-NLS-2$

    Cloudera_CDH4_YARN(1, "Cloudera_CDH4_YARN", "Cloudera CDH4 YARN", true, false, true, false, HiveConnVersionInfo.CLOUDERA), //$NON-NLS-1$//$NON-NLS-2$

    Cloudera_CDH4(1, "Cloudera_CDH4", "Cloudera CDH4", true, true, HiveConnVersionInfo.CLOUDERA), //$NON-NLS-1$//$NON-NLS-2$

    Cloudera_CDH3(1, "Cloudera_CDH3", "Cloudera CDH3(deprecated)", false, HiveConnVersionInfo.CLOUDERA), //$NON-NLS-1$//$NON-NLS-2$

    MAPR3_1_0(1, "MAPR310", "MapR 3.1.0", true, false, HiveConnVersionInfo.MAPR), //$NON-NLS-1$//$NON-NLS-2$

    MAPR3_0_1(1, "MAPR301", "MapR 3.0.1", true, false, HiveConnVersionInfo.MAPR), //$NON-NLS-1$//$NON-NLS-2$

    MAPR2_1_3(1, "MAPR213", "MapR 2.1.3", true, false, HiveConnVersionInfo.MAPR), //$NON-NLS-1$//$NON-NLS-2$

    MAPR2_1_2(1, "MAPR212", "MapR 2.1.2", false, HiveConnVersionInfo.MAPR), //$NON-NLS-1$//$NON-NLS-2$

    MAPR2(1, "MAPR2", "MapR 2.0.0", false, HiveConnVersionInfo.MAPR), //$NON-NLS-1$//$NON-NLS-2$

    MAPR1(1, "MAPR1", "MapR 1.2.0", false, HiveConnVersionInfo.MAPR), //$NON-NLS-1$//$NON-NLS-2$

    APACHE_1_0_3_EMR(1, "APACHE_1_0_3_EMR", "Apache 1.0.3 (Hive 0.8.1)", false, HiveConnVersionInfo.AMAZON_EMR), //$NON-NLS-1$//$NON-NLS-2$

    MapR_EMR(1, "MapR_EMR", "MapR 1.2.8(deprecated)", false, HiveConnVersionInfo.AMAZON_EMR), //$NON-NLS-1$//$NON-NLS-2$

    PIVOTAL_HD_2_0(1, "PIVOTAL_HD_2_0", "Pivotal HD 2.0", true, false, true, true, HiveConnVersionInfo.PIVOTAL_HD), //$NON-NLS-1$//$NON-NLS-2$

    PIVOTAL_HD_1_0_1(1, "PIVOTAL_HD_1_0_1", "Pivotal HD 1.0.1", false, false, true, false, HiveConnVersionInfo.PIVOTAL_HD), //$NON-NLS-1$//$NON-NLS-2$

    DISTRO_VERSION_CUSTOM(1, "DISTRO_VERSION_CUSTOM", "Customized Version", true, true, HiveConnVersionInfo.DISTRO_CUSTOM), //$NON-NLS-1$//$NON-NLS-2$

    MODE_EMBEDDED(2, "EMBEDDED",//$NON-NLS-1$
                  "Embedded",//$NON-NLS-1$
                  false,
                  HiveConnVersionInfo.HDP_1_0,
                  HiveConnVersionInfo.HDP_1_2,
                  HiveConnVersionInfo.HDP_1_3,
                  HiveConnVersionInfo.HDP_2_0,
                  HiveConnVersionInfo.HDP_2_1,
                  HiveConnVersionInfo.APACHE_1_0_0,
                  HiveConnVersionInfo.Cloudera_CDH4,
                  HiveConnVersionInfo.Cloudera_CDH4_YARN,
                  HiveConnVersionInfo.Cloudera_CDH5,
                  HiveConnVersionInfo.MAPR2,
                  HiveConnVersionInfo.MAPR2_1_2,
                  HiveConnVersionInfo.MAPR2_1_3,
                  HiveConnVersionInfo.MAPR3_0_1,
                  HiveConnVersionInfo.MAPR3_1_0,
                  HiveConnVersionInfo.APACHE_1_0_3_EMR,
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
                    HiveConnVersionInfo.APACHE_0_20_203,
                    HiveConnVersionInfo.APACHE_1_0_0,
                    HiveConnVersionInfo.Cloudera_CDH3,
                    HiveConnVersionInfo.Cloudera_CDH4,
                    HiveConnVersionInfo.Cloudera_CDH4_YARN,
                    HiveConnVersionInfo.Cloudera_CDH5,
                    HiveConnVersionInfo.MAPR1,
                    HiveConnVersionInfo.MAPR2,
                    HiveConnVersionInfo.MAPR2_1_2,
                    HiveConnVersionInfo.MAPR2_1_3,
                    HiveConnVersionInfo.MAPR3_0_1,
                    HiveConnVersionInfo.MAPR3_1_0,
                    HiveConnVersionInfo.MapR_EMR,
                    HiveConnVersionInfo.APACHE_1_0_3_EMR,
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
            HiveConnVersionInfo.HDP_2_1, HiveConnVersionInfo.HDP_2_0, HiveConnVersionInfo.PIVOTAL_HD_2_0 };

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
