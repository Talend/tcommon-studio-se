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
package org.talend.core.database;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.runtime.hd.hive.HiveMetadataHelper;

/**
 * DOC hzhao  class global comment. Detailled comment
 */
public enum EImpalaDriver {

    HIVE2("HIVE2", "HIVE2", "org.apache.hive.jdbc.HiveDriver", "doSupportHive2"),
    IMPALA("IMPALA", "IMPALA", "com.cloudera.impala.jdbc.Driver", "doSupportImpalaConnector");

    EImpalaDriver(String displayName, String name, String driver, String supportDriverMethodName) {
        this.displayName = displayName;
        this.name = name;
        this.driver = driver;
        this.supportDriverMethodName = supportDriverMethodName;
    }

    private String displayName;

    private String name;

    private String driver;

    private String supportDriverMethodName;

    public String getDisplayName() {
        return displayName;
    }

    public String getName() {
        return name;
    }

    public String getDriver() {
        return driver;
    }

    public String getSupportDriverMethodName() {
        return supportDriverMethodName;
    }

    public static boolean isSupport(String distribution, String version, boolean byDisplay, String supportMethodName) {
        return HiveMetadataHelper.doSupportMethod(distribution, version, byDisplay, supportMethodName);
    }

    public static String[] getImpalaDriverDisplay(String distribution, String version, boolean byDisplay) {
        List<String> list = new ArrayList<>(0);
        for (EImpalaDriver driver : EImpalaDriver.values()) {
            if (isSupport(distribution, version, byDisplay, driver.getSupportDriverMethodName())) {
                list.add(driver.getDisplayName());
            }
        }
        return list.toArray(new String[0]);
    }

    public static EImpalaDriver getByDisplay(String display) {
        for (EImpalaDriver driver : EImpalaDriver.values()) {
            if (driver.getDisplayName().equals(display)) {
                return driver;
            }
        }
        return null;
    }

    public static EImpalaDriver getByName(String name) {
        for (EImpalaDriver driver : EImpalaDriver.values()) {
            if (driver.getName().equals(name)) {
                return driver;
            }
        }
        return null;
    }
}
