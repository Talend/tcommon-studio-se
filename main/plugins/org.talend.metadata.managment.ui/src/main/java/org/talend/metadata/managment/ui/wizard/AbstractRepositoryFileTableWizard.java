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
package org.talend.metadata.managment.ui.wizard;

import org.eclipse.emf.common.util.EList;
import org.eclipse.ui.IWorkbench;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public abstract class AbstractRepositoryFileTableWizard extends CheckLastVersionRepositoryWizard {

    public AbstractRepositoryFileTableWizard(IWorkbench workbench, boolean creation) {
        super(workbench, creation);
    }

    public AbstractRepositoryFileTableWizard(IWorkbench workbench, boolean creation, boolean forceReadOnly) {
        super(workbench, creation, forceReadOnly);
    }

    @Override
    public boolean performCancel() {
        if (creation) {
            EList<orgomg.cwm.objectmodel.core.Package> packges = this.connectionItem.getConnection().getDataPackage();
            if (packges.size() == 1) {
                orgomg.cwm.objectmodel.core.Package pkg = packges.get(0);
                EList<ModelElement> tables = pkg.getOwnedElement();
                /* remove the last table if it's creation */
                if (tables.contains(this.metadataTable)) {
                    tables.remove(this.metadataTable);
                }
            }
        }
        return super.performCancel();
    }

}
