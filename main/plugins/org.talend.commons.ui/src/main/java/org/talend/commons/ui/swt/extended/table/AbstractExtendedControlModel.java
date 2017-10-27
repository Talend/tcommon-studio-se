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
package org.talend.commons.ui.swt.extended.table;

import org.eclipse.core.runtime.ListenerList;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class AbstractExtendedControlModel {

    /**
     * 
     * Event type. <br/>
     * 
     * $Id$
     * 
     */
    public enum EVENT_TYPE implements IExtendedControlEventType {
        NAME_CHANGED,
    };

    public static final String NAME_CHANGED = "NAME_CHANGED"; //$NON-NLS-1$

    /*
     * The list of listeners who wish to be notified when something significant happens.
     */
    private ListenerList listeners = new ListenerList();

    private String name;

    /**
     * DOC amaumont AbstractExtendedControlModel constructor comment.
     */
    public AbstractExtendedControlModel(String name) {
        super();
        this.name = name;
    }

    /**
     * DOC amaumont AbstractExtendedControlModel constructor comment.
     */
    public AbstractExtendedControlModel() {
    }

    public String getName() {
        return this.name;
    }

    /**
     * Sets the name.
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
        fireEvent(new ExtendedModelEvent(EVENT_TYPE.NAME_CHANGED));
    }

    public abstract boolean isDataRegistered();

    public abstract void release();

    public void addListener(IExtendedModelListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(IExtendedModelListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * DOC amaumont Comment method "fireEvent".
     * 
     * @param event
     */
    protected void fireEvent(ExtendedModelEvent event) {
        final Object[] listenerArray = listeners.getListeners();
        for (int i = 0; i < listenerArray.length; i++) {
            ((IExtendedModelListener) listenerArray[i]).handleEvent(event);
        }

    }

}
