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
package org.talend.core.repository.ui.actions.metadata;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.GenericPackage;
import org.talend.core.model.metadata.builder.connection.GenericSchemaConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.impl.MetadataColumnImpl;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.GenericSchemaConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.repository.i18n.Messages;
import org.talend.core.repository.model.repositoryObject.MetadataTableRepositoryObject;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.service.IMetadataManagmentUiService;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.repository.model.IProxyRepositoryFactory;
import orgomg.cwm.objectmodel.core.Package;

/**
 * Administrator class global comment. Detailed comment <br/>
 * 
 */
public class CopyToGenericSchemaHelper {

    private static final String DEFAULT_TABLE_NAME = "metadata"; //$NON-NLS-1$

    private static IProxyRepositoryFactory repositoryFactory;

    /**
     * Administrator Comment method "moveToGenericSchema".
     * 
     * @param factory
     * @param targetPath
     * @param ve
     * @param isConnectionTableSchema
     * @throws PersistenceException
     */
    public static void copyToGenericSchema(IProxyRepositoryFactory factory, IRepositoryViewObject tableToMove, IPath targetPath)
            throws PersistenceException {

        repositoryFactory = factory;
        String connectionLabel;

        String dbmsId = null;
        DatabaseConnection dbConnectiona = null;
        if (tableToMove.getProperty().getItem() instanceof DatabaseConnectionItem) {
            Connection connection = ((DatabaseConnectionItem) tableToMove.getProperty().getItem()).getConnection();
            dbConnectiona = (DatabaseConnection) connection;
            dbmsId = dbConnectiona.getDbmsId();
            connectionLabel = tableToMove.getLabel();
        } else if (tableToMove.getProperty().getItem() instanceof GenericSchemaConnectionItem) {
            Connection connection = ((GenericSchemaConnectionItem) tableToMove.getProperty().getItem()).getConnection();
            GenericSchemaConnection dbConnection = (GenericSchemaConnection) connection;
            dbmsId = dbConnection.getMappingTypeId();
            connectionLabel = tableToMove.getProperty().getItem().getProperty().getLabel();
        } else {
            connectionLabel = tableToMove.getProperty().getItem().getProperty().getLabel();
        }
        connectionLabel = connectionLabel.replace(" ", "_"); //$NON-NLS-1$ //$NON-NLS-2$

        GenericSchemaConnectionItem connectionItem = PropertiesFactory.eINSTANCE.createGenericSchemaConnectionItem();
        GenericSchemaConnection connection = ConnectionFactory.eINSTANCE.createGenericSchemaConnection();

        // bug 23018
        if (dbmsId != null && dbmsId.length() > 0) {
            if (ContextParameterUtils.containContextVariables(dbmsId)) {
                if (GlobalServiceRegister.getDefault().isServiceRegistered(IMetadataManagmentUiService.class)) {
                    IMetadataManagmentUiService mmUIService = (IMetadataManagmentUiService) GlobalServiceRegister.getDefault()
                            .getService(IMetadataManagmentUiService.class);
                    ContextType contextType = mmUIService.getContextTypeForContextMode(dbConnectiona);
                    if (contextType != null) {
                        String context = mmUIService.getOriginalValue(contextType, dbmsId);
                        if (context != null) {
                            connection.setMappingTypeId(context);
                            connection.setMappingTypeUsed(true);
                        }
                    }
                }
            }
        }

        MetadataTable metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
        if (metadataTable.getNamespace() instanceof Package) {
            Package pkg = (Package) metadataTable.getNamespace();
            pkg.getDataManager().add(connection);
        }
        EList listColumns;
        if (tableToMove instanceof MetadataTableRepositoryObject) {
            listColumns = ((MetadataTableRepositoryObject) tableToMove).getTable().getColumns();
        } else {
            Connection sourceConnection = ((GenericSchemaConnectionItem) tableToMove.getProperty().getItem()).getConnection();
            GenericSchemaConnection dbConnection = (GenericSchemaConnection) sourceConnection;
            listColumns = (ConnectionHelper.getTables(dbConnection).toArray(new MetadataTable[0])[0]).getColumns();
        }

        // boolean isConnectionTableSchema = checkIsConnectionTableSchema(tableToMove);
        // if (isConnectionTableSchema) {
        // metadataTable.setLabel(DEFAULT_TABLE_NAME);
        // } else {
        metadataTable.setLabel(tableToMove.getLabel() == null ? DEFAULT_TABLE_NAME : tableToMove.getLabel());
        // }
        for (Object temp : listColumns) {
            MetadataColumn column = (MetadataColumnImpl) temp;
            MetadataColumn metadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
            metadataColumn.setComment(column.getComment());
            metadataColumn.setLabel(column.getLabel());
            metadataColumn.setDefaultValue(column.getDefaultValue());
            metadataColumn.setKey(column.isKey());

            metadataColumn.setLength(column.getLength());

            metadataColumn.setPrecision(column.getPrecision());

            metadataColumn.setPattern(column.getPattern());
            metadataColumn.setNullable(column.isNullable());
            String originalField = column.getOriginalField();
            if (originalField == null || "".equals(originalField)) { //$NON-NLS-1$
                originalField = column.getLabel();
            }
            metadataColumn.setOriginalField(originalField);
            metadataColumn.setTalendType(column.getTalendType());
            metadataColumn.setSourceType(column.getSourceType());

            metadataTable.getColumns().add(metadataColumn);
        }

        Property connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
        connectionProperty.setAuthor(((RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                .getProperty(Context.REPOSITORY_CONTEXT_KEY)).getUser());
        connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
        connectionProperty.setStatusCode(""); //$NON-NLS-1$
        connectionProperty.setLabel(connectionLabel);
        metadataTable.setId(factory.getNextId());

        GenericPackage g = (GenericPackage) ConnectionHelper.getPackage(connection.getName(), connection, GenericPackage.class);
        if (g != null) { // hywang
            g.getOwnedElement().add(metadataTable);
        } else {
            GenericPackage gpkg = ConnectionFactory.eINSTANCE.createGenericPackage();
            PackageHelper.addMetadataTable(metadataTable, gpkg);
            ConnectionHelper.addPackage(gpkg, connection);

        }
        connectionItem.setConnection(connection);
        connectionProperty.setItem(connectionItem);
        connectionProperty.setId(factory.getNextId());

        if (!repositoryFactory.isNameAvailable(connectionItem, connectionLabel)) {
            // schema.
            try {
                setPropNewName(connectionProperty);
            } catch (BusinessException e) {
                // e.printStackTrace();
                ExceptionHandler.process(e);
            }
        }

        repositoryFactory.create(connectionItem, targetPath);
    }

    /**
     * Comment method "checkIsConnectionTableSchema".
     * 
     * @param objectToM m isConnectionTableSchema
     * @return
     */
    private static boolean checkIsConnectionTableSchema(IRepositoryViewObject objectToMove) {
        if (objectToMove != null && objectToMove.getRepositoryObjectType() != null) {
            ERepositoryObjectType objectType = objectToMove.getRepositoryObjectType();
            if (objectType == ERepositoryObjectType.METADATA_CON_TABLE
                    || objectType == ERepositoryObjectType.METADATA_CON_SYNONYM
                    || objectType == ERepositoryObjectType.METADATA_CON_VIEW) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Comment method "getCopiedLabel".
     * 
     * @param copiedProperty
     * @return
     * @throws PersistenceException
     * @throws BusinessException
     */
    private static void setPropNewName(Property copiedProperty) throws PersistenceException, BusinessException {
        String originalLabel = copiedProperty.getLabel();
        String add1 = "Copy_of_"; //$NON-NLS-1$
        String initialTry = add1 + originalLabel;
        copiedProperty.setLabel(initialTry);
        if (repositoryFactory.isNameAvailable(copiedProperty.getItem(), null)) {
            return;
        } else {
            char j = 'a';
            while (!repositoryFactory.isNameAvailable(copiedProperty.getItem(), null)) {
                if (j > 'z') {
                    throw new BusinessException(Messages.getString("CopyToGenericSchemaHelper.cannotGenarateItem")); //$NON-NLS-1$
                }
                String nextTry = initialTry + "_" + (j++) + ""; //$NON-NLS-1$ //$NON-NLS-2$
                copiedProperty.setLabel(nextTry);
            }
        }
    }
}
