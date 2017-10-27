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
package org.talend.repository.ui.wizards.metadata.table.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.utils.platform.PluginChecker;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ITDQRepositoryService;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.metadata.builder.database.TableInfoParameters;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.update.EUpdateResult;
import org.talend.core.model.update.RepositoryUpdateManager;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.metadata.managment.repository.ManagerConnection;
import org.talend.metadata.managment.ui.wizard.CheckLastVersionRepositoryWizard;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import orgomg.cwm.objectmodel.core.Package;

/**
 * TableWizard present the TableForm width the MetaDataTable. Use to create a new table (need a connection to a DB).
 */

public class DatabaseTableWizard extends CheckLastVersionRepositoryWizard implements INewWizard {

    private static Logger log = Logger.getLogger(DatabaseTableWizard.class);

    private SelectorTableWizardPage selectorWizardPage;

    private DatabaseTableWizardPage tableWizardpage;

    private DatabaseTableFilterWizardPage tableFilterWizardPage;

    private DatabaseConnection oldCopiedConnection;

    private boolean skipStep;

    private final ManagerConnection managerConnection;

    private Map<String, String> oldTableMap;

    private IMetadataConnection metadataConnection;

    private List<IMetadataTable> oldMetadataTable;

    private MetadataTable selectedMetadataTable;

    /* hywang add for 0017426,catches used to store the uuids and labels of old tables and columns */
    private static Map<String, String> originalColumnsMap = new HashMap<String, String>();

    private static Map<String, String> originalTablesMap = new HashMap<String, String>();

    private final ConnectionUUIDHelper tableHelper;

    /**
     * DOC ocarbone DatabaseTableWizard constructor comment.
     * 
     * @param workbench
     * @param idNodeDbConnection
     * @param metadataTable
     * @param existingNames
     * @param managerConnection
     */
    @SuppressWarnings("unchecked")
    public DatabaseTableWizard(IWorkbench workbench, boolean creation, IRepositoryViewObject object, MetadataTable metadataTable,
            String[] existingNames, boolean forceReadOnly, ManagerConnection managerConnection,
            IMetadataConnection metadataConnection) {
        super(workbench, creation, forceReadOnly);
        this.existingNames = existingNames;
        this.managerConnection = managerConnection;
        this.metadataConnection = metadataConnection;
        setNeedsProgressMonitor(true);

        // set the repositoryObject, lock and set isRepositoryObjectEditable
        setRepositoryObject(object);
        isRepositoryObjectEditable();
        initLockStrategy();
        this.selectedMetadataTable = metadataTable;
        this.connectionItem = (ConnectionItem) object.getProperty().getItem();
        this.tableHelper = new ConnectionUUIDHelper((DatabaseConnection) this.connectionItem.getConnection());
        if (connectionItem != null) {
            this.tableHelper.recordConnection();
            oldTableMap = RepositoryUpdateManager.getOldTableIdAndNameMap(connectionItem, metadataTable, creation);
            oldMetadataTable = RepositoryUpdateManager.getConversionMetadataTables(connectionItem.getConnection());
            cloneBaseDataBaseConnection((DatabaseConnection) connectionItem.getConnection());
        }
        originalColumnsMap.clear();
        originalTablesMap.clear();
    }

    /**
     * DOC acer Comment method "setSkipStep".
     * 
     * @param skipStep
     */
    public void setSkipStep(boolean skipStep) {
        this.skipStep = skipStep;
    }

