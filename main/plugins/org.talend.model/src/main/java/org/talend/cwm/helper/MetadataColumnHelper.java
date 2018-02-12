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
package org.talend.cwm.helper;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.resource.record.impl.RecordFileImpl;

/**
 * DOC talend class global comment. Detailled comment
 */
public class MetadataColumnHelper {

    public static Connection getConnection(MetadataColumn metadataColumn) {
        if (metadataColumn == null) {
            return null;
        } else if (metadataColumn.eContainer() == null) {
            return null;
        } else if (metadataColumn.eContainer().eContainer() == null) {
            return null;
        }
        EObject eContainer = metadataColumn.eContainer().eContainer();
        if (RecordFileImpl.class.isInstance(eContainer)) {
            EList<DataManager> dataManager = ((RecordFileImpl) eContainer).getDataManager();
            if (dataManager.size() > 0 && Connection.class.isInstance(dataManager.get(0))) {
                return (Connection) dataManager.get(0);
            }
        } else if (orgomg.cwm.objectmodel.core.Package.class.isInstance(eContainer)) {
            return ConnectionHelper.getConnection((orgomg.cwm.objectmodel.core.Package) eContainer);

        }
        return null;
    }
}
