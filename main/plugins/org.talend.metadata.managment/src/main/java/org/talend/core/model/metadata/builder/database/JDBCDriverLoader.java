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
package org.talend.core.model.metadata.builder.database;

import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.map.MultiKeyMap;
import org.talend.commons.utils.encoding.CharsetToolkit;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.version.EDatabaseVersion4Drivers;
import org.talend.utils.sql.ConnectionUtils;

/**
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 */
public class JDBCDriverLoader {

    private static MultiKeyMap classLoadersMap = new MultiKeyMap();

    private static Map<String, HotClassLoader> classLoadersMapBasedOnLibraries = new HashMap<String, HotClassLoader>();

    /**
     * Loads the jars for hive embedded mode required, I do not think it is the better method to do this here. Due to
     * the limitation on code structure, I have to write this method to load the jar required by Hive. If metadata
     * connection part is refactored, developer could adjust this method. Added by Marvin Wang on Oct 24, 2012.
     * 
     * @param libraries
     * @param dbType
     * @param dbVersion
     */
    public void loadForHiveEmbedded(List<String> libraries, String dbType, String dbVersion) {
        boolean flog = EDatabaseVersion4Drivers.containTypeAndVersion(dbType, dbVersion);
        HotClassLoader loader = null;
        if (flog) {
            loader = getHotClassLoaderFromCache(dbType, dbVersion);
            if (loader == null) {
                loader = new HotClassLoader();
                for (int i = 0; i < libraries.size(); i++) {
                    loader.addPath(libraries.get(i));
                }
                classLoadersMap.put(dbType, dbVersion, loader);
            } else {
                URL[] urls = loader.getURLs();
                if (urls != null && urls.length > 0) {
                    for (URL url : urls) {
                        String urlPath = url.getPath();
                        for (int j = 0; j < libraries.size(); j++) {
                            if (urlPath != null && !"".equals(urlPath) && urlPath.equals(libraries.get(j))) {
                                loader.addPath(libraries.get(j));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * DOC msjian Comment method "getHotClassLoaderFromCache".
     * 
     * @param dbType
     * @param dbVersion
     * @return
     */
    public HotClassLoader getHotClassLoaderFromCache(String dbType, String dbVersion) {
        HotClassLoader loader;
        loader = (HotClassLoader) classLoadersMap.get(dbType, dbVersion);
        return loader;
    }

    private HotClassLoader getHotClassLoaderFromCacheBasedOnLibraries(String[] librariesPaths) {
        if (librariesPaths == null || librariesPaths.length <= 0) {
            return new HotClassLoader();
        }
        String[] sortedLibrariesPaths = Arrays.copyOf(librariesPaths, librariesPaths.length);
        Arrays.sort(sortedLibrariesPaths);
        HotClassLoader cLoader = null;
        String key = ""; //$NON-NLS-1$
        for (String library : sortedLibrariesPaths) {
            if (library == null || (library = library.trim()).isEmpty()) {
                continue;
            }
            key = key + ";" + library; //$NON-NLS-1$
        }

        cLoader = classLoadersMapBasedOnLibraries.get(key);

        if (cLoader == null) {
            cLoader = new HotClassLoader();
            classLoadersMapBasedOnLibraries.put(key, cLoader);
            addPathsForClassLoader(librariesPaths, cLoader);
        }

        return cLoader;
    }

    public Driver getDriver(HotClassLoader loader, String[] jarPath, String driverClassName, String dbType, String dbVersion)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Driver driver;
        Class<?> cDriver = Class.forName(driverClassName, true, loader);
        driver = (Driver) cDriver.newInstance();
        return driver;
    }

    /**
     * 
     * DOC YeXiaowei Comment method "getConnection".
     * 
     * @param jarPath
     * @param driverClassName
     * @param url
     * @param username
     * @param password
     * @return
     */
    public List getConnection(String[] jarPath, String driverClassName, String url, String username, String password,
            String dbType, String dbVersion, String additionalParams) throws Exception {
        // qli modified to fix the bug 7656.
        List list = new ArrayList();

        DriverShim wapperDriver = null;
        Connection connection = null;
        try {
            HotClassLoader loader = getHotClassLoader(jarPath, dbType, dbVersion);
            wapperDriver = new DriverShim((getDriver(loader, jarPath, driverClassName, dbType, dbVersion)));
            // Object driver = loader.loadClass(driverClassName).newInstance();
            // wapperDriver = new DriverShim((Driver) (driver));
            Properties info = new Properties();

            // to avoid NPE
            username = username != null ? username : "";
            password = password != null ? password : "";
            info.put("user", username); //$NON-NLS-1$
            info.put("password", password); //$NON-NLS-1$
            if (dbType.equals(EDatabaseTypeName.ACCESS.getXmlName()) || dbType.equals(EDatabaseTypeName.GODBC.getXmlName())) {
                Charset systemCharset = CharsetToolkit.getInternalSystemCharset();
                if (systemCharset != null && systemCharset.displayName() != null) {
                    info.put("charSet", systemCharset.displayName()); //$NON-NLS-1$
                }
            }

            if (additionalParams != null && !"".equals(additionalParams) && dbType.toUpperCase().contains("ORACLE")) {
                additionalParams = additionalParams.replaceAll("&", "\n");//$NON-NLS-1$//$NON-NLS-2$
                info.load(new java.io.ByteArrayInputStream(additionalParams.getBytes()));
                connection = wapperDriver.connect(url, info);

            } else {
                if (ConnectionUtils.isHsql(url)) {
                    url = ConnectionUtils.addShutDownForHSQLUrl(url, additionalParams);
                }

                if (dbType != null && dbType.equalsIgnoreCase(EDatabaseTypeName.MSSQL.getDisplayName()) && "".equals(username)) {
                    ExtractMetaDataUtils.getInstance().setDriverCache(wapperDriver);
                }
                // MOD klliu TDQ-4659 sso could not check passed.2012-02-10
                // if (dbType.equals(EDatabaseTypeName.MSSQL.getDisplayName())) {
                // connection = ConnectionUtils.createConnection(url, (Driver) (driver.newInstance()), info);
                // } else {
                // we need to change later when TDQ supports hive embedded mode.
                // Changed by Marvin Wang on Oct. 6, 2012 for bug TDI-24027, because for hive standalone mode, it also
                // need get a class loader from the current thread.
                if (EDatabaseTypeName.HIVE.getDisplayName().equals(dbType)) {
                    Thread.currentThread().setContextClassLoader(loader);
                }
                connection = wapperDriver.connect(url, info);
            }
            // }
            // DriverManager.deregisterDriver(wapperDriver);
            // bug 9162
            list.add(connection);
            list.add(wapperDriver);
            return list;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Extract this method from getConnection(...) in this class.
     * 
     * @param jarPath
     * @param dbType
     * @param dbVersion
     * @return
     */
    public HotClassLoader getHotClassLoader(String[] jarPath, String dbType, String dbVersion) {

        if (EDatabaseTypeName.GENERAL_JDBC.getDisplayName().equals(dbType)) {
            return getHotClassLoaderFromCacheBasedOnLibraries(jarPath);
        }

        HotClassLoader loader;
        boolean flog = EDatabaseVersion4Drivers.containTypeAndVersion(dbType, dbVersion);
        if (flog) {
            loader = getHotClassLoaderFromCache(dbType, dbVersion);
            if (loader == null) {
                loader = new HotClassLoader();
                classLoadersMap.put(dbType, dbVersion, loader);
            }// else loader gotten from cache
        } else {
            loader = new HotClassLoader();
        }
        addPathsForClassLoader(jarPath, loader);
        return loader;
    }

    private void addPathsForClassLoader(String[] jarPath, HotClassLoader loader) {
        for (String element : jarPath) {
            // bug 17800 fixed: fix a problem of jdbc drivers used in the wizard.
            if (element.contains(";")) {
                String[] splittedPath = element.split(";");
                for (String element2 : splittedPath) {
                    loader.addPath(element2);
                }
            } else {
                loader.addPath(element);
            }
        }
    }
}
