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
package org.talend.cwm.constants;

/**
 * @author scorreia.
 * 
 * This class enumerates the possible types of software systems.
 */
public enum SoftwareSystemConstants {
    OS("Operation System"),
    DBMS("Database management system"),
    MDDB("Multidimensional database"),
    FileSystem("File system"),
    ODBC("Open Database Connectivity"),
    JDBC("Java Database Connectivity"),
    Application("Application");

    private final String description;

    SoftwareSystemConstants(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }

}
