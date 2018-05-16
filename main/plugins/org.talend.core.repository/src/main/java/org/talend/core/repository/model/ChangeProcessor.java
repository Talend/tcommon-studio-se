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
package org.talend.core.repository.model;

import java.util.ArrayList;
import java.util.List;

import org.talend.repository.IRepositoryChangedListener;
import org.talend.repository.IRepositoryElementDelta;
import org.talend.repository.RepositoryChangedEvent;

/**
 * Processes the changes of the repository.
 * 
 * $Id: ChangeProcessor.java 2006-12-31下午01:15:32 bqian $
 * 
 */
public class ChangeProcessor {

    List<IRepositoryChangedListener> listeners = new ArrayList<IRepositoryChangedListener>();

    public ChangeProcessor() {

    }

    /*
     * Need to clone defensively the listener information, in case some listener is reacting to some notification
     * iteration by adding/changing/removing any of the other (for example, if it deregisters itself).
     */
    public synchronized void addRepositoryChangedListener(IRepositoryChangedListener listener) {
        if (listener == null || listeners.contains(listener)) {
            return;
        }
        listeners.add(listener);
    }

    public synchronized void removeRepositoryChangedListener(IRepositoryChangedListener listener) {
        listeners.remove(listener);
    }

    public synchronized void repositoryChanged(IRepositoryElementDelta delta) {
        for (IRepositoryChangedListener listener : listeners) {
            final RepositoryChangedEvent event = new RepositoryChangedEvent(delta);
            listener.repositoryChanged(event);
        }
    }

    /**
     * qzhang Comment method "registerRepositoryChangedListenerAsFirst".
     * 
     * @param listener
     */
    public void registerRepositoryChangedListenerAsFirst(IRepositoryChangedListener listener) {
        if (listener == null || listeners.contains(listener)) {
            return;
        }
        listeners.add(0, listener);

    }

}
