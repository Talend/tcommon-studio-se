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
import java.util.List;

/**
 * ggu class global comment. Detailled comment
 */
public interface IUpdateManager {

    /**
     * for joblet changed event.
     */
    public void addNodesPropertyChanger(PropertyChangeEvent event);

    /*
     * for all update
     */
    /**
     * this method same as "first,checkAllModification(). then executeUpdates()".
     */
    public boolean updateAll();

    public void checkAllModification();

    /**
     * before call this method, must call the "checkAllModification" method.
     */
    public List<UpdateResult> getUpdatesNeeded();

    public boolean executeUpdates();

    /**
     * before call this method, must call the "checkAllModification" method.
     */
    public boolean isUpdatedNeeded(IUpdateItemType type);

    /*
     * for each type
     */
    /**
     * this method same as "executeUpdates(getUpdatesNeeded(type))".
     */
    public boolean update(IUpdateItemType type);

    public List<UpdateResult> getUpdatesNeeded(IUpdateItemType type);

    public List<UpdateResult> getUpdatesNeeded(IUpdateItemType type, boolean onlySimpleShow);

    public boolean executeUpdates(List<UpdateResult> results);

    public void retrieveRefInformation();
}
