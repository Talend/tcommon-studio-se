// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.metadata;

import java.util.List;

/**
 * org.talend.core.model.metadata.builder.connection.MetadataColumn<br/>
 * 
 * org.talend.core.model.metadata.IMetadataColumn<br/>
 * 
 * @param <E> Type of beans, MetadataColumn and IMetadataColumn
 */
public class MetadataColumnsAndDbmsId<E> {

    private List<E> metadataColumns;

    private String dbmsId;

    public MetadataColumnsAndDbmsId(List<E> metadataColumns, String dbmsId) {
        super();
        this.metadataColumns = metadataColumns;
        this.dbmsId = dbmsId;
    }

    public List<E> getMetadataColumns() {
        return metadataColumns;
    }

    public String getDbmsId() {
        return dbmsId;
    }

}
