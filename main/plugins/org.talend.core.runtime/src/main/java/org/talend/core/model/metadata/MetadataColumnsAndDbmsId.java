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
package org.talend.core.model.metadata;

import java.util.List;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * org.talend.core.model.metadata.builder.connection.MetadataColumn<br/>
 * 
 * org.talend.core.model.metadata.IMetadataColumn<br/>
 * 
 * @param <E> Type of beans, MetadataColumn and IMetadataColumn
 * 
 * 
 */
public class MetadataColumnsAndDbmsId<E> {

    private List<E> metadataColumns;

    private String dbmsId;

    /**
     * DOC ggu MetadataColumnsAndDbmsId constructor comment.
     * 
     * @param metadataColumns
     * @param dbmsId
     */
    public MetadataColumnsAndDbmsId(List<E> metadataColumns, String dbmsId) {
        super();
        this.metadataColumns = metadataColumns;
        this.dbmsId = dbmsId;
    }

    /**
     * Getter for metadataColumns.
     * 
     * @return the metadataColumns
     */
    public List<E> getMetadataColumns() {
        return metadataColumns;
    }

    /**
     * Getter for dbmsId.
     * 
     * @return the dbmsId
     */
    public String getDbmsId() {
        return dbmsId;
    }

}
