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
package org.talend.metadata.managment.model;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.talend.commons.utils.database.SybaseDatabaseMetaData;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.cwm.helper.SchemaHelper;
import org.talend.utils.sql.metadata.constants.MetaDataConstants;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * created by talend on Dec 30, 2013 Detailled comment
 * 
 */
public class SybaseConnectionFillerImpl extends DBConnectionFillerImpl {

    private static Logger log = Logger.getLogger(SybaseConnectionFillerImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.metadata.DBConnectionFillerImpl#fillSchemaToCatalog(org.talend.core.model.metadata.builder
     * .connection.Connection, java.sql.DatabaseMetaData, orgomg.cwm.resource.relational.Catalog, java.util.List)
     */
    @Override
    public List<Schema> fillSchemaToCatalog(DatabaseConnection dbConn, DatabaseMetaData dbJDBCMetadata, Catalog catalog,
            List<String> schemaFilter) throws Throwable {
        ResultSet schemaRs = null;
        if (dbJDBCMetadata instanceof SybaseDatabaseMetaData) {
            schemaRs = ((SybaseDatabaseMetaData) dbJDBCMetadata).getSchemas(catalog.getName(), null);
        } else {
            schemaRs = dbJDBCMetadata.getSchemas();
        }
        List<String> schemaNameCacheTmp = new ArrayList<String>();
        List<Schema> schemaList = new ArrayList<Schema>();

        if (schemaRs != null) {
            try {
                while (schemaRs.next()) {
                    String schemaName = schemaRs.getString(MetaDataConstants.TABLE_SCHEM.name());

                    // MOD mzhao bug 9606 filter duplicated schemas.
                    if (!schemaNameCacheTmp.contains(schemaName)) {
                        if (dbConn != null && !isNullUiSchema(dbConn)) {
                            // this case we only create one schema which name is same as UiSchema
                            Schema createByUiSchema = createSchemaByUiSchema(dbConn);
                            schemaList.add(createByUiSchema);
                            break;
                        } else if (isCreateElement(schemaFilter, schemaName, true)) {
                            Schema schema = SchemaHelper.createSchema(schemaName);
                            schemaList.add(schema);
                            schemaNameCacheTmp.add(schemaName);
                        }
                    }
                }
            } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    log.debug(e, e);
                }
            } finally {
                schemaRs.close();
            }
        }

        return schemaList;
    }

}
