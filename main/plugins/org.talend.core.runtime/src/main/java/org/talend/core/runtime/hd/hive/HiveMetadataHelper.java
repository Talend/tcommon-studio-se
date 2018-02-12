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
package org.talend.core.runtime.hd.hive;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.GlobalServiceRegister;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.hadoop.IHadoopDistributionService;
import org.talend.core.model.metadata.connection.hive.HiveModeInfo;
import org.talend.core.model.metadata.connection.hive.HiveServerVersionInfo;
import org.talend.core.runtime.hd.IDistributionsManager;
import org.talend.core.runtime.hd.IHDistribution;
import org.talend.core.runtime.hd.IHDistributionVersion;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class HiveMetadataHelper {

    private static IHadoopDistributionService getHadoopDistributionService() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IHadoopDistributionService.class)) {
            return (IHadoopDistributionService) GlobalServiceRegister.getDefault().getService(IHadoopDistributionService.class);
        }
        return null;
    }

    private static IDistributionsManager getHiveDistributionsManager() {
        IHadoopDistributionService hadoopDistributionService = getHadoopDistributionService();
        if (hadoopDistributionService != null) {
            return hadoopDistributionService.getHiveDistributionManager();
        }
        return null;
    }

    public static String[] getDistributionsDisplay() {
        List<String> distributionItems = new ArrayList<String>();
        IDistributionsManager hiveDistributionManager = getHiveDistributionsManager();
        if (hiveDistributionManager != null) {
            IHDistribution[] distributions = hiveDistributionManager.getDistributions();
            for (IHDistribution d : distributions) {
                if (!d.useCustom()) {// custom should be add
                    String[] distributionVersionsDisplay = getDistributionVersionsDisplay(d.getName(), false);
                    if (distributionVersionsDisplay == null || distributionVersionsDisplay.length == 0) {
                        continue; // if no version support, ignore it.
                    }
                }
                distributionItems.add(d.getDisplayName());
            }
        }
        return distributionItems.toArray(new String[distributionItems.size()]);
    }

    public static IHDistribution getDistribution(String hiveDistribution, boolean byDisplay) {
        return getDistribution(hiveDistribution, byDisplay, false);
    }

    public static IHDistribution getDistribution(String hiveDistribution, boolean byDisplay, boolean withDefault) {
        IDistributionsManager hiveDistributionManager = getHiveDistributionsManager();
        if (hiveDistributionManager != null && hiveDistribution != null) {
            IHDistribution distribution = hiveDistributionManager.getDistribution(hiveDistribution, byDisplay);
            if (withDefault && distribution == null) {
                IHDistribution[] distributions = hiveDistributionManager.getDistributions();
                if (distributions.length > 0) {
                    distribution = distributions[0];
                }
            }
            return distribution;
        }
        return null;
    }

    public static String[] getDistributionVersionsDisplay(String hiveDistribution, boolean byDisplay) {
        List<String> versionsItems = new ArrayList<String>();
        IHDistribution distribution = getDistribution(hiveDistribution, byDisplay);
        if (distribution != null) {
            IHDistributionVersion[] hdVersions = distribution.getHDVersions();
            for (IHDistributionVersion v : hdVersions) {
                String[] hiveModesDisplay = getHiveModesDisplay(distribution.getName(), v.getVersion(), null, false);
                if (hiveModesDisplay == null || hiveModesDisplay.length == 0) {
                    continue; // if no hive mode to support, ignore this version?
                }
                // String[] hiveServersDisplay = getHiveServersDisplay(distribution.getName(), v.getVersion(), false);
                // if (hiveServersDisplay == null || hiveServersDisplay.length == 0) {
                // continue; // if no hive server to support, ignore this version?
                // }
                String displayVersion = v.getDisplayVersion();
                if (displayVersion != null) {
                    versionsItems.add(displayVersion);
                }
            }
            // versionsItems.addAll(Arrays.asList(distribution.getVersionsDisplay()));
        }
        return versionsItems.toArray(new String[versionsItems.size()]);
    }

    public static String[] getHiveModesDisplay(String hiveDistribution, String hiveVersion, String hiveServer, boolean byDisplay) {
        List<String> hiveModeItems = new ArrayList<String>();
        if (doSupportEmbeddedMode(hiveDistribution, hiveVersion, byDisplay)) {
            hiveModeItems.add(HiveModeInfo.EMBEDDED.getDisplayName());
        }
        if (doSupportStandaloneMode(hiveDistribution, hiveVersion, byDisplay)) {
            HiveServerVersionInfo mode = byDisplay ? HiveServerVersionInfo.getByDisplay(hiveServer) : HiveServerVersionInfo
                    .getByKey(hiveServer);
            if (mode == null) {
                String[] hiveServersDisplay = getHiveServersDisplay(hiveDistribution, hiveVersion, byDisplay);
                if (hiveServersDisplay != null && hiveServersDisplay.length > 0) {
                    mode = HiveServerVersionInfo.getByDisplay(hiveServersDisplay[0]);
                }
            }

            /*
             * According to the tHiveXXX component to set it
             */
            if (mode == null // add always?
                    || mode == HiveServerVersionInfo.HIVE_SERVER_2 // server 2
                    || (HiveServerVersionInfo.HIVE_SERVER_1 == mode && doSupportMethod(hiveDistribution, hiveVersion, byDisplay,
                            "doSupportHive1Standalone"))) {//$NON-NLS-1$
                hiveModeItems.add(HiveModeInfo.STANDALONE.getDisplayName());
            }
        }
        return hiveModeItems.toArray(new String[0]);
    }

    public static boolean isHiveEmbeddedMode(String dbTypeDisplay, String hiveModeDisplay) {
        if (dbTypeDisplay != null && hiveModeDisplay != null
                && EDatabaseTypeName.HIVE.getDisplayName().equalsIgnoreCase(dbTypeDisplay)) {
            return HiveModeInfo.getByDisplay(hiveModeDisplay) == HiveModeInfo.EMBEDDED;
        }
        return false;
    }

    public static boolean isHiveStandaloneMode(String dbTypeDisplay, String hiveModeDisplay) {
        if (dbTypeDisplay != null && hiveModeDisplay != null
                && EDatabaseTypeName.HIVE.getDisplayName().equalsIgnoreCase(dbTypeDisplay)) {
            return HiveModeInfo.getByDisplay(hiveModeDisplay) == HiveModeInfo.STANDALONE;
        }
        return false;
    }

    public static String[] getHiveServersDisplay(String hiveDistribution, String hiveVersion, boolean byDisplay) {
        List<String> hiveServerItems = new ArrayList<String>();
        if (doSupportHive1(hiveDistribution, hiveVersion, byDisplay)) {
            hiveServerItems.add(HiveServerVersionInfo.HIVE_SERVER_1.getDisplayName());
        }
        if (doSupportHive2(hiveDistribution, hiveVersion, byDisplay)) {
            hiveServerItems.add(HiveServerVersionInfo.HIVE_SERVER_2.getDisplayName());
        }
        return hiveServerItems.toArray(new String[0]);
    }

    /**
     * return true/false of {@link MRComponent#isExecutedThroughWebHCat}.
     */
    public static boolean isExecutedThroughWebHCat(String hiveDistribution, String hiveVersion, boolean byDisplay) {
        return doSupportMethod(hiveDistribution, hiveVersion, byDisplay, "isExecutedThroughWebHCat");//$NON-NLS-1$
    }

    /**
     * return true/false of {@link HiveComponent#doSupportHive1}.
     */
    public static boolean doSupportHive1(String hiveDistribution, String hiveVersion, boolean byDisplay) {
        return doSupportMethod(hiveDistribution, hiveVersion, byDisplay, "doSupportHive1");//$NON-NLS-1$
    }

    /**
     * return true/false of {@link HiveComponent#doSupportHive2}.
     */
    public static boolean doSupportHive2(String hiveDistribution, String hiveVersion, boolean byDisplay) {
        return doSupportMethod(hiveDistribution, hiveVersion, byDisplay, "doSupportHive2");//$NON-NLS-1$
    }

    /**
     * return true/false of {@link HiveComponent#doSupportTezForHive}.
     */
    public static boolean doSupportTez(String hiveDistribution, String hiveVersion, boolean byDisplay) {
        return doSupportMethod(hiveDistribution, hiveVersion, byDisplay, "doSupportTezForHive");//$NON-NLS-1$
    }

    /**
     * return true/false of {@link HiveComponent#doSupportSSL}.
     */
    public static boolean doSupportSSL(String hiveDistribution, String hiveVersion, boolean byDisplay) {
        return doSupportMethod(hiveDistribution, hiveVersion, byDisplay, "doSupportSSL");//$NON-NLS-1$
    }

    /**
     * return true/false of {@link HiveComponent#doSupportSSLwithKerberos}.
     */
    public static boolean doSupportSSLwithKerberos(String hiveDistribution, String hiveVersion, boolean byDisplay) {
        return doSupportMethod(hiveDistribution, hiveVersion, byDisplay, "doSupportSSLwithKerberos"); //$NON-NLS-1$
    }

    /**
     * return true/false of {@link HiveComponent#doSupportSecurity}. also, when hive service or embedded
     */
    public static boolean doSupportSecurity(String hiveDistribution, String hiveVersion, String hiveMode,
            String hiveServerVersion, boolean byDisplay) {
        HiveModeInfo hiveModeInfo = byDisplay ? HiveModeInfo.getByDisplay(hiveMode) : HiveModeInfo.get(hiveMode);
        HiveServerVersionInfo hiveServerVersionInfo = byDisplay ? HiveServerVersionInfo.getByDisplay(hiveServerVersion)
                : HiveServerVersionInfo.getByKey(hiveServerVersion); // same as DatabaseForm

        boolean supportSecurity = doSupportMethod(hiveDistribution, hiveVersion, byDisplay, "doSupportKerberos");//$NON-NLS-1$
        if (supportSecurity
                && (HiveServerVersionInfo.HIVE_SERVER_2 == hiveServerVersionInfo || hiveModeInfo == HiveModeInfo.EMBEDDED)) {
            return true;
        }
        return false;
    }

    /**
     * return true/false of {@link HiveComponent#doSupportEmbeddedMode}.
     */
    public static boolean doSupportEmbeddedMode(String hiveDistribution, String hiveVersion, boolean byDisplay) {
        return doSupportMethod(hiveDistribution, hiveVersion, byDisplay, "doSupportEmbeddedMode");//$NON-NLS-1$
    }

    /**
     * return true/false of {@link HiveComponent#doSupportStandaloneMode}.
     */
    public static boolean doSupportStandaloneMode(String hiveDistribution, String hiveVersion, boolean byDisplay) {
        return doSupportMethod(hiveDistribution, hiveVersion, byDisplay, "doSupportStandaloneMode");//$NON-NLS-1$
    }

    public static boolean doSupportMethod(String hiveDistribution, String hiveVersion, boolean byDisplay, String supportMethodName) {
        IHDistribution distribution = getDistribution(hiveDistribution, byDisplay);
        if (distribution != null) {
            IHDistributionVersion version = distribution.getHDVersion(hiveVersion, byDisplay);
            IHadoopDistributionService hadoopDistributionService = getHadoopDistributionService();

            if (version != null && hadoopDistributionService != null) {
                try {
                    return hadoopDistributionService.doSupportMethod(version, supportMethodName);
                } catch (Exception e) {
                    // ignore if NoSuchMethodException
                }
            }
        }
        return false;
    }

}