    /**
     * Adding the page to the wizard.
     */
    @Override
    public void addPages() {
        setWindowTitle(Messages.getString("TableWizard.windowTitle")); //$NON-NLS-1$
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.METADATA_TABLE_WIZ));
        TableInfoParameters tableInfoParameters = new TableInfoParameters();
        DatabaseConnection curDbConnection = (DatabaseConnection) connectionItem.getConnection();

        tableWizardpage = new DatabaseTableWizardPage(selectedMetadataTable, managerConnection, connectionItem,
                isRepositoryObjectEditable(), metadataConnection, curDbConnection);
        tableWizardpage.setWizard(this);
        if (creation && !skipStep) {
            selectorWizardPage = new SelectorTableWizardPage(connectionItem, isRepositoryObjectEditable(), tableInfoParameters,
                    metadataConnection, curDbConnection);
            tableFilterWizardPage = new DatabaseTableFilterWizardPage(tableInfoParameters, this.connectionItem,
                    metadataConnection);

            tableFilterWizardPage.setDescription(Messages.getString("DatabaseTableWizard.description")); //$NON-NLS-1$
            tableFilterWizardPage.setPageComplete(true);
            selectorWizardPage
                    .setTitle(Messages.getString("TableWizardPage.titleCreate") + " \"" + connectionItem.getProperty().getLabel() //$NON-NLS-1$ //$NON-NLS-2$
                            + "\""); //$NON-NLS-1$
            selectorWizardPage.setDescription(Messages.getString("TableWizardPage.descriptionCreate")); //$NON-NLS-1$
            selectorWizardPage.setPageComplete(true);

            tableWizardpage
                    .setTitle(Messages.getString("TableWizardPage.titleCreate") + " \"" + connectionItem.getProperty().getLabel() //$NON-NLS-1$ //$NON-NLS-2$
                            + "\""); //$NON-NLS-1$
            tableWizardpage.setDescription(Messages.getString("TableWizardPage.descriptionCreate")); //$NON-NLS-1$
            tableWizardpage.setPageComplete(false);

            addPage(tableFilterWizardPage);
            addPage(selectorWizardPage);
            addPage(tableWizardpage);

        } else {
            tableWizardpage
                    .setTitle(Messages.getString("TableWizardPage.titleUpdate") + " \"" + connectionItem.getProperty().getLabel() //$NON-NLS-1$ //$NON-NLS-2$
                            + "\""); //$NON-NLS-1$
            tableWizardpage.setDescription(Messages.getString("TableWizardPage.descriptionUpdate")); //$NON-NLS-1$
            tableWizardpage.setPageComplete(false);
            addPage(tableWizardpage);
        }

    }

    /**
     * This method determine if the 'Finish' button is enable This method is called when 'Finish' button is pressed in
     * the wizard. We will create an operation and run it using wizard as execution context.
     */
    @Override
    public boolean performFinish() {
        if (tableWizardpage.isPageComplete()) {
            IWorkspace workspace = ResourcesPlugin.getWorkspace();

            IWorkspaceRunnable operation = new IWorkspaceRunnable() {

                @Override
                public void run(IProgressMonitor monitor) throws CoreException {
                    // temConnection will be set to model when finish
                    DatabaseConnection connection = (DatabaseConnection) connectionItem.getConnection();

                    /*
                     * The first save,to make sure all the columns and tables in connection can has a eResource,or it
                     * can't set uuids
                     */
                    saveMetaData();
                    tableHelper.resetUUID(connection);

                    ITDQRepositoryService tdqRepositoryService = null;
                    boolean needUpdateAnalysis = false;

                    if (PluginChecker.isTDQLoaded()) {
                        // MOD qiongli 2011-11-23,TDQ-3930,pop a question dilog when need to update DQ analyses,if user
                        // click
                        // cancel,will return and stop this retrieve action.

                        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQRepositoryService.class)) {
                            tdqRepositoryService = (ITDQRepositoryService) org.talend.core.GlobalServiceRegister.getDefault()
                                    .getService(ITDQRepositoryService.class);
                            needUpdateAnalysis = isNeedUpdateDQ(connection, oldCopiedConnection, tdqRepositoryService);
                        }

                        if (tdqRepositoryService != null && needUpdateAnalysis) {
                            if (!tdqRepositoryService.confirmUpdateAnalysis(connectionItem)) {
                                return;
                            }
                        }
                    }

                    saveMetaData();

                    // update related analysis for TDQ after saving connection.
                    if (tdqRepositoryService != null && needUpdateAnalysis) {
                        tdqRepositoryService.updateImpactOnAnalysis(connectionItem);
                    }
                    Display.getDefault().asyncExec(new Runnable() {
                        
                        @Override
                        public void run() {
                            RepositoryUpdateManager.updateMultiSchema(connectionItem, oldMetadataTable, oldTableMap);
                            closeLockStrategy();
                        }
                    });
                    List<IRepositoryViewObject> list = new ArrayList<IRepositoryViewObject>();
                    list.add(repositoryObject);
                    CoreRuntimePlugin.getInstance().getRepositoryService().notifySQLBuilder(list);

                    oldCopiedConnection = null;
                    tableHelper.clean();
                    if (ExtractMetaDataUtils.getInstance().getConn() != null) {
                        ExtractMetaDataUtils.getInstance().closeConnection();
                    }
                }
            };
            try {
                workspace.run(operation, null);
            } catch (CoreException e) {
                ExceptionHandler.process(e);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * We will accept the selection in the workbench to see if we can initialize from it.
     * 
     * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
     */
    @Override
    public void init(final IWorkbench workbench, final IStructuredSelection selection2) {
        this.selection = selection2;
    }

    /**
     * execute saveMetaData() on TableForm.
     */
    private void saveMetaData() {
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        try {
            factory.save(connectionItem);
        } catch (PersistenceException e) {
            String detailError = e.toString();
            new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("CommonWizard.persistenceException"), detailError); //$NON-NLS-1$
            log.error(Messages.getString("CommonWizard.persistenceException") + "\n" + detailError); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.RepositoryWizard#performCancel()
     */
    @Override
    public boolean performCancel() {
        if (selectorWizardPage != null) {
            selectorWizardPage.performCancel();
        }
        oldCopiedConnection = null;
        tableHelper.clean();
        return super.performCancel();
    }

    /**
     * clone a new DB connection
     */
    private void cloneBaseDataBaseConnection(DatabaseConnection connection) {
        oldCopiedConnection = EcoreUtil.copy(connection);
        EList<Package> dataPackage = connection.getDataPackage();
        Collection<Package> newDataPackage = EcoreUtil.copyAll(dataPackage);
        ConnectionHelper.addPackages(newDataPackage, oldCopiedConnection);
    }

    /**
     * 
     * DOC qiongli: judge if need to update related Analyses for TDQ.It is according to method
     * RepositoryUpdateManager.updateMultiSchema(ConnectionItem connItem, List<IMetadataTable> oldMetadataTable,
     * Map<String, String> oldTableMap).
     * 
     * @param item
     * @return
     */
    private boolean isNeedUpdateDQ(Connection newconn, Connection oldConn, ITDQRepositoryService tdqRepositoryService) {

        if (!PluginChecker.isTDQLoaded() || newconn == null || oldConn == null || tdqRepositoryService == null) {
            return false;
        }
        if (!tdqRepositoryService.hasClientDependences(connectionItem)) {
            return false;
        }

        Map<String, String> schemaRenamedMap = RepositoryUpdateManager.getSchemaRenamedMap(newconn, connectionItem.getProperty(),
                oldTableMap);
        boolean isNeed = !schemaRenamedMap.isEmpty();
        if (!isNeed) {
            if (oldMetadataTable != null) {
                List<IMetadataTable> newMetadataTable = RepositoryUpdateManager.getConversionMetadataTables(newconn);
                // change the manually changed label to the old label before compare it
                Map<String, Map<String, String>> labelChanged = tableWizardpage.getLabelChanged();
                if (labelChanged != null) {
                    for (IMetadataTable newTable : newMetadataTable) {
                        Map<String, String> map = labelChanged.get(newTable.getLabel());
                        if (map != null && !map.isEmpty()) {
                            for (IMetadataColumn newColumn : newTable.getListColumns()) {
                                String oldColumnLabel = map.get(newColumn.getLabel());
                                if (oldColumnLabel != null) {
                                    newColumn.setLabel(oldColumnLabel);
                                }
                            }
                        }
                    }
                }
                isNeed = !RepositoryUpdateManager.sameAsMetadatTable(newMetadataTable, oldMetadataTable, oldTableMap,
                        IMetadataColumn.OPTIONS_IGNORE_DBCOLUMNNAME);
            }
        }
        if (!isNeed) {
            List<IMetadataTable> newMetadataTable = RepositoryUpdateManager.getConversionMetadataTables(newconn);
            Map<String, EUpdateResult> deletedOrReselectTablesMap = new HashMap<String, EUpdateResult>();
            isNeed = RepositoryUpdateManager.isDeleteOrReselectMap(connectionItem, newMetadataTable, oldMetadataTable,
                    deletedOrReselectTablesMap);
        }
        return isNeed;
    }

    @Override
    /**
     * TUP-2073 if the related connection editor is opened in DQ side,should not unlock.
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
}
