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
package org.talend.core;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.ui.IViewPart;
import org.talend.commons.exception.BusinessException;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public interface ITDQRepositoryService extends IService {

    public static final String RULE_VALUE = "RULE_VALUE"; //$NON-NLS-1$

    public static final String RULE_TYPE = "RULE_TYPE"; //$NON-NLS-1$

    public static final String RULE_NAME = "RULE_NAME"; //$NON-NLS-1$

    public static final String RULE_TABLE = "RULE_TABLE";//$NON-NLS-1$

    public IViewPart getTDQRespositoryView();

    public void openConnectionEditor(Item item);

    /**
     * 
     * add some new Alias to sql explorer system.
     * 
     * @param items
     */
    public void notifySQLExplorer(Item... items);

    public void fillMetadata(ConnectionItem connItem);

    public void refresh();

    // Added 20120503 yyin
    public void refresh(Object refreshObject);

    public void initProxyRepository();

    public void addPartListener();

    // MOD klliu 2011-04-28 bug 20204 removing connection is synced to the connection view of SQL explore
    public boolean removeAliasInSQLExplorer(IRepositoryNode children);

    // MOD zshen 2012-09-19 bug TDQ-5850 change the action for create softWareSystemResource, so need to delete it when
    // connection just be deleted.
    public boolean removeSoftWareSystem(IRepositoryNode children);

    public void createParserRuleItem(ArrayList<HashMap<String, Object>> values, String parserRuleName);

    public List<Map<String, String>> getPaserRulesFromResources(Object[] rules);

    public List<Map<String, String>> getPaserRulesFromRules(Object parser);

    // ADD zshen 2012-1-6 TDQ-4384
    public ReturnCode reloadDatabase(ConnectionItem connectionItem);

    // ADD qiongli 2011-9-13 TDQ-3797
    public void updateImpactOnAnalysis(ConnectionItem connectionItem);

    // ADD qiongli 2011-9-13 TDQ-3930
    public boolean confirmUpdateAnalysis(ConnectionItem connectionItem);

    // ADD qiongli 2012-4-19 TDQ-5130
    public boolean hasClientDependences(ConnectionItem connectionItem);

    // ADD zshen 2012-5-9 TDQ-4738
    public int confimDelete(IRepositoryViewObject deleteObject);

    // ADD zshen 2012-5-9 TDQ-4738
    // public boolean deleteObjectPhysical(IRepositoryViewObject deleteObject, Item newItem);

    // ADD zshen 2012-5-9 TDQ-4738
    public InputDialog getInputDialog(final Item newItem);

    // ADD zshen 2012-5-9 TDQ-4738
    public void changeElementName(Item newItem, String newName);

    // ADD xqliu 2012-05-25 TDQ-4831
    public boolean sourceFileOpening(RepositoryNode node);

    // ADD xqliu 2012-07-24 TDQ-5853
    public void checkUsernameBeforeSaveConnection(ConnectionItem connectionItem);

    // ADD qiongli 2012-11-12 TDQ-6166
    public void initAllConnectionsToSQLExplorer();

    // Add yyin 2012-11-15 TDQ-6395
    public void saveConnectionWithDependency(ConnectionItem connectionItem);

    public void refreshConnectionEditor(Item item);

    // Add xqliu 2012-12-11 TDQ-5750
    public void refreshCurrentAnalysisEditor(ConnectionItem connectionItem);

    // Add zshen 2013-01-05
    @Deprecated
    public void refreshCurrentAnalysisAndConnectionEditor();

    /**
     * create a hive connection(embedded or standalone) according to the IMetadataConnection, if it is not a hive
     * connection type, return null.
     * 
     * @param metadataConnection the db((MetadataTable) selectedObj).getColumns() type should be hive
     * @return a hive connection or null
     */
    public Connection createHiveConnection(IMetadataConnection metadataConnection);

    // Add qiongli 2013-05-10 TDQ-7217
    public boolean isDQEditorOpened(Item item);

    /**
     * 
     * Add the software system update listener. The listener will be triggered when a new connection created, an
     * existing connection reloaded, modifed with another type and updated from SVN repository. <br>
     * Note that this listener will be registered only once a time when application startup.
     */
    public void addSoftwareSystemUpdateListener();

    /**
     * 
     * Publish software system update event.
     * 
     * @param databaseConnection
     */
    public void publishSoftwareSystemUpdateEvent(DatabaseConnection databaseConnection);

    /**
     * After creating the database connection, will open the connection editor when this parameter= false (used in
     * ConnectionTypePage, which will use DatabaseWizard but no need to open the connection editor after creating the db
     * from here); will not open the connection editor when this parameter=true;
     * 
     * @param noNeedToOpenConnectionEditor: true--open the connection editor after creating, false-- not open
     */
    public void setIsOpenConnectionEditorAfterCreate(boolean noNeedToOpenConnectionEditor);

    /**
     * Refresh the table in data sample section of match analysis after running
     * 
     * @param result
     */
    public void refreshTableWithResult(ModelElement analysis, List<Object[]> result);

    /**
     * 
     * update sql exploerer by the old connection name and new connectionItem.
     * 
     * @param connectionItem The connection item with new name that is used to update the Alias.
     * @param oldConnName The original connection name used to find Alias from SQL Explorer system.
     */
    public void updateAliasInSQLExplorer(ConnectionItem connectionItem, String oldConnName);

    /**
     * update the depended analysis of the current file connection, when the file connection changed schema: if the
     * analysis 's analyzed columns are in the changed schema: compare columns, and remain the column with same name.
     * 
     * @param newMetadataTable
     */
    public void reloadMetadataOfDelimitedFile(MetadataTable newMetadataTable) throws BusinessException;

    public Collection<org.talend.core.model.metadata.builder.connection.Connection> getAllDataProviders();

    // Added TDQ-8787 20140616: publish a dynamic chart event
    public void publishDynamicEvent(ModelElement indicator, Object value);

    public Item createFile(String content, IPath path, String label, String extension);

    public void changePerspectiveAction(String perspectiveId);

    ConnectionItem createHive(RepositoryNode currentNode);

    void reloadTableList(final DatabaseConnectionItem hiveConnectionItem2);

    /**
     * When the context is modified, the analysis and report which had imported this context should also be updated.
     * Added TDQ-14992,yyin,20180518
     * 
     * @param contextManager
     */
    void updateAllContextInAnalysisAndReport(ContextItem contextItem, JobContextManager contextManager);
}
