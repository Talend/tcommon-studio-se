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
package org.talend.core.repository.model.provider;

import java.util.List;
import java.util.Map;

import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.repository.model.connection.ConnectionStatus;
import org.talend.cwm.relational.TdColumn;

/**
 * DOC hywang class global comment. This interface can be used to extend a new type of database,implement all the
 * methods can make it adapt to UI
 */
public interface IDBMetadataProvider {

    public Map getConnectionMap();

    /** test connection used for test a connection,for example ,click "check" button on database wizard **/
    public ConnectionStatus testConnection(IMetadataConnection metadataConnection);

    /** when check connection ,will update the package,this one no use for HBASE **/
    public void updatePackage(IMetadataConnection metadataConnection);

    /** init the root node to display on selector table wizard **/
    public List getTableNodeInfo(IMetadataConnection metadataConnection);

    /**
     * to implement a AbstractMetadataExtractorViewProvider in the implemented class of this interface,the view will use
     * the provider to show all nodes
     **/
    public AbstractMetadataExtractorViewProvider getMetadataViewProvider();

    /**
     * return the node type in which the node will add a executeble thread to execute the code retrieve columns etc
     * which wrote in method "executeInRunnable"
     **/
    public int getRunnableAccessNodeType();

    /** execution in thread which to retieve schemas/tables/metadatas etc **/
    public void executeInRunnable(IMetadataConnection metadataConnection, Object currentNode, DatabaseConnection dbconn);

    /** implement the method will fill datapackages when create databaseConnection **/
    public void fillConnection(DatabaseConnection connection);

    public String getDefaultCatalogName();

    /**
     * implement this method will delete things from connection as u want ,when deselect a node in selector table wizard
     **/
    public void deleteMetadataFromConnection(Object node, DatabaseConnection connection);

    public boolean isMetadataExsit(Object node, DatabaseConnection connection);

    public List<String> returnTablesFormConnection(IMetadataConnection metadataConnection);

    public List<TdColumn> returnMetadataColumnsFromTable(String tableName, IMetadataConnection metadataConnection);

    /** is guess schema button enable **/
    public boolean isSupportGuessSchema();

    /** is retrieve schama button enable **/
    public boolean isSupportRetrieveSchema();

}
