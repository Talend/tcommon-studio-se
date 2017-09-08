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
package org.talend.core.model.metadata.builder.database.extractots;

import org.talend.core.repository.model.provider.IDBMetadataProvider;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public interface IDBMetadataProviderObject {

    public IDBMetadataProvider getProvider();

    public void setProvider(IDBMetadataProvider provider);

    public String getDbType();

    public void setDbType(String dbType);

    public String getDbVersion();

    public void setDbVersion(String dbVersion);

    public boolean isSupportJDBC();

    public void setSupportJDBC(boolean supportJDBC);

}
