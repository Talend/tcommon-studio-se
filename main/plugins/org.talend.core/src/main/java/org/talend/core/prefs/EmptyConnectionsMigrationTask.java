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
package org.talend.core.prefs;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;
import org.talend.migration.AbstractMigrationTask;
import org.talend.migration.IWorkspaceMigrationTask;

/**
 * created by nrousseau on Jan 5, 2013 Detailled comment
 * 
 */
public class EmptyConnectionsMigrationTask extends AbstractMigrationTask implements IWorkspaceMigrationTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.IMigrationTask#execute()
     */
    public boolean execute() {
        IPreferenceStore store = PlatformUI.getPreferenceStore();

        if (store.contains(ITalendCorePrefConstants.LAST_USED_CONNECTION)) {
            store.setValue(ITalendCorePrefConstants.LAST_USED_CONNECTION, ""); //$NON-NLS-1$
        }

        if (store.contains(ITalendCorePrefConstants.CONNECTIONS)) {
            store.setValue(ITalendCorePrefConstants.CONNECTIONS, ""); //$NON-NLS-1$
        }

        if (store.contains("Connections.new")) { //$NON-NLS-1$
            store.setValue("Connections.new", ""); //$NON-NLS-1$ //$NON-NLS-2$
        }

        if (store != null && store instanceof IPersistentPreferenceStore && store.needsSaving()) {
            try {
                ((IPersistentPreferenceStore) store).save();
            } catch (IOException e) {
                //
            }
        }

        return true;
    }

    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2013, 1, 5, 12, 0, 0);
        return gc.getTime();
    }
}
