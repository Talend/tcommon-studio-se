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
package org.talend.repository.ui.wizards.metadata.connection.database;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ITDQCompareService;
import org.talend.core.ITDQRepositoryService;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.database.EDatabase4DriverClassName;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.ConnParameterKeys;
import org.talend.core.database.conn.version.EDatabaseVersion4Drivers;
import org.talend.core.hadoop.IHadoopClusterService;
import org.talend.core.hadoop.IHadoopDistributionService;
import org.talend.core.hadoop.repository.HadoopRepositoryUtil;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.core.model.metadata.builder.database.PluginConstant;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.core.model.metadata.connection.hive.HiveModeInfo;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.update.RepositoryUpdateManager;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.model.provider.IDBMetadataProvider;
import org.talend.core.repository.utils.AbstractResourceChangesService;
import org.talend.core.repository.utils.TDQServiceRegister;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.hd.IHDistribution;
import org.talend.core.runtime.services.IGenericDBService;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.metadata.managment.connection.manager.HiveConnectionManager;
import org.talend.metadata.managment.model.MetadataFillFactory;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.metadata.managment.ui.utils.DBConnectionContextUtils;
import org.talend.metadata.managment.ui.wizard.CheckLastVersionRepositoryWizard;
import org.talend.metadata.managment.ui.wizard.PropertiesWizardPage;
import org.talend.metadata.managment.ui.wizard.metadata.connection.Step0WizardPage;
import org.talend.metadata.managment.utils.MetadataConnectionUtils;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.utils.json.JSONArray;
import org.talend.utils.json.JSONException;
import org.talend.utils.json.JSONObject;
import org.talend.utils.sql.ConnectionUtils;
import org.talend.utils.sugars.ReturnCode;

import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DatabaseWizard present the DatabaseForm. Use to manage the metadata connection.
 */
public class DatabaseWizard extends CheckLastVersionRepositoryWizard implements INewWizard {

    private static Logger log = Logger.getLogger(DatabaseWizard.class);

    private PropertiesWizardPage propertiesWizardPage;

    private DatabaseWizardPage databaseWizardPage;

    // TODO Remove this refrence, use connectionItem (at super class) instead.
    private Connection connection;

    private Property connectionProperty;

    private String originaleObjectLabel;

    private String originalVersion;

    private String originalPurpose;

    private String originalDescription;

    private String originalStatus;

    private String originalSid;

    private String originalUiSchema;

    private String originalHCId; // related hadoop cluster id.

    private boolean isToolBar;

    // Added 20120503 yyin TDQ-4959
    private RepositoryNode node;

    private IProxyRepositoryFactory repFactory;

    private String propertyId;
    
    private ConnectionItem originalConnectionItem;

    /**
     * Constructor for DatabaseWizard. Analyse Iselection to extract DatabaseConnection and the pathToSave. Start the
     * Lock Strategy.
     * 
     * @param selection
     * @param existingNames
     */
    public DatabaseWizard(IWorkbench workbench, boolean creation, ISelection selection, String[] existingNames) {
        super(workbench, creation);
        this.selection = selection;
        this.existingNames = existingNames;
        setNeedsProgressMonitor(true);

        // RepositoryNode node = null;
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        if (obj instanceof RepositoryNode) {
            node = (RepositoryNode) obj;
        } else {
            return;
        }

        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case REPOSITORY_ELEMENT:
            pathToSave = RepositoryNodeUtilities.getPath(node);
            break;
        case SYSTEM_FOLDER:
            pathToSave = new Path(""); //$NON-NLS-1$
            break;
        }

        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case SYSTEM_FOLDER:
            connection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
            connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
            connectionProperty.setAuthor(((RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                    .getProperty(Context.REPOSITORY_CONTEXT_KEY)).getUser());
            connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
            connectionProperty.setStatusCode(""); //$NON-NLS-1$

            connectionItem = PropertiesFactory.eINSTANCE.createDatabaseConnectionItem();
            connectionItem.setProperty(connectionProperty);
            connectionItem.setConnection(connection);
            break;

        case REPOSITORY_ELEMENT:
            connection = ((ConnectionItem) node.getObject().getProperty().getItem()).getConnection();
            connectionProperty = node.getObject().getProperty();
            connectionItem = (ConnectionItem) node.getObject().getProperty().getItem();
            propertyId = connectionProperty.getId();
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            setRepositoryObject(node.getObject());
            isRepositoryObjectEditable();
            initLockStrategy();
            break;
        }
        if (!creation) {
            this.originaleObjectLabel = this.connectionItem.getProperty().getDisplayName();
            this.originalVersion = this.connectionItem.getProperty().getVersion();
            this.originalDescription = this.connectionItem.getProperty().getDescription();
            this.originalPurpose = this.connectionItem.getProperty().getPurpose();
            this.originalStatus = this.connectionItem.getProperty().getStatusCode();

            if (this.connection != null && (this.connection instanceof DatabaseConnection)) {
                this.originalSid = ((DatabaseConnection)this.connection).getSID();
                this.originalUiSchema = ((DatabaseConnection)this.connection).getUiSchema();
            }
        }

