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
package org.talend.core.repository.model.listeners;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.repository.documentation.ERepositoryActionName;

/**
 * DOC talend class global comment. Detailled comment
 */
public abstract class AbstractJobRestoreListener implements PropertyChangeListener {

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (!event.getPropertyName().equals(ERepositoryActionName.RESTORE.getName())) {
            return;
        }

        Object newValue = event.getNewValue();
        if (!(newValue instanceof IRepositoryViewObject)) {
            return;
        }
        IRepositoryViewObject jobObjectToRestore = (IRepositoryViewObject) newValue;
        execute(jobObjectToRestore);
    }

    public abstract void execute(IRepositoryViewObject jobObjectToRestore);
}
