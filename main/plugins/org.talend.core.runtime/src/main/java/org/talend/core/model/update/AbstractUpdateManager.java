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

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.core.model.update.extension.UpdateManagerProviderDetector;

/**
 * ggu class global comment. Detailled comment
 * 
 * update manager for the jobs / joblets.
 */
public abstract class AbstractUpdateManager implements IUpdateManager {

    private List<UpdateResult> updatedResultList = new ArrayList<UpdateResult>();

    // for joblet changed
    private List<PropertyChangeEvent> nodesChangerList = new ArrayList<PropertyChangeEvent>();

    // for repository schema rename
    private Map<String, String> schemaRenamedMap = new HashMap<String, String>();

    // for repository file connection column rename
    private Map<String, String> columnRenamedMap = new HashMap<String, String>();

    /* for table deleted and reselect on database wizard table */
    private Map<String, EUpdateResult> deletedOrReselectTablesMap = new HashMap<String, EUpdateResult>();

    private boolean fromRepository = false;

    protected boolean isAddColumn = false;

    protected static boolean isColumnUsed = false;

    /**
     * ggu AbstractUpdateManager constructor comment.
     * 
     * @param process
     */
    public AbstractUpdateManager() {
        super();
    }

    public List<PropertyChangeEvent> getNodePropertyChanger() {
        return this.nodesChangerList;
    }

    /**
     * 
     * ggu Comment method "getSchemaRenamedMap".
     * 
     * used for repository renamed schema update.
     */
    public Map<String, String> getSchemaRenamedMap() {
        return this.schemaRenamedMap;
    }

    public void setSchemaRenamedMap(Map<String, String> schemaRenamedMap) {
        this.schemaRenamedMap.clear();
        if (schemaRenamedMap == null) {
            return;
        }
        for (String old : schemaRenamedMap.keySet()) {
            this.schemaRenamedMap.put(old, schemaRenamedMap.get(old));
        }

    }

    /**
     * 
     * ldong Comment method "getColumnRenamedMap".
     * 
     * used for file connection column rename update.
     */
    public Map<String, String> getColumnRenamedMap() {
        return this.columnRenamedMap;
    }

    public void setColumnRenamedMap(Map<String, String> columnRenamedMap) {
        this.columnRenamedMap.clear();
        if (columnRenamedMap == null) {
            return;
        }
        for (String old : columnRenamedMap.keySet()) {
            this.columnRenamedMap.put(old, columnRenamedMap.get(old));
        }

    }

    public boolean isFromRepository() {
        return this.fromRepository;
    }

    public void setFromRepository(boolean fromRepository) {
        this.fromRepository = fromRepository;
    }

    /**
     * 
     * ggu Comment method "clearResult". clear all result .
     */
    public void clearResult() {
        getUpdatesNeeded().clear();
    }

    @Override
    public List<UpdateResult> getUpdatesNeeded() {
        return this.updatedResultList;
    }

    @Override
    public boolean isUpdatedNeeded(IUpdateItemType type) {
        if (type == null) {
            return false;
        }
        for (UpdateResult result : getUpdatesNeeded()) {
            if (result.getUpdateType() == type) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * ggu Comment method "addNodesPropertyChanger".
     * 
     * for joblet updated.
     */
    @Override
    public void addNodesPropertyChanger(PropertyChangeEvent event) {
        nodesChangerList.add(event);
    }

    @Override
    public boolean update(IUpdateItemType type) {
        if (type != null) {
            return executeUpdates(getUpdatesNeeded(type));
        }
        return false;
    }

    @Override
    public void checkAllModification() {
        clearResult();
        IUpdateItemType[] allUpdateItemTypes = UpdateManagerProviderDetector.INSTANCE.getAllUpdateItemTypes();
        Map<Object, Object> contextData = new HashMap<Object, Object>();
        for (IUpdateItemType type : allUpdateItemTypes) {
            List<UpdateResult> result = getUpdatesNeeded(type, false, contextData);
            if (result != null) {
                getUpdatesNeeded().addAll(result);
            }
        }
    }

    /**
     * udpate all difference.
     */
    @Override
    public boolean updateAll() {
        checkAllModification();
        return executeUpdates();
    }

    /**
     * execute Updates.
     */
    @Override
    public boolean executeUpdates() {
        return executeUpdates(getUpdatesNeeded());
    }

    public Map<String, EUpdateResult> getDeletedOrReselectTablesMap() {
        return deletedOrReselectTablesMap;
    }

    public void setDeletedOrReselectTablesMap(Map<String, EUpdateResult> deletedOrReselectTablesMap) {
        this.deletedOrReselectTablesMap = deletedOrReselectTablesMap;
    }

    /**
     * Getter for isAddColumn.
     * 
     * @return the isAddColumn
     */
    public boolean isAddColumn() {
        return this.isAddColumn;
    }

    /**
     * Sets the isAddColumn.
     * 
     * @param isAddColumn the isAddColumn to set
     */
    public void setAddColumn(boolean isAddColumn) {
        this.isAddColumn = isAddColumn;
        if (isAddColumn) {
            this.isColumnUsed = true;
        }
    }

}
