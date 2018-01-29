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
package org.talend.metadata.managment.model;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.utils.sql.metadata.constants.MetaDataConstants;

import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;

/**
 * created by xqliu on 2014-10-29 Detailled comment
 * 
 */
public class NetezzaConnectionFiller extends DBConnectionFillerImpl {

    private static Logger log = Logger.getLogger(NetezzaConnectionFiller.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.metadata.DBConnectionFillerImpl#getDatabaseName(org.talend.core.model.metadata.builder.
     * connection.DatabaseConnection)
     */
    @Override
    protected String getDatabaseName(DatabaseConnection dbConn) {
        boolean isJdbc = MetadataFillFactory.isJdbcNetezza(dbConn.getDatabaseType(), dbConn.getDriverClass());
        return isJdbc ? getDatabaseNameFromUrl(dbConn.getURL()) : super.getDatabaseName(dbConn);
    }

    /**
     * get the database name from the url.
     * 
     * @param url
     * @return
     */
    private String getDatabaseNameFromUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return StringUtils.EMPTY;
        }
        int lastIndexOf1 = StringUtils.lastIndexOf(url, "/"); //$NON-NLS-1$
        if (lastIndexOf1 < 0 || lastIndexOf1 > url.length() - 1) {
            return StringUtils.EMPTY;
        }
        int lastIndexOf2 = StringUtils.lastIndexOf(url, "?"); //$NON-NLS-1$
        if (lastIndexOf2 < 0) {
            return StringUtils.substring(url, lastIndexOf1 + 1);
        } else {
            if (lastIndexOf2 < lastIndexOf1) {
                return StringUtils.EMPTY;
            }
            return StringUtils.substring(url, lastIndexOf1 + 1, lastIndexOf2);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.DBConnectionFillerImpl#getSchemaName(java.sql.ResultSet,
     * java.sql.DatabaseMetaData, orgomg.cwm.resource.relational.Catalog)
     */
    @Override
    protected String getSchemaName(ResultSet schemaRs, DatabaseMetaData dbJDBCMetadata, Catalog catalog) {
        String schemaName = null;
        String catalogName = null;
        try {
            schemaName = schemaRs.getString(MetaDataConstants.TABLE_SCHEM.name());
            catalogName = schemaRs.getString(MetaDataConstants.TABLE_CATALOG.name());

            if (catalogName != null && !StringUtils.equalsIgnoreCase(catalogName, catalog.getName())) {
                return null;
            }
        } catch (Exception e) {
            log.warn(e);
        }
        return schemaName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.metadata.DBConnectionFillerImpl#fillSchemas(org.talend.core.model.metadata.builder.connection
     * .DatabaseConnection, java.sql.DatabaseMetaData, org.talend.core.model.metadata.IMetadataConnection,
     * java.util.List)
     */
    @Override
    public List<Package> fillSchemas(DatabaseConnection dbConn, DatabaseMetaData dbJDBCMetadata,
            IMetadataConnection metaConnection, List<String> schemaFilter) {
        return new ArrayList<Package>();
    }
}
