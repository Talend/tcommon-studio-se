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
package org.talend.utils.properties;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.talend.utils.files.FileUtils;

/**
 * DOC stephane class global comment. Detailled comment
 */
public class PropertiesReloader {

    public static synchronized void changeProperties(String fileName, String key, String oldValue, String newValue)
            throws IOException, URISyntaxException {
        FileUtils.replaceInFile(fileName, key + "=" + oldValue, key + "=" + newValue);
    }

    public static synchronized void setProperties(Class<?> clazz, String propertiesFilename, String key, String oldValue,
            String newValue) throws IOException, URISyntaxException {
        URL resource = clazz.getClassLoader().getResource(propertiesFilename);
        changeProperties(resource.toURI().getPath(), key, oldValue, newValue);
    }

    public static void main(String[] args) {
        String key = "database.driver";
        String newValue = "tagada";
        String oldValue = "org.gjt.mm.mysql.Driver";

        try {
            new PropertiesReloader()
                    .changeProperties(
                            "C:/Program Files/tomcat/apache-tomcat-5.5.26/apache-tomcat-5.5.26/webapps/org.talend.administrator/WEB-INF/classes/database.properties",
                            key, oldValue, newValue);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
