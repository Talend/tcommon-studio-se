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
package org.talend.core.model.update;

import java.util.HashMap;
import java.util.Map;

/**
 * created by ldong on Feb 6, 2015 Detailled comment
 * 
 */
public class ConnectionColumnUpdateManager {

    private static ConnectionColumnUpdateManager columnManager = null;

    private Map<String, String> columnRenameMap = new HashMap<String, String>();

    private ConnectionColumnUpdateManager() {

    }

    public synchronized static ConnectionColumnUpdateManager getInstance() {
        if (columnManager == null) {
            columnManager = new ConnectionColumnUpdateManager();
        }
        return columnManager;
    }

    public Map<String, String> getColumnRenameMap() {
        return this.columnRenameMap;
    }

    public void addNewName(String newName, String oldName) {
        String name = columnRenameMap.get(oldName);
        if (name != null) {
            columnRenameMap.remove(oldName);
            columnRenameMap.put(newName, name);
        } else {
            columnRenameMap.put(newName, oldName);
        }
    }

    public void setColumnRenameMap(Map<String, String> columnRenameMap) {
        this.columnRenameMap = columnRenameMap;
    }

}
