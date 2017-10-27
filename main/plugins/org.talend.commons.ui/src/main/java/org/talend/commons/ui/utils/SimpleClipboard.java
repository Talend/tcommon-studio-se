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
package org.talend.commons.ui.utils;

import org.eclipse.core.runtime.ListenerList;
import org.talend.commons.ui.swt.extended.table.ClipboardEvent;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class SimpleClipboard {

    private ListenerList listeners = new ListenerList();

    private static SimpleClipboard instance;

    public static SimpleClipboard getInstance() {
        if (instance == null) {
            instance = new SimpleClipboard();
        }
        return instance;
    }

    private Object object;

    public void setData(Object object) {
        this.object = object;
        fireEvent(new ClipboardEvent());
    }

    public Object getData() {
        return object;
    }

    public void addListener(IClipoardListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(IClipoardListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * DOC amaumont Comment method "fireEvent".
     * 
     * @param event
     */
    protected void fireEvent(ClipboardEvent event) {
        final Object[] listenerArray = listeners.getListeners();
        for (int i = 0; i < listenerArray.length; i++) {
            ((IClipoardListener) listenerArray[i]).handleEvent(event);
        }

    }

}
