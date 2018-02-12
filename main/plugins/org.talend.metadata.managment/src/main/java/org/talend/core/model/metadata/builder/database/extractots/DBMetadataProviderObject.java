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
package org.talend.core.model.metadata.builder.database.extractots;

import org.talend.core.repository.model.provider.IDBMetadataProvider;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class DBMetadataProviderObject implements IDBMetadataProviderObject {

    private IDBMetadataProvider provider;

    private String dbType;

    private String dbVersion;

    private boolean supportJDBC;

    public IDBMetadataProvider getProvider() {
        return this.provider;
    }

    public void setProvider(IDBMetadataProvider provider) {
        this.provider = provider;

    }

    public String getDbType() {
        return this.dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getDbVersion() {
        return this.dbVersion;
    }

    public void setDbVersion(String dbVersion) {
        this.dbVersion = dbVersion;
    }

    public boolean isSupportJDBC() {
        return this.supportJDBC;
    }

    public void setSupportJDBC(boolean supportJDBC) {
        this.supportJDBC = supportJDBC;

    }
}
