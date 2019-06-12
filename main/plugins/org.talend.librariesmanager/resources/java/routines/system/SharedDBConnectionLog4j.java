// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package routines.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Level;

/**
 * A buffer to keep all the DB connections, make it reusable between the different jobs.
 */
public class SharedDBConnectionLog4j {

    private static boolean DEBUG = false;

    private static org.apache.log4j.Logger LOGGER = null;

    private static String cid = null;

    private static SharedDBConnectionLog4j instance = null;

    private Map<String, Connection> sharedConnections = new HashMap<String, java.sql.Connection>();

    private SharedDBConnectionLog4j() {

    }

    private static synchronized SharedDBConnectionLog4j getInstance() {
        if (instance == null) {
            instance = new SharedDBConnectionLog4j();
        }
        return instance;
    }

    private synchronized void debugSharedConnections(){
        String keys="";
        if(DEBUG || LOGGER!=null){
            Set<String> keySet = sharedConnections.keySet();
            for (String key : keySet) {
                keys+=" "+key;
            }
        }
        if (DEBUG) {
            System.out.println("SharedDBConnection, current shared connections list is:"+keys); //$NON-NLS-1$
        }
        logMessage(Level.DEBUG,cid +" - SharedDBConnection, current shared connections list is:"+keys);
    }
    /**
     *
     * DOC jyhu Comment method "logMessage".
     * @param logLevel :current logInfo level
     * @param logInfo :loginfo
     */
    private synchronized void logMessage(Level logLevel,String logInfo){
        if(LOGGER!=null){
            LOGGER.log(logLevel,logInfo);
        }
    }
    private synchronized Connection getConnection(String dbDriver, String url, String userName, String password,
            String dbConnectionName) throws ClassNotFoundException, SQLException {

        debugSharedConnections();
        Connection connection = sharedConnections.get(dbConnectionName);
        if (connection == null) {
            if (DEBUG) {
                System.out.println("SharedDBConnection, can't find the key:" + dbConnectionName + " " //$NON-NLS-1$ //$NON-NLS-2$
                        + "so create a new one and share it."); //$NON-NLS-1$
            }
            logMessage(Level.DEBUG,cid +" - SharedDBConnection, can't find the key:" + dbConnectionName + " " + "so create a new one and share it.");
            logMessage(Level.DEBUG,cid +" - Driver ClassName: "+dbDriver+".");
            Class.forName(dbDriver);
            logMessage(Level.INFO,cid +" - SharedDBConnection, Connection attempt to '" + url + "' with the username '" + userName + "'.");
            connection = DriverManager.getConnection(url, userName, password);
            logMessage(Level.INFO,cid +" - SharedDBConnection, Connection to '" + url + "' has succeeded.");
            sharedConnections.put(dbConnectionName, connection);
            logMessage(Level.DEBUG,cid +" - Shared Connection with key '" + dbConnectionName + "'");
        } else if (connection.isClosed()) {
            if (DEBUG) {
                System.out.println("SharedDBConnection, find the key: " + dbConnectionName + " " //$NON-NLS-1$ //$NON-NLS-2$
                        + "But it is closed. So create a new one and share it."); //$NON-NLS-1$
            }
            logMessage(Level.DEBUG,cid +" - SharedDBConnection, find the key: " + dbConnectionName + " "  + "But it is closed. So create a new one and share it.");
            logMessage(Level.INFO,cid +" - SharedDBConnection, Connection attempt to '" + url + "' with the username '" + userName + "'.");
            connection = DriverManager.getConnection(url, userName, password);
            logMessage(Level.INFO,cid +" - SharedDBConnection, Connection to '" + url + "' has succeeded.");
            sharedConnections.put(dbConnectionName, connection);
            logMessage(Level.DEBUG,cid +" - SharedDBConnection, Shared Connection with key '" + dbConnectionName + "'");
        } else {
            if (DEBUG) {
                System.out.println("SharedDBConnection, find the key: " + dbConnectionName + " " + "it is OK."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
            logMessage(Level.INFO,cid +" - SharedDBConnection, find the key: " + dbConnectionName + " " + "it is OK.");
        }
        return connection;
    }

    private synchronized Connection getConnection(String dbDriver, String url, String dbConnectionName)
            throws ClassNotFoundException, SQLException {

        debugSharedConnections();

        Connection connection = sharedConnections.get(dbConnectionName);
        if (connection == null) {
            if (DEBUG) {
                System.out.println("SharedDBConnection, can't find the key:" + dbConnectionName + " " //$NON-NLS-1$ //$NON-NLS-2$
                        + "so create a new one and share it."); //$NON-NLS-1$
            }
            logMessage(Level.DEBUG,cid +" - SharedDBConnection, can't find the key:" + dbConnectionName + " " + "so create a new one and share it.");
            logMessage(Level.DEBUG,cid +" - SharedDBConnection, Driver ClassName: "+dbDriver+".");
            Class.forName(dbDriver);
            logMessage(Level.INFO,cid +" - SharedDBConnection, Connection attempt to '" + url + ".");
            connection = DriverManager.getConnection(url);
            logMessage(Level.INFO,cid +" - SharedDBConnection, Connection to '" + url + "' has succeeded.");
            sharedConnections.put(dbConnectionName, connection);
            logMessage(Level.DEBUG,cid +" - Shared Connection with key '" + dbConnectionName + "'");
        } else if (connection.isClosed()) {
            if (DEBUG) {
                System.out.println("SharedDBConnection, find the key: " + dbConnectionName + " " //$NON-NLS-1$ //$NON-NLS-2$
                        + "But it is closed. So create a new one and share it."); //$NON-NLS-1$
            }
            logMessage(Level.DEBUG,cid +" - SharedDBConnection, find the key: " + dbConnectionName + " " + "But it is closed. So create a new one and share it.");
            logMessage(Level.INFO,cid +" - SharedDBConnection, Connection attempt to '" + url + ".");
            connection = DriverManager.getConnection(url);
            logMessage(Level.INFO,cid +" - SharedDBConnection, Connection to '" + url + "' has succeeded.");
            sharedConnections.put(dbConnectionName, connection);
            logMessage(Level.DEBUG,cid +" - SharedDBConnection, Shared Connection with key '" + dbConnectionName + "'");
        } else {
            if (DEBUG) {
                System.out.println("SharedDBConnection, find the key: " + dbConnectionName + " " + "it is OK."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
            logMessage(Level.DEBUG,cid +" - SharedDBConnection, find the key: " + dbConnectionName + " " + "it is OK.");
        }
        return connection;
    }

    /**
     * If there don't exist the connection or it is closed, create and store it.
     *
     * @param dbDriver
     * @param url
     * @param userName
     * @param password
     * @param dbConnectionName
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getDBConnection(String dbDriver, String url, String userName, String password,
            String dbConnectionName) throws ClassNotFoundException, SQLException {
        SharedDBConnectionLog4j instanceLocal = getInstance();
        Connection connection = instanceLocal.getConnection(dbDriver, url, userName, password, dbConnectionName);
        return connection;
    }

    /**
     * If there don't exist the connection or it is closed, create and store it.
     *
     * @param dbDriver
     * @param url
     * @param dbConnectionName
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getDBConnection(String dbDriver, String url, String dbConnectionName) throws ClassNotFoundException,
            SQLException {
        SharedDBConnectionLog4j instanceLocal = getInstance();
        Connection connection = instanceLocal.getConnection(dbDriver, url, dbConnectionName);
        return connection;
    }

    /**
     * Set the buffer as null, make it recyclable.
     */
    public static void clear() {
        instance = null;
    }

    public static void setDebugMode(boolean debug) {
        DEBUG = debug;
    }

    public static void initLogger(org.apache.log4j.Logger logger,String uniqueName) {
        if(LOGGER ==null){
            LOGGER = logger;
        }
        cid = uniqueName;
    }
}
