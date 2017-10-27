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
package org.talend.core.database.conn.version;

import java.util.HashSet;
import java.util.Set;

import org.talend.core.database.EDatabaseTypeName;

/**
 * cli class global comment. Detailled comment
 */
public class DbVersion4DriversForOracle11 extends DbVersion4Drivers {

    public static final String DRIVER_1_6 = "ojdbc6.jar"; //$NON-NLS-1$

    public static final String DRIVER_1_5 = "ojdbc5.jar"; //$NON-NLS-1$

    DbVersion4DriversForOracle11(EDatabaseTypeName[] dbTypes, String versionDisplayName, String versionValue, String[] drivers) {
        super(dbTypes, versionDisplayName, versionValue, drivers);
    }

    @Override
    Set<String> getDrivers() {
        Set<String> drivers = new HashSet<String>();
        String javaVersion = System.getProperty("java.version");//$NON-NLS-1$
        if (javaVersion != null) {
            org.talend.commons.utils.Version v = new org.talend.commons.utils.Version(javaVersion);
            if (v.getMajor() == 1 && v.getMinor() == 5) {
                drivers.add(DRIVER_1_5);
            } else {
                drivers.add(DRIVER_1_6);
            }
        }
        return drivers;
    }
}
