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
package org.talend.metadata.managment;

import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.AbstractMetadataObject;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.service.IMetadataManagmentService;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class MetadataManagmentService implements IMetadataManagmentService {

    public IMetadataTable convertMetadataTable(MetadataTable old) {
        return ConvertionHelper.convert(old);
    }

    public MetadataTable convertMetadataTable(IMetadataTable old) {
        return ConvertionHelper.convert(old);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.service.IMetadataManagmentService#convertServicesOperation(org.talend.core.model.metadata.builder
     * .connection.AbstractMetadataObject)
     */
    public IMetadataTable convertServicesOperation(AbstractMetadataObject old) {
        return ConvertionHelper.convertServicesOperational(old);
    }
}
