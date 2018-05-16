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
package org.talend.core.model.metadata.query.generator;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.QueryUtil;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.query.AbstractQueryGenerator;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.service.ICoreUIService;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class AS400QueryGenerator extends AbstractQueryGenerator {

    private boolean standardSyntax;

    public AS400QueryGenerator() {
        super(EDatabaseTypeName.AS400);
    }

    @Override
    public void setParameters(IElement element, IMetadataTable metadataTable, String schema, String realTableName) {
        super.setParameters(element, metadataTable, schema, realTableName);
        //
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ICoreUIService.class)) {
            ICoreUIService service = (ICoreUIService) GlobalServiceRegister.getDefault().getService(ICoreUIService.class);
            standardSyntax = service.getPreferenceStore().getBoolean(ITalendCorePrefConstants.AS400_SQL_SEG);
        }
        if (getElement() != null) {
            IElementParameter parentParam = getElement().getElementParameterFromField(EParameterFieldType.PROPERTY_TYPE);
            if (parentParam != null && parentParam.getChildParameters() != null) {
                IElementParameter param = parentParam.getChildParameters().get("PROPERTY_TYPE"); //$NON-NLS-1$
                if (param != null && "REPOSITORY".equals(param.getValue())) { //$NON-NLS-1$
                    param = parentParam.getChildParameters().get("REPOSITORY_PROPERTY_TYPE"); //$NON-NLS-1$
                    if (param != null && param.getValue() != null) {
                        try {
                            IRepositoryViewObject lastVersion = CorePlugin.getDefault().getProxyRepositoryFactory()
                                    .getLastVersion((String) param.getValue());
                            if (lastVersion != null) {
                                Item item = lastVersion.getProperty().getItem();
                                if (item != null && item instanceof DatabaseConnectionItem) {
                                    standardSyntax = ((DatabaseConnection) ((DatabaseConnectionItem) item).getConnection())
                                            .isStandardSQL();
                                }
                            }
                        } catch (PersistenceException e) {
                            ExceptionHandler.process(e);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void setParameters(IElement element, IMetadataTable metadataTable, String schema, String realTableName, boolean isJdbc) {
        super.setParameters(element, metadataTable, schema, realTableName, isJdbc);
        //
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ICoreUIService.class)) {
            ICoreUIService service = (ICoreUIService) GlobalServiceRegister.getDefault().getService(ICoreUIService.class);
            standardSyntax = service.getPreferenceStore().getBoolean(ITalendCorePrefConstants.AS400_SQL_SEG);
        }
        if (getElement() != null) {
            IElementParameter parentParam = getElement().getElementParameterFromField(EParameterFieldType.PROPERTY_TYPE);
            if (parentParam != null && parentParam.getChildParameters() != null) {
                IElementParameter param = parentParam.getChildParameters().get("PROPERTY_TYPE"); //$NON-NLS-1$
                if (param != null && "REPOSITORY".equals(param.getValue())) { //$NON-NLS-1$
                    param = parentParam.getChildParameters().get("REPOSITORY_PROPERTY_TYPE"); //$NON-NLS-1$
                    if (param != null && param.getValue() != null) {
                        try {
                            IRepositoryViewObject lastVersion = CorePlugin.getDefault().getProxyRepositoryFactory()
                                    .getLastVersion((String) param.getValue());
                            if (lastVersion != null) {
                                Item item = lastVersion.getProperty().getItem();
                                if (item != null && item instanceof DatabaseConnectionItem) {
                                    standardSyntax = ((DatabaseConnection) ((DatabaseConnectionItem) item).getConnection())
                                            .isStandardSQL();
                                }
                            }
                        } catch (PersistenceException e) {
                            ExceptionHandler.process(e);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected char getSQLFieldConnector() {
        if (standardSyntax) {
            return super.getSQLFieldConnector();
        } else {
            return '/';
        }
    }

    @Override
    protected String getTableNameWithDBAndSchema(final String dbName, final String schema, String tableName) {
        if (tableName == null || EMPTY.equals(tableName.trim())) {
            tableName = QueryUtil.DEFAULT_TABLE_NAME;
        }
        final StringBuffer tableNameWithDBAndSchema = new StringBuffer();

        if (schema != null && !EMPTY.equals(schema)) {
            tableNameWithDBAndSchema.append(checkContextAndAddQuote(schema));
            tableNameWithDBAndSchema.append(getSQLFieldConnector());
        }
        //
        tableNameWithDBAndSchema.append(checkContextAndAddQuote(tableName));
        return tableNameWithDBAndSchema.toString();
    }

}