        repFactory = ProxyRepositoryFactory.getInstance();
        if (creation) {
            propertyId = repFactory.getNextId();
            connectionProperty.setId(propertyId);
        } else {
            propertyId = connectionProperty.getId();
        }
        connection.setId(propertyId);

        // initialize the context mode
        ConnectionContextHelper.checkContextMode(connectionItem);
        this.originalConnectionItem = connectionItem;
    }

    /**
     * Constructor for DatabaseWizard. Analyse Iselection to extract DatabaseConnection and the pathToSave. Start the
     * Lock Strategy.
     * 
     * @param selection
     * @param existingNames
     */
    public DatabaseWizard(IWorkbench workbench, boolean creation, RepositoryNode node, String[] existingNames) {
        super(workbench, creation);
        this.existingNames = existingNames;
        setNeedsProgressMonitor(true);
        this.node = node;
        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case REPOSITORY_ELEMENT:
            pathToSave = RepositoryNodeUtilities.getPath(node);
            break;
        case SYSTEM_FOLDER:
            pathToSave = new Path(""); //$NON-NLS-1$
            break;
        }

        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case SYSTEM_FOLDER:
            connection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
            connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
            connectionProperty.setAuthor(((RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                    .getProperty(Context.REPOSITORY_CONTEXT_KEY)).getUser());
            connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
            connectionProperty.setStatusCode(""); //$NON-NLS-1$

            connectionItem = PropertiesFactory.eINSTANCE.createDatabaseConnectionItem();
            connectionItem.setProperty(connectionProperty);
            connectionItem.setConnection(connection);
            break;

        case REPOSITORY_ELEMENT:
            connection = ((ConnectionItem) node.getObject().getProperty().getItem()).getConnection();
            connectionProperty = node.getObject().getProperty();
            connectionItem = (ConnectionItem) node.getObject().getProperty().getItem();

            // set the repositoryObject, lock and set isRepositoryObjectEditable
            setRepositoryObject(node.getObject());
            isRepositoryObjectEditable();
            initLockStrategy();
            break;
        }
        if (!creation) {
            this.originaleObjectLabel = this.connectionItem.getProperty().getDisplayName();
            this.originalVersion = this.connectionItem.getProperty().getVersion();
            this.originalDescription = this.connectionItem.getProperty().getDescription();
            this.originalPurpose = this.connectionItem.getProperty().getPurpose();
            this.originalStatus = this.connectionItem.getProperty().getStatusCode();

            if (this.getDatabaseConnection() != null) {
                this.originalSid = this.getDatabaseConnection().getSID();
                this.originalUiSchema = this.getDatabaseConnection().getUiSchema();
            }
        }
        if(getDatabaseConnection() != null){
            originalHCId = getDatabaseConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HADOOP_CLUSTER_ID);
        }

        repFactory = ProxyRepositoryFactory.getInstance();
        if (creation) {
            propertyId = repFactory.getNextId();
            connectionProperty.setId(propertyId);
        } else {
            propertyId = connectionProperty.getId();
        }
        connection.setId(propertyId);

        // initialize the context mode
        ConnectionContextHelper.checkContextMode(connectionItem);
        this.originalConnectionItem = connectionItem;
    }

    /**
     * DOC ycbai DatabaseWizard constructor comment.
     * 
     * <p>
     * If you want to initialize connection before creation you can use this constructor.
     * </p>
     * 
     * @param workbench
     * @param creation
     * @param node
     * @param existingNames
     * @param parameters initial values to initialize connection.
     */
    public DatabaseWizard(IWorkbench workbench, boolean creation, RepositoryNode node, String[] existingNames,
            Map<String, String> parameters) {
        this(workbench, creation, node, existingNames);
        initConnection(parameters);
        if(getDatabaseConnection() != null){
            originalHCId = getDatabaseConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HADOOP_CLUSTER_ID);
        }
    }

    private void initConnection(Map<String, String> parameters) {
        if (parameters == null || parameters.size() == 0) {
            return;
        }
        if(getDatabaseConnection() == null){
            return;
        }
        EMap<String, String> connParameters = getDatabaseConnection().getParameters();
        Iterator<Entry<String, String>> iter = parameters.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            connParameters.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * yzhang Comment method "setToolBar".
     * 
     * @param isToolbar
     */
    public void setToolBar(boolean isToolbar) {
        this.isToolBar = isToolbar;
    }

    /**
     * Adding the page to the wizard and set Title, Description and PageComplete.
     */
    @Override
    public void addPages() {
        setWindowTitle(Messages.getString("DatabaseWizard.windowTitle")); //$NON-NLS-1$
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.METADATA_CONNECTION_WIZ));
        if (isToolBar) {
            pathToSave = null;
        }
        propertiesWizardPage = new Step0WizardPage(connectionProperty, pathToSave, ERepositoryObjectType.METADATA_CONNECTIONS,
                !isRepositoryObjectEditable(), creation);
        databaseWizardPage = new DatabaseWizardPage(connectionItem, isRepositoryObjectEditable(), existingNames);

        if (creation) {
            propertiesWizardPage.setTitle(Messages.getString("DatabaseWizardPage.titleCreate.Step1")); //$NON-NLS-1$
            propertiesWizardPage.setDescription(Messages.getString("DatabaseWizardPage.descriptionCreate.Step1")); //$NON-NLS-1$
            propertiesWizardPage.setPageComplete(false);

            databaseWizardPage.setTitle(Messages.getString("DatabaseWizardPage.titleCreate.Step2")); //$NON-NLS-1$
            databaseWizardPage.setDescription(Messages.getString("DatabaseWizardPage.descriptionCreate.Step2")); //$NON-NLS-1$
            databaseWizardPage.setPageComplete(false);
        } else {
            boolean isTCOM = databaseWizardPage.isTCOMDB(connectionItem.getTypeName());
            
            propertiesWizardPage.setTitle(Messages.getString("DatabaseWizardPage.titleUpdate.Step1")); //$NON-NLS-1$
            propertiesWizardPage.setDescription(Messages.getString("DatabaseWizardPage.descriptionUpdate.Step1")); //$NON-NLS-1$
            propertiesWizardPage.setPageComplete(isRepositoryObjectEditable() && !isTCOM);

            databaseWizardPage.setTitle(Messages.getString("DatabaseWizardPage.titleUpdate.Step2")); //$NON-NLS-1$
            databaseWizardPage.setDescription(Messages.getString("DatabaseWizardPage.descriptionUpdate.Step2")); //$NON-NLS-1$
            databaseWizardPage.setPageComplete(isRepositoryObjectEditable() && !isTCOM);
        }
        addPage(propertiesWizardPage);
        addPage(databaseWizardPage);
    }

    private String getHadoopPropertiesString(List<HashMap<String, Object>> hadoopPrperties) throws JSONException {
        JSONArray jsonArr = new JSONArray();
        for (HashMap<String, Object> map : hadoopPrperties) {
            JSONObject object = new JSONObject();
            Iterator it = map.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                object.put(key, map.get(key));
            }
            jsonArr.put(object);
        }
        return jsonArr.toString();
    }

    private IHadoopDistributionService getHadoopDistributionService() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IHadoopDistributionService.class)) {
            return (IHadoopDistributionService) GlobalServiceRegister.getDefault().getService(IHadoopDistributionService.class);
        }
        return null;
    }

    /**
     * This method is called when 'Finish' button is pressed in the wizard. Save metadata close Lock Strategy and close
     * wizard.
     */
    @Override
    public boolean performFinish() {
        if (databaseWizardPage.isPageComplete()) {
            /*
             * if create connection in TOS with context model,should use the original value when create catalog or //
             * schema,see bug 0016636,using metadataConnection can be sure that all the values has been parse to
             * original. hywang
             */
            deleteSwitchTypeNode();
            // use the context group of selected on check button to check the selection in perform finish.
            String contextName = null;
            if (databaseWizardPage.getSelectedContextType() != null) {
                contextName = databaseWizardPage.getSelectedContextType().getName();
            }
            if(isTCOMType(getDBType(connectionItem))){
                IGenericDBService dbService = null;
                if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericDBService.class)) {
                    dbService = (IGenericDBService) GlobalServiceRegister.getDefault().getService(
                            IGenericDBService.class);
                }
                if(dbService == null){
                    return false;
                }
                try {
                    dbService.dbWizardPerformFinish(connectionItem, databaseWizardPage.getForm(), isCreation(), pathToSave, new ArrayList<IMetadataTable>(),contextName);
                    closeLockStrategy();
                } catch (CoreException e) {
                    new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("CommonWizard.persistenceException"), //$NON-NLS-1$
                            e.toString());
                    log.error(Messages.getString("CommonWizard.persistenceException") + "\n" + e.toString()); //$NON-NLS-1$ //$NON-NLS-2$
                    return false;
                }
                return true;
            }
            
            // MOD by gdbu 2011-3-24 bug 19528
            EDatabaseTypeName dbType = EDatabaseTypeName.getTypeFromDbType(getDBType(connectionItem));
            if (dbType != EDatabaseTypeName.GENERAL_JDBC) {
                String driverClass = ExtractMetaDataUtils.getInstance().getDriverClassByDbType(getDBType(connectionItem));
                DatabaseConnection dbConnection = (DatabaseConnection) connectionItem.getConnection();
                String dbVersion = dbConnection.getDbVersionString();
                // feature TDI-22108
                if (EDatabaseTypeName.VERTICA.equals(dbType)
                        && (EDatabaseVersion4Drivers.VERTICA_6.getVersionValue().equals(dbVersion)
                                || EDatabaseVersion4Drivers.VERTICA_5_1.getVersionValue().equals(dbVersion)
                                || EDatabaseVersion4Drivers.VERTICA_6_1_X.getVersionValue().equals(dbVersion) || EDatabaseVersion4Drivers.VERTICA_7
                                .getVersionValue().equals(dbVersion) || EDatabaseVersion4Drivers.VERTICA_9
                                .getVersionValue().equals(dbVersion))) {
                    driverClass = EDatabase4DriverClassName.VERTICA2.getDriverClass();
                } else if (EDatabaseTypeName.IMPALA.equals(dbType)) {
                    IHadoopDistributionService hadoopService = getHadoopDistributionService();
                    if (hadoopService != null) {
                        String distributionName = dbConnection.getParameters().get(
                                ConnParameterKeys.CONN_PARA_KEY_IMPALA_DISTRIBUTION);
                        IHDistribution impalaDistribution = hadoopService.getImpalaDistributionManager().getDistribution(
                                distributionName, false);
                        if (null != impalaDistribution && !impalaDistribution.useCustom()) {
                            dbConnection.setDbVersionString(dbConnection.getParameters().get(
                                    ConnParameterKeys.CONN_PARA_KEY_IMPALA_VERSION));
                        }
                    }
                } else if (EDatabaseTypeName.MYSQL.equals(dbType)) {
                    if (EDatabaseVersion4Drivers.MYSQL_8.getVersionValue().equals(dbVersion)) {
                        driverClass = EDatabase4DriverClassName.MYSQL8.getDriverClass();
                    } else if (EDatabaseVersion4Drivers.MARIADB.getVersionValue().equals(dbVersion)) {
                        driverClass = EDatabase4DriverClassName.MARIADB.getDriverClass();
                    }
                } else if (EDatabaseTypeName.MSSQL.equals(dbType)
                        && EDatabaseVersion4Drivers.MSSQL_PROP.getVersionValue().equals(dbVersion)) {
                    driverClass = EDatabase4DriverClassName.MSSQL2.getDriverClass();
                } else if (EDatabaseTypeName.SYBASEASE.equals(dbType)
                        && EDatabaseVersion4Drivers.SYBASEIQ_16.getVersionValue().equals(dbVersion)) {
                    driverClass = EDatabase4DriverClassName.SYBASEIQ_16.getDriverClass();
                }
                else if (EDatabaseTypeName.SYBASEASE.equals(dbType)
                        && EDatabaseVersion4Drivers.SYBASEIQ_16_SA.getVersionValue().equals(dbVersion)) {
                    driverClass = EDatabase4DriverClassName.SYBASEIQ_16_SA.getDriverClass();
                }
                dbConnection.setDriverClass(driverClass);
            }
        
            // ~19528

        
            IMetadataConnection metadataConnection = null;
            if (contextName == null) {
                metadataConnection = ConvertionHelper.convert(connection, true);
            } else {
                metadataConnection = ConvertionHelper.convert(connection, false, contextName);
            }

            ITDQRepositoryService tdqRepService = null;

            if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQRepositoryService.class)) {
                tdqRepService = (ITDQRepositoryService) GlobalServiceRegister.getDefault()
                        .getService(ITDQRepositoryService.class);
            }

            if (getDatabaseConnection() !=null && !connection.isContextMode()) {
                handleUppercase(getDatabaseConnection(), metadataConnection);
            }
            try {
                // TODO use seperate subclass to handle the create and update logic , using a varable "creation" is not
                // a good practice.
                if (creation && getDatabaseConnection() != null) {
                    handleCreation(getDatabaseConnection(), metadataConnection, tdqRepService);
                } else {
                    Boolean isSuccess = handleUpdate(metadataConnection, tdqRepService);
                    if (!isSuccess) {
                        return false;
                    }
                }
            } catch (Exception e) {
                String detailError = e.toString();
                new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("CommonWizard.persistenceException"), //$NON-NLS-1$
                        detailError);
                log.error(Messages.getString("CommonWizard.persistenceException") + "\n" + detailError); //$NON-NLS-1$ //$NON-NLS-2$
                return false;
            }
            List<IRepositoryViewObject> list = new ArrayList<IRepositoryViewObject>();
            list.add(repositoryObject);
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IRepositoryService.class)) {
                IRepositoryService service = (IRepositoryService) GlobalServiceRegister.getDefault().getService(
                        IRepositoryService.class);
                service.notifySQLBuilder(list);
            }

            if (tdqRepService != null) {
                // MOD qiongli 2012-11-19 TDQ-6287
                if (creation) {
                    tdqRepService.notifySQLExplorer(connectionItem);
                    tdqRepService.openConnectionEditor(connectionItem);
                } else {
                    tdqRepService.updateAliasInSQLExplorer(connectionItem, originaleObjectLabel);
                    // refresh the opened connection editor whatever is in DI or DQ perspective.
                    tdqRepService.refreshConnectionEditor(connectionItem);

                }
                if (CoreRuntimePlugin.getInstance().isDataProfilePerspectiveSelected()) {
                    tdqRepService.refresh(node.getParent());
                }
            }

            refreshHadoopCluster();
            RelationshipItemBuilder.getInstance().addOrUpdateItem(connectionItem);

            return true;
        } else {
            return false;
        }
    }
    
    private void deleteSwitchTypeNode(){
        if(isCreation()){
            return;
        }
        if(originalConnectionItem == null){
            return;
        }
        if(isTCOMType(getDBType(originalConnectionItem)) != isTCOMType(getDBType(connectionItem))){
            creation = true;
            try {
                if(repositoryObject != null){
                    repFactory.deleteObjectPhysical(repositoryObject);
                }
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
        }
        
    }
    
    private boolean isTCOMType(String dbType){
        List<ERepositoryObjectType> extraTypes = new ArrayList<ERepositoryObjectType>();
        IGenericDBService dbService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericDBService.class)) {
            dbService = (IGenericDBService) GlobalServiceRegister.getDefault().getService(
                    IGenericDBService.class);
        }
        if(dbService != null){
            extraTypes.addAll(dbService.getExtraTypes());
        }
        for(ERepositoryObjectType type:extraTypes){
            if(type.getType().equals(dbType)){
                return true;
            }
        }
        return false;
    }

    /**
     * if the database if oracle uppercase the ui schema of it; if the database if netezza uppercase the sid and url of
     * it.
     * 
     * @param databaseConnection
     * @param metadataConnection
     */
    private void handleUppercase(DatabaseConnection databaseConnection, IMetadataConnection metadataConnection) {
        if (StringUtils.equals(databaseConnection.getProductId(), EDatabaseTypeName.ORACLEFORSID.getProduct())) {
            if (databaseConnection.getUiSchema() == null) {
                databaseConnection.setUiSchema(""); //$NON-NLS-1$
            } else {
                databaseConnection.setUiSchema(databaseConnection.getUiSchema().toUpperCase());
            }
            if (metadataConnection != null) {
                metadataConnection.setUiSchema(databaseConnection.getUiSchema());
            }
        }
        if (StringUtils.equals(databaseConnection.getProductId(), EDatabaseTypeName.NETEZZA.getProduct())
                || MetadataFillFactory.isJdbcNetezza(metadataConnection)) {
            uppercaseNetezzaSidUrl(databaseConnection);
            if (metadataConnection != null) {
                metadataConnection.setDatabase(databaseConnection.getSID());
                metadataConnection.setUrl(databaseConnection.getURL());
            }
        }
    }

    /**
     * DOC zhao Comment method "handleUpdate".
     * 
     * @param metadataConnection
     * @param tdqRepService
     * @return True if handled successfully.
     */
    private Boolean handleUpdate(IMetadataConnection metadataConnection, ITDQRepositoryService tdqRepService) throws Exception {
        boolean isNameModified = propertiesWizardPage.isNameModifiedByUser();
        // Add this parameter to control only ask user refresh the opened analysis once, TDQ-7438 20130628
        // yyin
        boolean isNeedRefreshEditor = false;// ~
        if (connectionItem.getConnection() instanceof DatabaseConnection) {
            DatabaseConnection conn = (DatabaseConnection) connectionItem.getConnection();
            ReturnCode reloadCheck = new ReturnCode(false);
            ITDQCompareService tdqCompareService = null;

            if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQCompareService.class)) {
                tdqCompareService = (ITDQCompareService) GlobalServiceRegister.getDefault().getService(ITDQCompareService.class);
            }
            if (tdqCompareService != null && ConnectionHelper.isUrlChanged(conn)
                    && MetadataConnectionUtils.isTDQSupportDBTemplate(conn)) {
                reloadCheck = openConfirmReloadConnectionDialog(Display.getCurrent().getActiveShell());
                if (!reloadCheck.isOk()) {
                    return false;
                }
            }
            // bug 20700
            if (reloadCheck.isOk()) {
                if (needReload(reloadCheck.getMessage())) {
                    if (tdqCompareService != null) {
                        // When TDQ comparison service available, relaod the database.
                        Boolean isReloadSuccess = reloadDatabase(isNameModified, tdqCompareService, tdqRepService);
                        if (!isReloadSuccess) {
                            return Boolean.FALSE;
                        }
                        isNeedRefreshEditor = true;
                    }
                }
            } else {
                DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(conn);
                if (dbConn != null) {
                    updateConnectionInformation(dbConn, metadataConnection);
                }
            }
            // update
            RepositoryUpdateManager.updateDBConnection(connectionItem);
        }

        this.connection.setName(connectionProperty.getDisplayName());
        this.connection.setLabel(connectionProperty.getDisplayName());

        // Modified by Marvin Wang on Apr. 40, 2012 for bug TDI-20744
        // factory.save(connectionItem);
        // 0005170: Schema renamed - new name not pushed out to dependant jobs
        updateTdqDependencies();
        // MOD yyin 20121115 TDQ-6395, save all dependency of the connection when the name is changed.
        if (isNameModified && tdqRepService != null) {
            tdqRepService.saveConnectionWithDependency(connectionItem);
            closeLockStrategy();
        } else {
            updateConnectionItem();
        }
        // MOD 20130628 TDQ-7438, If the analysis editor is opened, popup the dialog which ask user refresh
        // the editor or not once should enough(use hasReloaded to control,because the reload will refresh)
        if (tdqRepService != null && !isNeedRefreshEditor && (isNameModified || IsVersionChange())) {
            tdqRepService.refreshCurrentAnalysisEditor(connectionItem);
        }
        // ~

        if (isNameModified) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreService.class)) {
                IDesignerCoreService service = (IDesignerCoreService) GlobalServiceRegister.getDefault().getService(
                        IDesignerCoreService.class);
                if (service != null) {
                    service.refreshComponentView(connectionItem);
                }
            }

        }
        return Boolean.TRUE;
    }

    /**
     * Note that normally this method will only be usefull when reloading database structure from real database in DQ
     * perspective.<br>
     * The caller must check if the DQ service extentions is avaible or not.
     */
    private Boolean reloadDatabase(Boolean isNameModified, ITDQCompareService tdqCompareService,
            ITDQRepositoryService tdqRepService) {
        // MOD 20130627 TDQ-7438 yyin: if the db name is changed, the db can not be reload
        // properly, so if the name is changed, make sure that the reload action use the old
        // name to reload
        String tempName = null;
        if (isNameModified) {
            tempName = connectionProperty.getLabel();
            connectionProperty.setLabel(connectionItem.getConnection().getLabel());
            connectionProperty.setDisplayName(connectionItem.getConnection().getLabel());
        }
        ReturnCode retCode = tdqCompareService.reloadDatabase(connectionItem);
        connection = connectionItem.getConnection();
        if (isNameModified) {
            connectionProperty.setLabel(tempName);
            connectionProperty.setDisplayName(tempName);
        }// ~
        if (!retCode.isOk()) {
            return Boolean.FALSE;
        } else {
            // Update the software system.
            if (tdqRepService != null && getDatabaseConnection() != null) {
                // Update software system when TDQ service available.
                tdqRepService.publishSoftwareSystemUpdateEvent(getDatabaseConnection());
            }

        }
        return Boolean.TRUE;
    }

    /**
     * DOC zhao Comment method "handleCreation".
     * 
     * @param databaseConnection
     * @param metadataConnection
     * @param tdqRepService
     * @throws PersistenceException
     */
    private void handleCreation(DatabaseConnection databaseConnection, IMetadataConnection metadataConnection,
            ITDQRepositoryService tdqRepService) throws PersistenceException {
        connectionProperty.setId(propertyId);
        EDatabaseTypeName type = EDatabaseTypeName.getTypeFromDbType(metadataConnection.getDbType());
        String displayName = connectionProperty.getDisplayName();
        this.connection.setName(displayName);
        this.connection.setLabel(displayName);

        if (tdqRepService != null) {
            tdqRepService.checkUsernameBeforeSaveConnection(connectionItem);
        }
        repFactory.create(connectionItem, propertiesWizardPage.getDestinationPath());

        // MOD yyi 2011-04-14:20362 reload connection
        ConnectionHelper.setIsConnNeedReload(connection, Boolean.FALSE);
        // MOD klliu 2012-02-08 TDQ-4645 add package filter for connection
        ConnectionHelper.setPackageFilter(connection, "");//$NON-NLS-1$

        String hiveMode = (String) metadataConnection.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_MODE);
        if (EDatabaseTypeName.HIVE.getDisplayName().equals(metadataConnection.getDbType())
                && HiveModeInfo.get(hiveMode) == HiveModeInfo.EMBEDDED) {
            JavaSqlFactory.doHivePreSetup((DatabaseConnection) metadataConnection.getCurrentConnection());
        }
        List<Catalog> catalogs = ConnectionHelper.getCatalogs(connection);
        List<Schema> schemas = ConnectionHelper.getSchema(connection);
        if (catalogs.isEmpty() && schemas.isEmpty()) {
            IDBMetadataProvider extractor = ExtractMetaDataFromDataBase.getProviderByDbType(metadataConnection.getDbType());
            if (extractor != null && type.isUseProvider() && getDatabaseConnection() != null) {
                extractor.fillConnection(getDatabaseConnection());
                repFactory.save(connectionItem);
            }
        }
        MetadataConnectionUtils.fillConnectionInformation(connectionItem, metadataConnection);
        if (tdqRepService != null) {
            // Update software system when TDQ service available.
            tdqRepService.publishSoftwareSystemUpdateEvent(databaseConnection);
        }
    }

    /**
     * uppercase the sid and url of Netezza connection.
     * 
     * @param netezzaConnection
     */
    private void uppercaseNetezzaSidUrl(DatabaseConnection netezzaConnection) {
        if (netezzaConnection == null) {
            return;
        }
        netezzaConnection.setSID(StringUtils.upperCase(netezzaConnection.getSID()));
        String url = netezzaConnection.getURL();
        if (StringUtils.isBlank(url)) {
            return;
        }
        int lastIndexOf = StringUtils.lastIndexOf(url, "/"); //$NON-NLS-1$
        if (lastIndexOf > 0 && lastIndexOf < url.length() - 1) {
            String part1 = StringUtils.substring(url, 0, lastIndexOf + 1);
            String part2 = StringUtils.substring(url, lastIndexOf + 1);
            if (!StringUtils.isEmpty(part2)) {
                int indexOf = StringUtils.indexOf(part2, "?"); //$NON-NLS-1$
                if (indexOf > -1) {
                    String sid = StringUtils.substring(part2, 0, indexOf);
                    part2 = StringUtils.upperCase(sid) + StringUtils.substring(part2, indexOf, part2.length());
                } else {
                    part2 = StringUtils.upperCase(part2);
                }
                netezzaConnection.setURL(part1 + part2);
            }
        }
    }

    private void refreshHadoopCluster() {
        IHadoopClusterService hadoopClusterService = HadoopRepositoryUtil.getHadoopClusterService();
        if (hadoopClusterService != null && getDatabaseConnection() != null) {
            String hcId = getDatabaseConnection().getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HADOOP_CLUSTER_ID);
            if (hcId != null) {
                hadoopClusterService.refreshCluster(hcId);
            } else if (originalHCId != null) {
                hadoopClusterService.refreshCluster(originalHCId);
            }
        }
    }

    /**
     * DOC xqliu Comment method "updateTdqDependencies".
     */
    private void updateTdqDependencies() {
        if (connectionItem != null) {
            if (IsVersionChange()) {
                AbstractResourceChangesService resChangeService = TDQServiceRegister.getInstance().getResourceChangeService(
                        AbstractResourceChangesService.class);
                if (resChangeService != null) {
                    resChangeService.updateDependeciesWhenVersionChange(connectionItem, this.originalVersion, connectionItem
                            .getProperty().getVersion());
                }
            }
        }
    }

    private boolean IsVersionChange() {
        String oldVersion = this.originalVersion;
        String newVersioin = connectionItem.getProperty().getVersion();
        if (oldVersion != null && newVersioin != null && !newVersioin.equals(oldVersion)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean performCancel() {
        if (!creation) {
            connectionItem = originalConnectionItem;
            connectionItem.getProperty().setVersion(this.originalVersion);
            connectionItem.getProperty().setDisplayName(this.originaleObjectLabel);
            connectionItem.getProperty().setDescription(this.originalDescription);
            connectionItem.getProperty().setPurpose(this.originalPurpose);
            connectionItem.getProperty().setStatusCode(this.originalStatus);
            
            if(connectionItem.getConnection() instanceof DatabaseConnection){
                DBConnectionContextUtils.setDatabaseConnectionParameter((DatabaseConnection) connectionItem.getConnection(),
                        databaseWizardPage.getMetadataConnection());
            }
        }
        return super.performCancel();
    }
    
    public void setNewConnectionItem(ConnectionItem connectionItem){
        this.connection = connectionItem.getConnection();
        this.connectionItem = connectionItem;
        this.connectionProperty = connectionItem.getProperty();
        propertiesWizardPage.setProperty(this.connectionProperty);
    }

    /**
     * We will accept the selection in the workbench to see if we can initialize from it.
     * 
     * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
     */
    @Override
    public void init(final IWorkbench workbench, final IStructuredSelection selection2) {
        super.setWorkbench(workbench);
        this.selection = selection2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.RepositoryWizard#getConnectionItem()
     */
    @Override
    public ConnectionItem getConnectionItem() {
        return this.connectionItem;
    }

    /**
     * 
     * DOC Comment method "updateConnectionInformation".
     * 
     * @param dbConn
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    private void updateConnectionInformation(DatabaseConnection dbConn, IMetadataConnection metaConnection)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        java.sql.Connection sqlConn = null;
        ExtractMetaDataUtils extractMeta = ExtractMetaDataUtils.getInstance();
        String orginalUrl = dbConn.getURL();
        try {
            MetadataFillFactory dbInstance = MetadataFillFactory.getDBInstance(metaConnection);
            dbConn = (DatabaseConnection) dbInstance.fillUIConnParams(metaConnection, dbConn);
            sqlConn = MetadataConnectionUtils.createConnection(metaConnection).getObject();

            if (sqlConn != null) {
                String dbType = metaConnection.getDbType();
                DatabaseMetaData dbMetaData = null;
                // Added by Marvin Wang on Mar. 13, 2013 for loading hive jars dynamically, refer to TDI-25072.
                if (EDatabaseTypeName.HIVE.getXmlName().equalsIgnoreCase(dbType)) {
                    dbMetaData = HiveConnectionManager.getInstance().extractDatabaseMetaData(metaConnection);
                } else {
                    dbMetaData = extractMeta.getDatabaseMetaData(sqlConn, dbType, false, metaConnection.getDatabase());
                }
                dbInstance.fillCatalogs(dbConn, dbMetaData, metaConnection,
                        MetadataConnectionUtils.getPackageFilter(dbConn, dbMetaData, true));
                dbInstance.fillSchemas(dbConn, dbMetaData, metaConnection,
                        MetadataConnectionUtils.getPackageFilter(dbConn, dbMetaData, false));
            }
        } finally {
            EDatabaseTypeName dbType = EDatabaseTypeName.getTypeFromDbType(dbConn.getDatabaseType());
            if (EDatabaseTypeName.HIVE.equals(dbType)) {
                // in case show password dirrectly
                dbConn.setURL(orginalUrl);
            }
            // bug 22619
            if (sqlConn != null) {
                ConnectionUtils.closeConnection(sqlConn);
            }
            MetadataConnectionUtils.closeDerbyDriver();
        }
    }

    /**
     * reload the connection
     */
    public static final String RELOAD_FLAG_TRUE = "RELOAD";

    /**
     * don't reload the connection
     */
    public static final String RELOAD_FLAG_FALSE = "UNRELOAD";

    /**
     * judgement reload the connection or not
     * 
     * @param reloadFlag
     * @return
     */
    public static boolean needReload(String reloadFlag) {
        return RELOAD_FLAG_TRUE.equals(reloadFlag);
    }

    /**
     * open the confirm dialog
     * 
     * @param shell
     * @return
     */
    public static ReturnCode openConfirmReloadConnectionDialog(Shell shell) {
        ReturnCode result = new ReturnCode(false);
        ConfirmReloadConnectionDialog dialog = new ConfirmReloadConnectionDialog(shell);
        if (dialog.open() == Window.OK) {
            result.setOk(true);
            result.setMessage(dialog.getReloadFlag());
        }
        return result;
    }

    /**
     * replace the package(catalog and/or schema) name with the new name if needed.
     * 
     * @param dbConnection
     */
    private void relpacePackageName(DatabaseConnection dbConnection) {
        if (dbConnection != null) {
            String newSid = dbConnection.getSID();
            String newSchema = dbConnection.getUiSchema();

            boolean replaceCatalog = this.originalSid != null && !PluginConstant.EMPTY_STRING.equals(this.originalSid)
                    && newSid != null && !PluginConstant.EMPTY_STRING.equals(newSid) && !this.originalSid.equals(newSid);
            boolean replaceSchema = this.originalUiSchema != null && !PluginConstant.EMPTY_STRING.equals(this.originalUiSchema)
                    && newSchema != null && !PluginConstant.EMPTY_STRING.equals(newSchema)
                    && !this.originalUiSchema.equals(newSchema);

            String dbType = EDatabaseTypeName.getTypeFromDbType(dbConnection.getDatabaseType()).getDisplayName();

            if (SupportDBUrlType.justHaveCatalog(dbType)) { // catalog only
                if (replaceCatalog) {
                    EList<Package> dataPackage = dbConnection.getDataPackage();
                    for (Package pckg : dataPackage) {
                        String name = pckg.getName();
                        if (name != null && name.equals(this.originalSid)) {
                            pckg.setName(newSid);
                        }
                    }
                }
            } else if (SupportDBUrlType.justHaveSchema(dbType)) { // schema only
                if (replaceSchema) {
                    EList<Package> dataPackage = dbConnection.getDataPackage();
                    for (Package pckg : dataPackage) {
                        String name = pckg.getName();
                        if (name != null && name.equals(this.originalUiSchema)) {
                            pckg.setName(newSchema);
                        }
                    }
                }
            } else { // both catalog and schema
                EList<Package> dataPackage = dbConnection.getDataPackage();
                for (Package pckg : dataPackage) {
                    if (pckg instanceof Catalog) {

                        Catalog catalog = (Catalog) pckg;
                        String catalogName = catalog.getName();
                        boolean sameCatalog = catalogName != null && catalogName.equals(this.originalSid);

                        if (sameCatalog) {
                            // replace catalog name if needed
                            if (replaceCatalog) {
                                catalog.setName(newSid);
                            }

                            // replace schema name if needed
                            EList<ModelElement> ownedElement = catalog.getOwnedElement();
                            for (ModelElement me : ownedElement) {
                                if (me instanceof Schema) {
                                    if (replaceSchema) {
                                        Schema schema = (Schema) me;
                                        String schemaName = schema.getName();
                                        if (schemaName != null && schemaName.equals(this.originalUiSchema)) {
                                            schema.setName(newSchema);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    /**
     * TDQ-7217 if the related connection editor is opened in DQ side,shoud not unlock.
     */
    public void closeLockStrategy() {
        ITDQRepositoryService tdqRepService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQRepositoryService.class)) {
            tdqRepService = (ITDQRepositoryService) GlobalServiceRegister.getDefault().getService(ITDQRepositoryService.class);
        }
        if (tdqRepService != null && tdqRepService.isDQEditorOpened(connectionItem)) {
            tdqRepService.refreshConnectionEditor(connectionItem);
            return;
        }
        super.closeLockStrategy();
    }
    
    private DatabaseConnection getDatabaseConnection(){
        if(connection instanceof DatabaseConnection){
            return (DatabaseConnection)connection;
        }
        return null;
    }
    
    private String getDBType(ConnectionItem item){
        if(item.getConnection() instanceof DatabaseConnection){
            return ((DatabaseConnection)item.getConnection()).getDatabaseType();
        }
        return item.getTypeName();
    }
}
