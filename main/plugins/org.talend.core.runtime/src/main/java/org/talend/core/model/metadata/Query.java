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


/**
 * Meta Data Table. Contains all the columns. <br/>
 * $Id: MetadataTable.java,v 1.24.4.1 2006/09/05 13:38:25 mhelleboid Exp $
 */
public class Query implements Cloneable {

    private String queryName;

    private IMetadataConnection parent;

    public String getTableName() {
        return this.queryName;
    }

    public void setTableName(String tableName) {
        this.queryName = tableName;
    }

    public IMetadataConnection getParent() {
        return this.parent;
    }

    public void setParent(IMetadataConnection parent) {
        this.parent = parent;
    }
}
