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
package org.talend.core.database.conn.version;

import java.util.HashSet;
import java.util.Set;

/**
 * DOC jding  class global comment. Detailled comment
 */
public class DatabaseDriversCache {

    private static Set<String> driversSet;

    public static Set<String> getDriversSet() {
        if (driversSet == null || driversSet.isEmpty()) {
            initDriversSet();
        }
        return driversSet;
    }

    private static void initDriversSet() {
        driversSet = new HashSet<String>();
        EDatabaseVersion4Drivers[] values = EDatabaseVersion4Drivers.values();
        for (EDatabaseVersion4Drivers databaseVersion4Drivers : values) {
            Set<String> providerDrivers = databaseVersion4Drivers.getProviderDrivers();
            driversSet.addAll(providerDrivers);
        }
    }

    public static boolean isDatabaseDriver(String module) {
        boolean flag = false;
        Set<String> drivers = getDriversSet();
        if (drivers.contains(module)) {
            flag = true;
        }
        return flag;
    }

}
