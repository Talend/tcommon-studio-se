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
package org.talend.core.database.conn.version;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.talend.core.database.EDatabaseTypeName;

/**
 * cli class global comment. Detailled comment
 */
class DbVersion4Drivers {

    private List<EDatabaseTypeName> dbTypes = new ArrayList<EDatabaseTypeName>();

    private String versionDisplayName;

    private String versionValue;

    private Set<String> drivers = new HashSet<String>();

    DbVersion4Drivers(EDatabaseTypeName dbType, String driver) {
        this(new EDatabaseTypeName[] { dbType }, null, null, new String[] { driver });
    }

    DbVersion4Drivers(EDatabaseTypeName dbType, String[] drivers) {
        this(new EDatabaseTypeName[] { dbType }, null, null, drivers);
    }

    DbVersion4Drivers(EDatabaseTypeName dbType, String versionDisplayName, String versionValue) {
        this(new EDatabaseTypeName[] { dbType }, versionDisplayName, versionValue, new String[0]);
    }

    DbVersion4Drivers(EDatabaseTypeName dbType, String versionDisplayName, String versionValue, String driver) {
        this(new EDatabaseTypeName[] { dbType }, versionDisplayName, versionValue, new String[] { driver });
    }

    DbVersion4Drivers(EDatabaseTypeName dbType, String versionDisplayName, String versionValue, String[] drivers) {
        this(new EDatabaseTypeName[] { dbType }, versionDisplayName, versionValue, drivers);
    }

    DbVersion4Drivers(EDatabaseTypeName[] dbTypes, String versionDisplayName, String versionValue, String driver) {
        this(dbTypes, versionDisplayName, versionValue, new String[] { driver });
    }

    DbVersion4Drivers(EDatabaseTypeName[] dbTypes, String versionDisplayName, String versionValue, String[] drivers) {
        if (dbTypes != null) {
            for (EDatabaseTypeName type : dbTypes) {
                if (type != null) {
                    this.dbTypes.add(type);
                }
            }
        }
        if (drivers != null) {
            for (String d : drivers) {
                if (d != null) {
                    this.drivers.add(d);
                }

            }
        }
        this.versionDisplayName = versionDisplayName;
        this.versionValue = versionValue;
    }

    String getVersionDisplayName() {
        return this.versionDisplayName;
    }

    String getVersionValue() {
        return this.versionValue;
    }

    Set<String> getDrivers() {
        return this.drivers;
    }

    List<EDatabaseTypeName> getDbTypes() {
        return this.dbTypes;
    }
}
