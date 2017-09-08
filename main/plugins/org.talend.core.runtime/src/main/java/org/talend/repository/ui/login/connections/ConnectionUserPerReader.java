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
package org.talend.repository.ui.login.connections;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.general.ConnectionBean;
import org.talend.utils.json.JSONArray;
import org.talend.utils.json.JSONException;
import org.talend.utils.json.JSONObject;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class ConnectionUserPerReader {

    private static ConnectionUserPerReader con = new ConnectionUserPerReader();

    private String perfileName = "connection_user.properties"; //$NON-NLS-1$

    public static final String CONNECTION_REGISTFAILTIMES = "connection.registFailTimes"; //$NON-NLS-1$

    private String path = null;

    private File perfile = null;

    private Properties proper = null;

    private boolean isRead;

    private ConnectionUserPerReader() {
        proper = new EncryptedProperties();
        isRead = false;
        path = new Path(Platform.getConfigurationLocation().getURL().getPath()).toFile().getAbsolutePath();
        String tmp = String.valueOf(path.charAt(path.length() - 1));
        if (!tmp.equals(File.separator)) {
            perfile = new File(path + File.separator + perfileName);
        } else {
            perfile = new File(path + perfileName);
        }
    }

    public static ConnectionUserPerReader getInstance() {
        synchronized (con) {
            if (con == null) {
                con = new ConnectionUserPerReader();
            }
            return con;
        }
    }

    public List<ConnectionBean> forceReadConnections() {
        isRead = false;
        return readConnections();
    }

    /**
     * connetion.users=user1|user2|user3|user4 user1=local#Local#Default connection####false
     * 
     * 
     * DOC teileizeget Comment method "readConnections".
     * 
     * @return
     * @throws DocumentException
     */
    public List<ConnectionBean> readConnections() {
        if (!isRead) {
            this.readProperties();
        }
        List<ConnectionBean> toReturn = new ArrayList<ConnectionBean>();

        String userString = proper.getProperty("connection.users"); //$NON-NLS-1$
        if (userString != null) {
            String[] users = userString.split("\\|");//$NON-NLS-1$
            for (String usr : users) {
                ConnectionBean conBean = ConnectionBean.writeFromString(proper.getProperty(usr));
                toReturn.add(conBean);
            }
            // FIXME
            // proper.remove("connection.users");
        }
        try {
            String jsonString = proper.getProperty("connection.define"); //$NON-NLS-1$
            if (jsonString != null) {
                JSONArray users = new JSONArray(jsonString);
                for (int i = 0; i < users.length(); i++) {
                    JSONObject user = users.getJSONObject(i);
                    ConnectionBean conBean = ConnectionBean.writeFromJSON(user);
                    toReturn.add(conBean);
                }
            }
        } catch (JSONException e) {
            ExceptionHandler.process(e);
        }
        return toReturn;
    }

    private ConnectionUserPerReader readProperties() {
        if (!isHaveUserPer()) {
            createPropertyFile();
        }
        try {
            proper.load(new FileInputStream(perfile));
            isRead = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public String readLastConncetion() {
        if (!isRead) {
            this.readProperties();
        }
        String tmp = proper.getProperty("connection.lastConnection"); //$NON-NLS-1$
        if (tmp == null) {
            tmp = "";//$NON-NLS-1$
        }
        return tmp;
    }

    public void saveConnections(List<ConnectionBean> cons) {
        if (!isHaveUserPer()) {
            createPropertyFile();
        }
        if (!isRead) {
            this.readProperties();
        }
        if (cons == null || cons.size() == 0) {
            proper.remove("connection.users");//$NON-NLS-1$
        } else {
            JSONArray usersJsonArray = new JSONArray();
            for (ConnectionBean currentConnection : cons) {
                // String userName = currentConnection.getName();
                // if (i != 0 && userName != null) {
                //                    sb.append("|");//$NON-NLS-1$
                // }
                // if (userName != null) {
                JSONObject userJson = currentConnection.getConDetails();
                // proper.setProperty(userName, userJson.toString());
                usersJsonArray.put(userJson);
                // }
            }
            //proper.setProperty("connection.users", sb.toString());//$NON-NLS-1$
            if (proper.getProperty("connection.users") != null) {
                proper.remove("connection.users");
            }
            proper.setProperty("connection.define", usersJsonArray.toString());//$NON-NLS-1$
        }
        try {

            FileOutputStream out = new FileOutputStream(perfile);
            proper.store(out, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveLastConnectionBean(ConnectionBean bean) {
        if (!isHaveUserPer()) {
            createPropertyFile();
        }
        if (!isRead) {
            this.readProperties();
        }
        if (bean == null) {
            proper.remove("connection.lastConnection"); //$NON-NLS-1$
        } else {
            String userName = bean.getName();
            if (!"".equals(userName) && userName != null) { //$NON-NLS-1$
                proper.setProperty("connection.lastConnection", userName);//$NON-NLS-1$
            } else {
                proper.remove("connection.lastConnection"); //$NON-NLS-1$
            }
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(perfile);
            proper.store(out, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createPropertyFile() {
        File fatherFloder = new File(path);
        if (!fatherFloder.exists()) {
            fatherFloder.mkdirs();
        }
        try {
            if (!perfile.exists()) {
                perfile.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean isHaveUserPer() {
        return perfile.exists();
    }

    /**
     * DOC ycbai Comment method "readRegistFailTimes".
     * 
     * @return
     */
    public String readRegistFailTimes() {
        if (!isRead) {
            this.readProperties();
        }
        return StringUtils.trimToEmpty(proper.getProperty(CONNECTION_REGISTFAILTIMES));
    }

    public String readRegistration() {
        if (!isRead) {
            this.readProperties();
        }
        String tmp = proper.getProperty("connection.readRegistration"); //$NON-NLS-1$
        if (tmp == null) {
            tmp = "";//$NON-NLS-1$
        }
        return tmp;
    }

    public String readRegistrationDone() {
        if (!isRead) {
            this.readProperties();
        }
        String tmp = proper.getProperty("connection.readRegistrationDone"); //$NON-NLS-1$
        if (tmp == null) {
            tmp = "";//$NON-NLS-1$
        }
        return tmp;
    }

    public String readLicenseManagement() {
        if (!isRead) {
            this.readProperties();
        }
        String tmp = proper.getProperty("connection.licenseManagement"); //$NON-NLS-1$
        if (tmp == null) {
            tmp = "";//$NON-NLS-1$
        }
        return tmp;
    }

    public void saveRegistoryBean() {
        if (!isHaveUserPer()) {
            createPropertyFile();
        }
        if (!isRead) {
            this.readProperties();
        }
        IPreferenceStore prefStore = PlatformUI.getPreferenceStore();
        proper.setProperty("connection.readRegistration", Integer.toString(prefStore.getInt("REGISTRATION_TRIES")));
        proper.setProperty("connection.readRegistrationDone", Integer.toString(prefStore.getInt("REGISTRATION_DONE")));
        try {

            FileOutputStream out = new FileOutputStream(perfile);
            proper.store(out, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * DOC ycbai Comment method "saveRegistoryBean".
     * 
     * @param propertyMap
     */
    public void saveRegistoryBean(Map<String, String> propertyMap) {
        if (propertyMap == null) {
            return;
        }
        if (!isHaveUserPer()) {
            createPropertyFile();
        }
        if (!isRead) {
            this.readProperties();
        }
        Iterator<Entry<String, String>> iter = propertyMap.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, String> entry = iter.next();
            String key = entry.getKey();
            String val = entry.getValue();
            proper.setProperty(key, val);
        }
        try {
            FileOutputStream out = new FileOutputStream(perfile);
            proper.store(out, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveLiscenseManagement() {
        if (!isHaveUserPer()) {
            createPropertyFile();
        }
        if (!isRead) {
            this.readProperties();
        }
        IPreferenceStore prefStore = PlatformUI.getPreferenceStore();
        proper.setProperty("connection.licenseManagement", Integer.toString(prefStore.getInt("LICENSE_VALIDATION_DONE")));
        try {

            FileOutputStream out = new FileOutputStream(perfile);
            proper.store(out, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isInstallDone() {
        if (!isRead) {
            this.readProperties();
        }
        String tmp = proper.getProperty("connection.installDone"); //$NON-NLS-1$
        if (Boolean.TRUE.toString().equals(tmp)) {
            return true;
        }
        return false;
    }

    public void setInstallDone() {
        if (!isHaveUserPer()) {
            createPropertyFile();
        }
        if (!isRead) {
            this.readProperties();
        }
        proper.setProperty("connection.installDone", Boolean.TRUE.toString()); //$NON-NLS-1$
        try {
            FileOutputStream out = new FileOutputStream(perfile);
            proper.store(out, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
