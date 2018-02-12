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
package org.talend.test.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * load properties from a property file.
 * 
 */
public class DBPropertiesUtils {

    private static DBPropertiesUtils dbPropertiesUtils;

    public static DBPropertiesUtils getDefault() {
        if (dbPropertiesUtils == null) {
            dbPropertiesUtils = new DBPropertiesUtils();
        }
        return dbPropertiesUtils;
    }

    public Properties getProperties() {
        String fileName = "db.properties"; //$NON-NLS-1$
        Properties prop = new Properties();
        InputStream inStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        if (inStream == null) {
            // try to get it directly from the class (this is for use in Eclipse plugin environment)
            inStream = this.getClass().getResourceAsStream(fileName);
        }
        if (inStream != null) {
            try {
                prop.load(inStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return prop;
    }

}
