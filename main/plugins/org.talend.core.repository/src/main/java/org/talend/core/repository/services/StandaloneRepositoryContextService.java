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
package org.talend.core.repository.services;

import java.util.Properties;

import org.talend.core.IRepositoryContextService;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.FileConnection;
import org.talend.core.repository.utils.StandaloneConnectionContextUtils;

/**
 * Repository context service for job runtime. Currently, the main purpose is to retrieve the job context.
 */
public class StandaloneRepositoryContextService implements IRepositoryContextService {

    private Properties contextProperties;

    public void setContextProperties(Properties contextProperties) {
        this.contextProperties = contextProperties;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IRepositoryService#cloneOriginalValueConnection(org.talend.core.model.metadata.builder
     * .connection.DatabaseConnection)
     */
    @Override
    public DatabaseConnection cloneOriginalValueConnection(DatabaseConnection dbConn) {
        return StandaloneConnectionContextUtils.cloneOriginalValueConnection(dbConn, contextProperties);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IRepositoryService#cloneOriginalValueConnection(org.talend.core.model.metadata.builder
     * .connection.DatabaseConnection, boolean)
     */
    @Override
    public DatabaseConnection cloneOriginalValueConnection(DatabaseConnection dbConn, boolean defaultContext) {
        return StandaloneConnectionContextUtils.cloneOriginalValueConnection(dbConn, contextProperties);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IRepositoryService#cloneOriginalValueConnection(org.talend.core.model.metadata.builder
     * .connection.DatabaseConnection, boolean, java.lang.String)
     */
    @Override
    public DatabaseConnection cloneOriginalValueConnection(DatabaseConnection dbConn, boolean defaultContext,
            String selectedContext) {
        return StandaloneConnectionContextUtils.cloneOriginalValueConnection(dbConn, contextProperties);
    }

    @Override
    public void setMetadataConnectionParameter(DatabaseConnection dbConn, IMetadataConnection metaConn) {
        StandaloneConnectionContextUtils.setMetadataConnectionParameter(dbConn, metaConn, contextProperties);
    }

    @Override
    public FileConnection cloneOriginalValueConnection(FileConnection fileConn) {
        return StandaloneConnectionContextUtils.cloneOriginalValueConnection(fileConn, contextProperties);
    }

}
