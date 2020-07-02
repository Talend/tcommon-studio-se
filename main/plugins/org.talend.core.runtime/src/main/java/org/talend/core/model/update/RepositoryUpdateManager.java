// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.update;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ICoreService;
import org.talend.core.IRepositoryContextUpdateService;
import org.talend.core.ITDQPatternService;
import org.talend.core.ITDQRepositoryService;
import org.talend.core.hadoop.BigDataBasicUtil;
import org.talend.core.hadoop.HadoopConstants;
import org.talend.core.hadoop.IHadoopClusterService;
import org.talend.core.hadoop.repository.HadoopRepositoryUtil;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.context.JobContext;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.context.JobContextParameter;
import org.talend.core.model.context.link.ContextLink;
import org.talend.core.model.context.link.ContextLinkService;
import org.talend.core.model.context.link.ContextParamLink;
import org.talend.core.model.context.link.ItemContextLink;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataSchemaType;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.GenericSchemaConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.QueriesConnection;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.metadata.builder.connection.SAPConnection;
import org.talend.core.model.metadata.builder.connection.SAPFunctionUnit;
import org.talend.core.model.metadata.builder.connection.SAPIDocUnit;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.GenericSchemaConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.update.extension.UpdateManagerProviderDetector;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.model.utils.UpdateRepositoryHelper;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.i18n.Messages;
import org.talend.core.runtime.services.IGenericDBService;
import org.talend.core.service.IMRProcessService;
import org.talend.core.service.IMetadataManagmentService;
import org.talend.core.service.IStormProcessService;
import org.talend.core.ui.ISparkJobletProviderService;
import org.talend.core.ui.ISparkStreamingJobletProviderService;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SAPBWTableHelper;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.runprocess.ItemCacheManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.RepositoryNode;

/**
 * ggu class global comment. Detailled comment
 */
public abstract class RepositoryUpdateManager {

    private static final Logger LOGGER = Logger.getLogger(RepositoryUpdateManager.class);

    private static final IProxyRepositoryFactory FACTORY = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();

    private static List<IRepositoryContextUpdateService> CONTEXT_UPDATE_SERVICE_LIST = null;

    /**
     * for repository context rename.
     */
    private Map<ContextItem, Map<String, String>> repositoryRenamedMap = new HashMap<ContextItem, Map<String, String>>();

    private Map<String, String> schemaRenamedMap = new HashMap<String, String>();

    private Map<String, String> columnRenamedMap = new HashMap<String, String>();

    /**
     * for context group
     */
    private Map<ContextItem, List<IContext>> repositoryContextGroupMap = new HashMap<ContextItem, List<IContext>>();

    private Map<ContextItem, List<IContext>> removeRepositoryContextGroupMap = new HashMap<ContextItem, List<IContext>>();

    private Map<ContextItem, List<IContext>> renameRepositoryContextGroupMap = new HashMap<ContextItem, List<IContext>>();

    private Map<IContext, String> renameContextGroup = new HashMap<IContext, String>();

    /* for table deleted and reselect on database wizard table */
    private Map<String, EUpdateResult> deletedOrReselectTablesMap = new HashMap<String, EUpdateResult>();

    /**
     * used for filter result.
     */
    protected Object parameter;

    private Map<Item, Set<String>> newParametersMap = new HashMap<Item, Set<String>>();

    private boolean onlyOpeningJob = false;

    private List<Relation> relations;

    private static IRepositoryService repistoryService = null;

    private static ICoreService coreService = null;

    private boolean isDetectAndUpdate = false;

    private boolean checkAddContextGroup;

    private boolean isConfigContextGroup;

    protected static boolean isAddColumn = false;

    static {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRepositoryService.class)) {
            repistoryService = (IRepositoryService) GlobalServiceRegister.getDefault().getService(IRepositoryService.class);
        }
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ICoreService.class)) {
            coreService = (ICoreService) GlobalServiceRegister.getDefault().getService(ICoreService.class);
        }
    }

    public RepositoryUpdateManager(Object parameter) {
        this(parameter, false);
    }

    public RepositoryUpdateManager(Object parameter, boolean isDetectAndUpdate) {
        this.parameter = parameter;
        this.isDetectAndUpdate = isDetectAndUpdate;
    }

    public RepositoryUpdateManager(Object parameter, List<Relation> relations) {
        this.parameter = parameter;
        this.relations = relations;
    }

    public void setOnlyOpeningJob(boolean onlyOpeningJob) {
        this.onlyOpeningJob = onlyOpeningJob;
    }

    /*
     * context
     */
    public Map<ContextItem, Map<String, String>> getContextRenamedMap() {
        return this.repositoryRenamedMap;
    }

    public void setContextRenamedMap(Map<ContextItem, Map<String, String>> repositoryRenamedMap) {
        this.repositoryRenamedMap = repositoryRenamedMap;
    }

    public Map<ContextItem, List<IContext>> getRepositoryAddGroupContext() {
        return this.repositoryContextGroupMap;
    }

    public void setRepositoryAddGroupContext(Map<ContextItem, List<IContext>> repositoryContextGroupMap) {
        this.repositoryContextGroupMap = repositoryContextGroupMap;
    }

    public Map<ContextItem, List<IContext>> getRepositoryRemoveGroupContext() {
        return this.removeRepositoryContextGroupMap;
    }

    public void setRepositoryRemoveGroupContext(Map<ContextItem, List<IContext>> removeRepositoryContextGroupMap) {
        this.removeRepositoryContextGroupMap = removeRepositoryContextGroupMap;
    }

    public Map<ContextItem, List<IContext>> getRepositoryRenameGroupContext() {
        return this.renameRepositoryContextGroupMap;
    }

    public void setRepositoryRenameGroupContext(Map<ContextItem, List<IContext>> renameRepositoryContextGroupMap) {
        this.renameRepositoryContextGroupMap = renameRepositoryContextGroupMap;
    }

    public Map<IContext, String> getRenameContextGroup() {
        return renameContextGroup;
    }

    public void setRenameContextGroup(Map<IContext, String> renameContextGroup) {
        this.renameContextGroup = renameContextGroup;
    }

    /*
     * Schema old name to new one
     */

    public Map<String, String> getSchemaRenamedMap() {
        return this.schemaRenamedMap;
    }

    public void setSchemaRenamedMap(Map<String, String> schemaRenamedMap) {
        this.schemaRenamedMap = schemaRenamedMap;
    }

    public Map<String, String> getColumnRenamedMap() {
        return this.columnRenamedMap;
    }

    public void setColumnRenamedMap(Map<String, String> columnRenamedMap) {
        this.columnRenamedMap = columnRenamedMap;
    }

    public abstract Set<? extends IUpdateItemType> getTypes();

    public static boolean openPropagationDialog() {
        return MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
                Messages.getString("RepositoryUpdateManager.Title"), //$NON-NLS-1$
                Messages.getString("RepositoryUpdateManager.Messages")); //$NON-NLS-1$
    }

    /**
     *
     * ggu Comment method "openNoModificationDialog".
     *
     * @param onlyImpactAnalysis for 9543
     * @return
     */
    public static void openNoModificationDialog() {
        String title = Messages.getString("RepositoryUpdateManager.NoModificationTitle"); //$NON-NLS-1$
        String messages = Messages.getString("RepositoryUpdateManager.NoModificationMessages"); ////$NON-NLS-1$
        MessageDialog.openInformation(Display.getCurrent().getActiveShell(), title, messages);
    }

    private boolean openRenameCheckedDialog() {
        return MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
                Messages.getString("RepositoryUpdateManager.RenameContextTitle"), //$NON-NLS-1$
                Messages.getString("RepositoryUpdateManager.RenameContextMessagesNoBuiltIn")); //$NON-NLS-1$

    }

    public boolean doWork() {
        return doWork(true, false);
    }

    public boolean needForcePropagation() {
        return getSchemaRenamedMap() != null && !getSchemaRenamedMap().isEmpty();
    }

    private boolean needForcePropagationForContext() {
        return getContextRenamedMap() != null && !getContextRenamedMap().isEmpty();
    }

    public boolean doWork(boolean show, final boolean onlyImpactAnalysis) {
        /*
         * NOTE: Most of functions are similar with AbstractRepositoryUpdateManagerProvider.updateForRepository, so if
         * update this, maybe need check the updateForRepository too.
         */

        // check the dialog.
        boolean checked = true;
        boolean showed = false;
        if (show) {
            if (needForcePropagationForContext()) {
                checked = openRenameCheckedDialog(); // bug 4988
                showed = true;
            } else if (parameter != null && !needForcePropagation()) {
                // see feature 4786
                IDesignerCoreService designerCoreService = CoreRuntimePlugin.getInstance().getDesignerCoreService();
                boolean deactive = designerCoreService != null
                        ? Boolean.parseBoolean(
                                designerCoreService.getPreferenceStore(ITalendCorePrefConstants.DEACTIVE_REPOSITORY_UPDATE))
                        : true;
                if (deactive) {
                    return false;
                }

                checked = openPropagationDialog();
                showed = true;
            }
        } else {
            showed = true;
        }
        if (checked) {
            final List<UpdateResult> results = new ArrayList<UpdateResult>();
            boolean cancelable = !needForcePropagation();
            final IRunnableWithProgress runnable = new IRunnableWithProgress() {

                @SuppressWarnings("unchecked")
                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    List<UpdateResult> returnResult = checkJobItemsForUpdate(monitor, (Set<IUpdateItemType>) getTypes(),
                            onlyImpactAnalysis);
                    if (returnResult != null) {
                        results.addAll(returnResult);
                    }
                }
            };

            try {
                if (CommonsPlugin.isHeadless() || Display.getCurrent() == null) {
                    runnable.run(new NullProgressMonitor());
                } else {
                    // final ProgressMonitorJobsDialog dialog = new ProgressMonitorJobsDialog(null);
                    final ProgressMonitorDialog dialog = new ProgressMonitorDialog(null);
                    dialog.run(true, cancelable, runnable);
                }

                // PlatformUI.getWorkbench().getProgressService().run(true, true, runnable);
            } catch (InvocationTargetException e) {
                ExceptionHandler.process(e);
            } catch (InterruptedException e) {
                if (e.getMessage().equals(UpdatesConstants.MONITOR_IS_CANCELED)) {
                    return false;
                }
                ExceptionHandler.process(e);
            }
            List<UpdateResult> checkedResults = null;

            if (parameter == null) { // update all job
                checkedResults = filterSpecialCheckedResult(results);
            } else { // filter
                checkedResults = filterCheckedResult(results);
            }
            if (checkedResults != null && !checkedResults.isEmpty()) {
                boolean updateResult = false;
                if (showed || parameter == null || unShowDialog(checkedResults) || openPropagationDialog()) {
                    IDesignerCoreService designerCoreService = CoreRuntimePlugin.getInstance().getDesignerCoreService();
                    if (show) {
                        updateResult = designerCoreService.executeUpdatesManager(checkedResults, onlyImpactAnalysis);
                    } else {
                        updateResult = designerCoreService.executeUpdatesManagerBackgroud(checkedResults, onlyImpactAnalysis);
                    }
                }
                // Added TDQ-15353 propogate context changes on DQ side
                updateContextOnDQ(checked);
                return updateResult;
            }
            if (show) {
                openNoModificationDialog();
            }
        }

        // Added TDQ-15353 propogate context changes on DQ side
        updateContextOnDQ(checked);

        getColumnRenamedMap().clear();
        return false;
    }

    // TDQ-15353 propogate context changes on DQ side
    private void updateContextOnDQ(boolean checked) {
        ITDQRepositoryService tdqRepService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQRepositoryService.class)) {
            tdqRepService = (ITDQRepositoryService) GlobalServiceRegister.getDefault().getService(ITDQRepositoryService.class);
        }
        if (tdqRepService != null) {
            // udpate all ana/reports who used this context
            tdqRepService.updateAllContextInAnalysisAndReport(this, parameter, checked);
        }
    }

    private List<UpdateResult> filterSpecialCheckedResult(List<UpdateResult> results) {
        if (results == null) {
            return null;
        }
        List<IProcess2> openedProcessList = CoreRuntimePlugin.getInstance().getDesignerCoreService()
                .getOpenedProcess(getEditors());

        List<UpdateResult> checkedResults = new ArrayList<UpdateResult>();
        for (UpdateResult result : results) {
            if (result.getParameter() instanceof JobletProcessItem) {
                if (result.getJob() instanceof IProcess2) { // only opening job
                    if (openedProcessList.contains(result.getJob())) {
                        checkedResults.add(result);
                    }
                }
            } else {
                checkedResults.add(result); // ignore others
            }
        }
        return checkedResults;
    }

    private List<UpdateResult> filterCheckedResult(List<UpdateResult> results) {
        if (results == null) {
            return null;
        }
        List<UpdateResult> checkedResults = new ArrayList<UpdateResult>();
        for (UpdateResult result : results) {
            if (filterForType(result)) {
                checkedResults.add(result);
            } else {
                // for context
                if (result.getUpdateType() == EUpdateItemType.CONTEXT && result.getResultType() == EUpdateResult.BUIL_IN) {
                    checkedResults.add(result);
                }
                // for context group
                if (result.getUpdateType() == EUpdateItemType.CONTEXT_GROUP && result.getResultType() == EUpdateResult.ADD) {
                    Object job = result.getJob();
                    if (parameter instanceof ContextItem && job instanceof IProcess2) {
                        ContextItem contextItem = (ContextItem) parameter;
                        String sourceId = contextItem.getProperty().getId();
                        IProcess2 relatedJob = (IProcess2) job;
                        if (relatedJob != null) {
                            List<IContext> listContext = relatedJob.getContextManager().getListContext();
                            List<String> existSource = new ArrayList<String>();
                            for (IContext context : listContext) {
                                for (IContextParameter param : context.getContextParameterList()) {
                                    String source = param.getSource();
                                    if (source != null && !existSource.contains(source)) {
                                        existSource.add(source);
                                    }
                                }
                            }
                            if (existSource.contains(sourceId)) {
                                checkedResults.add(result);
                            }
                        }

                    }
                } else if (result.getUpdateType() == EUpdateItemType.CONTEXT && result.getResultType() == EUpdateResult.ADD) {
                    ConnectionItem contextModeConnectionItem = result.getContextModeConnectionItem();
                    // for context mode
                    if (contextModeConnectionItem != null && contextModeConnectionItem.getConnection() == this.parameter) {
                        checkedResults.add(result);
                    }
                }

            }

        }
        return checkedResults;
    }

    private boolean unShowDialog(List<UpdateResult> checkedResults) {
        if (checkedResults == null) {
            return false;
        }
        for (UpdateResult result : checkedResults) {
            if (result.getResultType() != EUpdateResult.UPDATE) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    protected boolean filterForType(UpdateResult result) {
        if (result == null || parameter == null) {
            return false;
        }
        Object object = result.getParameter();
        if (object == null) {
            return false;
        }
        if (object == parameter) {
            return true;
        }
        if (object instanceof ConnectionItem) {
            if (((ConnectionItem) object).getConnection() == parameter) {
                return true;
            }
        }
        if (isSameConnection(object, parameter)) {
            return true;
        }
        if (object instanceof List) {
            List list = ((List) object);
            if (!list.isEmpty()) {
                Object firstObj = list.get(0);
                if (parameter == firstObj) { // for context rename
                    return true;
                }

                // schema
                if (checkResultSchema(result, firstObj, parameter)) {
                    return true;
                }

            }

        }
        // schema
        if (checkResultSchema(result, object, parameter)) {
            return true;
        }
        // query for wizard
        if (parameter instanceof QueriesConnection && object instanceof Query) {
            for (Query query : ((QueriesConnection) parameter).getQuery()) {
                if (query.getId().equals(((Query) object).getId())) {
                    return true;
                }
            }
        }
        // for bug 17573
        if ((object instanceof Query) && (parameter instanceof Query)) {
            if (((Query) object).getId().equals(((Query) parameter).getId())) {
                return true;
            }
        }

        if (checkHadoopRelevances(object)) {
            return true;
        }

        if (checkForPattern(object)) {
            return true;
        }

        return false;
    }

    // Added TDQ-11688 20170309 yyin
    private boolean checkForPattern(Object object) {
        ITDQPatternService service = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQPatternService.class)) {
            service = (ITDQPatternService) GlobalServiceRegister.getDefault().getService(ITDQPatternService.class);
        }
        if (service != null) {
            return service.isPattern(parameter);
        }

        return false;
    }

    private boolean isSameConnection(Object obj1, Object obj2) {
        Connection conn1 = getConnection(obj1);
        Connection conn2 = getConnection(obj2);
        if (conn1 != null && conn2 != null && conn1.equals(conn2)) {
            return true;
        }

        return false;
    }

    private Connection getConnection(Object obj) {
        Connection conn = null;
        if (obj instanceof Connection) {
            conn = (Connection) obj;
        } else if (obj instanceof ConnectionItem) {
            conn = ((ConnectionItem) obj).getConnection();
        }

        return conn;
    }

    private boolean checkHadoopRelevances(Object resultParam) {
        if (resultParam != null) {
            Connection parentConnection = getConnection(parameter);
            Connection childConnection = getConnection(resultParam);
            IHadoopClusterService hadoopClusterService = HadoopRepositoryUtil.getHadoopClusterService();
            if (hadoopClusterService != null) {
                return hadoopClusterService.containedByCluster(parentConnection, childConnection);
            }
        }

        return false;
    }

    private boolean checkResultSchema(UpdateResult result, Object object, Object parameter) {
        if (object == null || parameter == null) {
            return false;
        }
        // schema
        if (object instanceof IMetadataTable) { //
            if (parameter instanceof ConnectionItem) { //
                ConnectionItem connection = (ConnectionItem) parameter;
                String source = UpdateRepositoryHelper.getRepositorySourceName(connection);
                if (result.getRemark() != null) {
                    if (result.getRemark().startsWith(source)) {
                        return true;
                    } else if (result.isReadOnlyProcess()) {
                        return true;
                    } else {
                        // for bug 10365
                        String[] split = result.getRemark().split(UpdatesConstants.SEGMENT_LINE);
                        if (connection.getProperty() != null && split[0].equals(connection.getProperty().getId())) {
                            return true;
                        }
                    }
                }
            } else if (parameter instanceof org.talend.core.model.metadata.builder.connection.MetadataTable) {
                IMetadataTable table1 = ((IMetadataTable) object);
                MetadataTable table2 = (org.talend.core.model.metadata.builder.connection.MetadataTable) parameter;
                if (table1.getId() == null || table2.getId() == null) {
                    return table1.getLabel().equals(table2.getLabel());
                } else {
                    return table1.getId().equals(table2.getId());
                }
            } else if (parameter instanceof SAPFunctionUnit) {
                // check sap function and schema
                IMetadataTable table1 = ((IMetadataTable) object);
                List<MetadataTable> tables = null;
                if (MetadataSchemaType.INPUT.name().equals(table1.getTableType())) {
                    tables = ((SAPFunctionUnit) parameter).getInputTables();
                } else {
                    tables = ((SAPFunctionUnit) parameter).getTables();
                }
                for (MetadataTable table : tables) {
                    boolean equals = table1.getId().equals(table.getId());
                    if (equals) {
                        return true;
                    }
                }
            } else if (parameter instanceof Connection) {
                Set<MetadataTable> tables = ConnectionHelper.getTables((Connection) parameter);
                if (tables.size() == 1) {
                    IMetadataTable table1 = ((IMetadataTable) object);
                    MetadataTable table2 = tables.toArray(new MetadataTable[0])[0];
                    return table1.getId().equals(table2.getId());
                }
                if (parameter instanceof XmlFileConnection) {
                    boolean isResult = false;
                    for (MetadataTable table : tables) {
                        if (table.getId() != null && table.getId().equals(((IMetadataTable) object).getId())) {
                            isResult = true;
                            break;
                        }
                    }
                    return isResult;
                }
            }

        }
        /* table delete or reload */
        Object parameter2 = result.getParameter();
        if (object instanceof String && parameter2 instanceof List) {
            List listParameter = (List) parameter2;
            if (listParameter.get(1) instanceof EUpdateResult) {
                return true;
            }
        }
        return false;
    }

    private void updateConnection(ContextItem citem) throws PersistenceException {
        Map<ContextItem, Map<String, String>> renameMap = getContextRenamedMap();
        if (renameMap == null) {
            return;
        }
        Map<String, String> valueMap = renameMap.get(citem);
        if (valueMap == null) {
            return;
        }

        List<IRepositoryViewObject> dbConnList = FACTORY.getAll(ERepositoryObjectType.METADATA_CONNECTIONS, true);
        for (IRepositoryViewObject obj : dbConnList) {
            Item item = obj.getProperty().getItem();
            if (item instanceof ConnectionItem) {
                updateConnectionContextParam((ConnectionItem) item, citem, valueMap);
            }
        }
        List<IRepositoryViewObject> excelConnList = FACTORY.getAll(ERepositoryObjectType.METADATA_FILE_EXCEL, true);
        for (IRepositoryViewObject obj : excelConnList) {
            Item item = obj.getProperty().getItem();
            if (item instanceof ConnectionItem) {
                updateConnectionContextParam((ConnectionItem) item, citem, valueMap);
            }
        }

        List<IRepositoryViewObject> deliConnList = FACTORY.getAll(ERepositoryObjectType.METADATA_FILE_DELIMITED, true);
        for (IRepositoryViewObject obj : deliConnList) {
            Item item = obj.getProperty().getItem();
            if (item instanceof ConnectionItem) {
                updateConnectionContextParam((ConnectionItem) item, citem, valueMap);
            }
        }
        List<IRepositoryViewObject> regConnList = FACTORY.getAll(ERepositoryObjectType.METADATA_FILE_REGEXP, true);
        for (IRepositoryViewObject obj : regConnList) {
            Item item = obj.getProperty().getItem();
            if (item instanceof ConnectionItem) {
                updateConnectionContextParam((ConnectionItem) item, citem, valueMap);
            }
        }
        List<IRepositoryViewObject> ldifConnList = FACTORY.getAll(ERepositoryObjectType.METADATA_FILE_LDIF, true);
        for (IRepositoryViewObject obj : ldifConnList) {
            Item item = obj.getProperty().getItem();
            if (item instanceof ConnectionItem) {
                updateConnectionContextParam((ConnectionItem) item, citem, valueMap);
            }
        }
        List<IRepositoryViewObject> posiConnList = FACTORY.getAll(ERepositoryObjectType.METADATA_FILE_POSITIONAL, true);
        for (IRepositoryViewObject obj : posiConnList) {
            Item item = obj.getProperty().getItem();
            if (item instanceof ConnectionItem) {
                updateConnectionContextParam((ConnectionItem) item, citem, valueMap);
            }
        }
        List<IRepositoryViewObject> xmlConnList = FACTORY.getAll(ERepositoryObjectType.METADATA_FILE_XML, true);
        for (IRepositoryViewObject obj : xmlConnList) {
            Item item = obj.getProperty().getItem();
            if (item instanceof ConnectionItem) {
                updateConnectionContextParam((ConnectionItem) item, citem, valueMap);
            }
        }
        List<IRepositoryViewObject> saleConnList = FACTORY.getAll(ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA, true);
        for (IRepositoryViewObject obj : saleConnList) {
            Item item = obj.getProperty().getItem();
            if (item instanceof ConnectionItem) {
                updateConnectionContextParam((ConnectionItem) item, citem, valueMap);
            }
        }
        List<IRepositoryViewObject> wsdlConnList = FACTORY.getAll(ERepositoryObjectType.METADATA_WSDL_SCHEMA, true);
        for (IRepositoryViewObject obj : wsdlConnList) {
            Item item = obj.getProperty().getItem();
            if (item instanceof ConnectionItem) {
                updateConnectionContextParam((ConnectionItem) item, citem, valueMap);
            }
        }

        ERepositoryObjectType jsonType = ERepositoryObjectType.valueOf("json");
        if (jsonType != null) {
            List<IRepositoryViewObject> jsonConnList = FACTORY.getAll(jsonType);
            for (IRepositoryViewObject obj : jsonConnList) {
                Item item = obj.getProperty().getItem();
                if (item instanceof ConnectionItem) {
                    updateConnectionContextParam((ConnectionItem) item, citem, valueMap);
                }
            }
        }

        List<IRepositoryViewObject> sapConnList = FACTORY.getAll(ERepositoryObjectType.METADATA_SAPCONNECTIONS, true);
        for (IRepositoryViewObject obj : sapConnList) {
            Item item = obj.getProperty().getItem();
            if (item instanceof ConnectionItem) {
                updateConnectionContextParam((ConnectionItem) item, citem, valueMap);
            }
        }
        for (String updateType : UpdateRepositoryHelper.getAllHadoopConnectionTypes()) {
            List<IRepositoryViewObject> hadoopConnList = FACTORY
                    .getAll(ERepositoryObjectType.valueOf(ERepositoryObjectType.class, updateType), true);
            for (IRepositoryViewObject obj : hadoopConnList) {
                Item item = obj.getProperty().getItem();
                if (item instanceof ConnectionItem) {
                    updateConnectionContextParam((ConnectionItem) item, citem, valueMap);
                }
            }
        }

        if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericDBService.class)) {
            IGenericDBService service = GlobalServiceRegister.getDefault().getService(IGenericDBService.class);
            for (ERepositoryObjectType objectType : service.getAllGenericMetadataDBRepositoryType()) {
                List<IRepositoryViewObject> repositoryObjects = FACTORY.getAll(objectType);
                for (IRepositoryViewObject object : repositoryObjects) {
                    Item item = object.getProperty().getItem();
                    if (item instanceof ConnectionItem) {
                        updateConnectionContextParam((ConnectionItem) item, citem, valueMap);
                    }
                }
            }
        }
    }

	private static void updateConnectionContextParam(ConnectionItem conntectionItem, ContextItem citem,
			Map<String, String> newToOldValueMap) throws PersistenceException {
		boolean isModified = false;
		if (conntectionItem != null && conntectionItem.getConnection() != null && citem != null
				&& citem.getProperty() != null
				&& StringUtils.equals(conntectionItem.getConnection().getContextId(), citem.getProperty().getId())) {
			for (String newValue : newToOldValueMap.keySet()) {
				String oldValue = newToOldValueMap.get(newValue);
				boolean result = updateConnectionContextParam(conntectionItem, oldValue, newValue);
				isModified = isModified || result;
			}
			if (isModified) {
				FACTORY.save(conntectionItem, false);
			}
		}
	}

    private static boolean updateConnectionContextParam(ConnectionItem conntectionItem, String oldValue, String newValue) {
        Connection conn = conntectionItem.getConnection();
        if (conn.isContextMode()) {
            IRepositoryContextUpdateService updater = null;
            updater = findContextParameterUpdater(conn);
            if (updater != null) {
                return updater.updateContextParameter(conn, addContextParamPrefix(oldValue), addContextParamPrefix(newValue));
            }
        }
        return false;
    }

    public static void updateConnectionContextParam(RepositoryNode connNode) throws PersistenceException {
        if (connNode.getObject() == null || connNode.getObject().getProperty() == null
                || connNode.getObject().getProperty().getItem() == null) {
            return;
        }
        ConnectionItem conntectionItem = (ConnectionItem) connNode.getObject().getProperty().getItem();
        updateConnectionContextParam(conntectionItem);
    }

    public static void updateConnectionContextParam(ConnectionItem conntectionItem)
            throws PersistenceException {
        Connection conn = conntectionItem.getConnection();
        if (conn.isContextMode()) {
            ContextItem contextItem = ContextUtils.getContextItemById2(conn.getContextId());
            ItemContextLink itemContextLink = null;
            try {
                itemContextLink = ContextLinkService.getInstance().loadContextLinkFromJson(conntectionItem);
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
            ContextLink contextLink = null;
            if (itemContextLink != null) {
                contextLink = itemContextLink.findContextLink(null, conn.getContextName());
            }
            if (contextLink != null) {
                ContextType contextType = ContextUtils.getContextTypeByName(contextItem, conn.getContextName(), false);
                IRepositoryContextUpdateService updateServce = findContextParameterUpdater(conn);
                if (updateServce != null) {
                    boolean isModified = false;
                    for (ContextParamLink paramLink : contextLink.getParameterList()) {
                        ContextParameterType paramType = ContextUtils.getContextParameterTypeByName(contextType,
                                paramLink.getName());
                        if (paramType == null) {
                            paramType = ContextUtils.getContextParameterTypeById(contextType, paramLink.getId(), true);
                            if (paramType != null) {
                                boolean result = updateServce.updateContextParameter(conn,
                                        addContextParamPrefix(paramLink.getName()), addContextParamPrefix(paramType.getName()));
                                isModified = isModified || result;
                            }
                        }
                    }
                    if (isModified) {
                        FACTORY.save(conntectionItem, false);
                    }
                }
            }
        }
    }

    private static String addContextParamPrefix(String value) {
        if (!value.startsWith(ContextParameterUtils.JAVA_NEW_CONTEXT_PREFIX)) {
            value = ContextParameterUtils.JAVA_NEW_CONTEXT_PREFIX + value;
        }
        return value;
    }

    public static IEditorReference[] getEditors() {
        if (CommonsPlugin.isHeadless() || !FACTORY.isFullLogonFinished()) {
            return new IEditorReference[0];
        }
        final List<IEditorReference> list = new ArrayList<IEditorReference>();
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                IEditorReference[] reference = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                        .getEditorReferences();
                list.addAll(Arrays.asList(reference));
            }
        });
        return list.toArray(new IEditorReference[0]);
    }

    /**
     *
     * ggu Comment method "checkJobItemsForUpdate".
     *
     * @param types - need update types of jobs.
     * @param sourceIdMap - map old source id to new one.
     * @param sourceItem - modified repository item.
     * @return
     */
    private List<UpdateResult> checkJobItemsForUpdate(IProgressMonitor parentMonitor, final Set<IUpdateItemType> types,
            final boolean onlySimpleShow) throws InterruptedException {
        if (types == null || types.isEmpty()) {
            return null;
        }
        RepositoryUpdateManagerHelper helper = new RepositoryUpdateManagerHelper(this) {

            @Override
            protected boolean enableCheckItem() {
                return parameter != null && relations != null && !onlyOpeningJob;
            }

            @Override
            protected List<Relation> getRelations() {
                return relations;
            }

            @Override
            protected List<UpdateResult> getOtherUpdateResults(IProgressMonitor parentMonitor, List<IProcess2> openedProcessList,
                    Set<IUpdateItemType> types) {
                List<UpdateResult> resultList = new ArrayList<UpdateResult>();
                // from dectect toolbar to check all process
                if (isDetectAndUpdate) {
                    resultList = updateAllProcess(parentMonitor, resultList, openedProcessList, types, false);
                }

                if (!onlyOpeningJob) {
                    // Ok, you also need to update the job setting in "create job with template"
                    List<UpdateResult> templateSetUpdate = checkSettingInJobTemplateWizard();
                    if (templateSetUpdate != null) {
                        resultList.addAll(templateSetUpdate);
                    }
                }
                return resultList;
            }

            @Override
            protected void checkAndSetParameters(IProcess2 process2) {
                // context rename and context group
                IContextManager contextManager = process2.getContextManager();
                if (contextManager instanceof JobContextManager) {
                    JobContextManager jobContextManager = (JobContextManager) contextManager;
                    jobContextManager.setRepositoryRenamedMap(getContextRenamedMap());
                    jobContextManager.setNewParametersMap(getNewParametersMap());
                    Map<ContextItem, List<IContext>> repositoryAddGroupContext = getRepositoryAddGroupContext();
                    jobContextManager.setConfigContextGroup(isConfigContextGroup);

                    if (checkAddContextGroup && repositoryAddGroupContext.isEmpty() && parameter instanceof ContextItem) {
                        List<IContext> addContextGroupList = new ArrayList<IContext>();
                        List<IContext> jobContexts = process2.getContextManager().getListContext();
                        List<ContextType> repositoryContexts = ((ContextItem) parameter).getContext();
                        String repositoryId = ((ContextItem) parameter).getProperty().getId();
                        for (ContextType repoContext : repositoryContexts) {
                            boolean found = false;
                            for (IContext jobContext : jobContexts) {
                                if (jobContext.getName().equals(repoContext.getName())) {
                                    found = true;
                                }
                            }
                            if (!found) {
                                IContext jobContext = new JobContext(repoContext.getName());
                                List<ContextParameterType> repoParams = repoContext.getContextParameter();
                                for (ContextParameterType repoParam : repoParams) {
                                    IContextParameter jobParam = new JobContextParameter();
                                    jobParam.setName(repoParam.getName());
                                    jobParam.setContext(jobContext);
                                    jobParam.setComment(repoParam.getComment());
                                    jobParam.setPrompt(repoParam.getPrompt());
                                    jobParam.setSource(repositoryId);
                                    jobParam.setType(repoParam.getType());
                                    jobParam.setValue(repoParam.getValue());
                                    jobParam.setInternalId(repoParam.getInternalId());
                                    jobContext.getContextParameterList().add(jobParam);
                                }
                                addContextGroupList.add(jobContext);
                            }
                        }
                        repositoryAddGroupContext.put((ContextItem) parameter, addContextGroupList);
                    }

                    List<IContext> listIContext = new ArrayList<IContext>();
                    for (ContextItem item : repositoryAddGroupContext.keySet()) {
                        List<IContext> list = repositoryAddGroupContext.get(item);
                        ListIterator<IContext> listIterator = list.listIterator();
                        while (listIterator.hasNext()) {
                            IContext context = listIterator.next();
                            JobContext newJobContext = new JobContext(context.getName());
                            List<IContextParameter> existedParameters = new ArrayList<IContextParameter>();

                            for (int j = 0; j < context.getContextParameterList().size(); j++) {
                                IContextParameter param = context.getContextParameterList().get(j);
                                IContextParameter contextParameter = jobContextManager.getDefaultContext()
                                        .getContextParameter(param.getName());
                                if (contextParameter != null && param.getName().equals(contextParameter.getName())
                                        && item.getProperty().getId().equals(contextParameter.getSource())) { // found
                                    IContextParameter clone = param.clone();
                                    clone.setContext(newJobContext);
                                    existedParameters.add(clone);
                                }
                            }
                            if (!existedParameters.isEmpty()) {
                                newJobContext.setContextParameterList(existedParameters);
                                listIContext.add(newJobContext);
                            }
                        }

                    }
                    jobContextManager.setAddGroupContext(listIContext);
                    jobContextManager.setAddContextGroupMap(repositoryAddGroupContext);

                    Map<ContextItem, List<IContext>> repositoryRemoveGroupContext = getRepositoryRemoveGroupContext();

                    List<IContext> removeListIContext = new ArrayList<IContext>();
                    for (ContextItem item : repositoryRemoveGroupContext.keySet()) {
                        List<IContext> list = repositoryRemoveGroupContext.get(item);
                        ListIterator<IContext> listIterator = list.listIterator();
                        while (listIterator.hasNext()) {
                            IContext context = listIterator.next();

                            if (!removeListIContext.contains(context)) {
                                removeListIContext.add(context);
                            }

                        }

                    }
                    jobContextManager.setRemoveGroupContext(removeListIContext);
                    jobContextManager.setRemoveContextGroupMap(repositoryRemoveGroupContext);

                    Map<ContextItem, List<IContext>> repositoryRenameGroupContext = getRepositoryRenameGroupContext();

                    jobContextManager.setRenameGroupContext(getRenameContextGroup());
                    jobContextManager.setRenameContextGroupMap(repositoryRenameGroupContext);
                }
                // schema
                IUpdateManager updateManager = process2.getUpdateManager();
                if (updateManager instanceof AbstractUpdateManager) {
                    AbstractUpdateManager manager = (AbstractUpdateManager) updateManager;
                    if (getSchemaRenamedMap() != null && !getSchemaRenamedMap().isEmpty()) {
                        manager.setSchemaRenamedMap(getSchemaRenamedMap());
                    }
                    if (getColumnRenamedMap() != null && !getColumnRenamedMap().isEmpty()) {
                        manager.setColumnRenamedMap(getColumnRenamedMap());
                    }
                    if (getDeletedOrReselectTablesMap() != null && !getDeletedOrReselectTablesMap().isEmpty()) {
                        manager.setDeletedOrReselectTablesMap(getDeletedOrReselectTablesMap());
                    }
                    manager.setFromRepository(true);
                    if (isAddColumn) {
                        manager.setAddColumn(true);
                        isAddColumn = false;
                    }
                }

                // Added TDQ-13437: when the job contains tMultiPattern, and update the REPOSITORY_PROPERTY_TYPE value
                // to the current modified pattern id.
                for (INode node : process2.getGraphicalNodes()) {
                    if (node.getComponent().getName().startsWith("tMultiPattern") && parameter instanceof Item) {
                        String id = ((Item) parameter).getProperty().getId();
                        IElementParameter repoProperty = node.getElementParameter("REPOSITORY_PROPERTY_TYPE");
                        if (repoProperty != null) {
                            repoProperty.setValue(id);
                        }
                    }
                }
            }

        };
        return helper.checkJobItemsForUpdate(parentMonitor, types);
    }

    /**
     * YeXiaowei Comment method "checkSettingInJobTemplateWizard".
     */
    private List<UpdateResult> checkSettingInJobTemplateWizard() {
        List<IProcess> processes = CoreRuntimePlugin.getInstance().getDesignerCoreService().getProcessForJobTemplate();

        if (processes == null || processes.isEmpty()) {
            return null;
        }

        List<UpdateResult> result = new ArrayList<UpdateResult>();

        for (IProcess process : processes) {
            if (process instanceof IProcess2) {
                IProcess2 nowProcess = (IProcess2) process;
                nowProcess.getUpdateManager().checkAllModification();
                List<UpdateResult> results = nowProcess.getUpdateManager().getUpdatesNeeded();
                if (results != null) {
                    result.addAll(results);
                }
            }
        }

        return result;
    }

    public static ERepositoryObjectType getTypeFromSource(String source) {
        if (source == null) {
            return null;
        }
        for (ERepositoryObjectType type : (ERepositoryObjectType[]) ERepositoryObjectType.values()) {
            String alias = type.getAlias();
            if (alias != null && source.startsWith(alias)) {
                return type;
            }
        }
        return null;
    }

    public static String getUpdateJobInfor(Property property) {
        StringBuffer infor = new StringBuffer();
        String prefix = "";
        String label = null;
        String version = null;
        if (property.getItem() instanceof JobletProcessItem) { // for joblet
            boolean isSpark = false;
            boolean isSparkStreaming = false;
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ISparkJobletProviderService.class)) {
                ISparkJobletProviderService sparkJobletService = (ISparkJobletProviderService) GlobalServiceRegister.getDefault()
                        .getService(ISparkJobletProviderService.class);
                if (sparkJobletService != null && sparkJobletService.isSparkJobletItem(property.getItem())) {
                    isSpark = true;
                }
            }
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ISparkStreamingJobletProviderService.class)) {
                ISparkStreamingJobletProviderService sparkStreamingJobletService = (ISparkStreamingJobletProviderService) GlobalServiceRegister
                        .getDefault().getService(ISparkStreamingJobletProviderService.class);
                if (sparkStreamingJobletService != null
                        && sparkStreamingJobletService.isSparkStreamingJobletItem(property.getItem())) {
                    isSparkStreaming = true;
                }
            }
            if (isSpark) {
                prefix = UpdatesConstants.SPARK_JOBLET;
            } else if (isSparkStreaming) {
                prefix = UpdatesConstants.SPARK_STREAMING_JOBLET;
            } else {
                prefix = UpdatesConstants.JOBLET;
            }

        }
        Item item = property.getItem();
        if (item != null && prefix.isEmpty() && GlobalServiceRegister.getDefault().isServiceRegistered(IMRProcessService.class)) {
            IMRProcessService mrProcessService = (IMRProcessService) GlobalServiceRegister.getDefault()
                    .getService(IMRProcessService.class);
            if (mrProcessService.isMapReduceItem(item)) {
                Object framework = BigDataBasicUtil.getFramework(item);
                if (framework != null) {
                    if (HadoopConstants.FRAMEWORK_SPARK.equals(framework)) {
                        prefix = UpdatesConstants.SPARK;
                    }
                }
                if (prefix == null || prefix.isEmpty()) {
                    prefix = UpdatesConstants.MAPREDUCE;
                }
            }
        }
        if (item != null && prefix.isEmpty()
                && GlobalServiceRegister.getDefault().isServiceRegistered(IStormProcessService.class)) {
            IStormProcessService stormProcessService = (IStormProcessService) GlobalServiceRegister.getDefault()
                    .getService(IStormProcessService.class);
            if (stormProcessService.isStormItem(item)) {
                Object framework = BigDataBasicUtil.getFramework(item);
                if (framework != null) {
                    if (HadoopConstants.FRAMEWORK_SPARKSTREAMING.equals(framework)) {
                        prefix = UpdatesConstants.SPARKSTREAMING;
                    }
                }
                if (prefix == null || prefix.isEmpty()) {
                    prefix = UpdatesConstants.STORM;
                }
            }
        }
        if (prefix == null || prefix.isEmpty()) {
            prefix = UpdatesConstants.JOB;
        }
        label = property.getLabel();
        version = property.getVersion();
        infor.append(prefix);
        if (label != null) {
            infor.append(UpdatesConstants.SPACE);
            infor.append(label);
            infor.append(UpdatesConstants.SPACE);
            infor.append(version);
        }
        return infor.toString();

    }

    /**
     *
     * ggu Comment method "updateSchema".
     *
     * for repository wizard.
     */
    public static boolean updateDBConnection(ConnectionItem connection) {
        return updateDBConnection(connection, true, false);
    }

    /**
     *
     * hwang Comment method "updateServices".
     *
     * for repository wizard.
     */
    public static boolean updateServices(ConnectionItem connection) {
        return updateServices(connection, true, false);
    }

    public static boolean updateDBConnection(ConnectionItem connection, boolean show) {
        return updateDBConnection(connection, show, false);
    }

    public static boolean updateDBConnection(ConnectionItem connectionItem, boolean show, final boolean onlySimpleShow) {
        return updateDBConnection(connectionItem, RelationshipItemBuilder.LATEST_VERSION, show, onlySimpleShow);
    }

    /**
     *
     * ggu Comment method "updateQuery".
     *
     * if show is false, will work for context menu action.
     */
    public static boolean updateDBConnection(ConnectionItem connectionItem, String version, boolean show,
            final boolean onlySimpleShow) {
        List<Relation> relations = RelationshipItemBuilder.getInstance().getItemsRelatedTo(connectionItem.getProperty().getId(),
                version, RelationshipItemBuilder.PROPERTY_RELATION);

        RepositoryUpdateManager repositoryUpdateManager = new RepositoryUpdateManager(connectionItem, relations) {

            @Override
            public Set<EUpdateItemType> getTypes() {
                Set<EUpdateItemType> types = new HashSet<EUpdateItemType>();
                types.add(EUpdateItemType.NODE_PROPERTY);
                types.add(EUpdateItemType.JOB_PROPERTY_EXTRA);
                types.add(EUpdateItemType.JOB_PROPERTY_STATS_LOGS);
                types.add(EUpdateItemType.JOB_PROPERTY_HEADERFOOTER);
                types.add(EUpdateItemType.JOB_PROPERTY_MAPREDUCE);
                types.add(EUpdateItemType.JOB_PROPERTY_STORM);

                return types;
            }

        };
        return repositoryUpdateManager.doWork(show, false);
    }

    /**
     *
     * hwang Comment method "updateServices".
     *
     * if show is false, will work for context menu action.
     */
    public static boolean updateServices(ConnectionItem connectionItem, boolean show, final boolean onlySimpleShow) {
        List<IRepositoryViewObject> updateList = new ArrayList<IRepositoryViewObject>();
        List<Relation> relations = RelationshipItemBuilder.getInstance().getItemsRelatedTo(connectionItem.getProperty().getId(),
                RelationshipItemBuilder.LATEST_VERSION, RelationshipItemBuilder.SERVICES_RELATION);

        RepositoryUpdateManager repositoryUpdateManager = new RepositoryUpdateManager(connectionItem, relations) {

            @Override
            public Set<EUpdateItemType> getTypes() {
                Set<EUpdateItemType> types = new HashSet<EUpdateItemType>();
                types.add(EUpdateItemType.NODE_PROPERTY);
                types.add(EUpdateItemType.JOB_PROPERTY_EXTRA);
                types.add(EUpdateItemType.JOB_PROPERTY_STATS_LOGS);
                types.add(EUpdateItemType.JOB_PROPERTY_HEADERFOOTER);

                return types;
            }

        };
        return repositoryUpdateManager.doWork(true, false);
    }

    /**
     *
     * ggu Comment method "updateSchema".
     *
     * for repository wizard.
     */
    public static boolean updateFileConnection(ConnectionItem connection) {
        return updateFileConnection(connection, true, false);
    }

    /**
     * DOC PLV Comment method "updateFileConnection".
     *
     * @param connectionItem
     * @param oldMetadataTable
     */
    public static boolean updateFileConnection(ConnectionItem connection, List<IMetadataTable> oldMetadataTable) {
        if (oldMetadataTable != null) {
            List<IMetadataTable> newMetadataTable = RepositoryUpdateManager
                    .getConversionMetadataTables(connection.getConnection());
            isAddColumn = isAddColumn(newMetadataTable, oldMetadataTable);
        }
        return updateFileConnection(connection, true, false);
    }

    /**
     *
     * ggu Comment method "updateQuery".
     *
     * if show is false, will work for context menu action.
     */
    public static boolean updateFileConnection(ConnectionItem connectionItem, boolean show, boolean onlySimpleShow) {
        List<Relation> relations = RelationshipItemBuilder.getInstance().getItemsRelatedTo(connectionItem.getProperty().getId(),
                RelationshipItemBuilder.LATEST_VERSION, RelationshipItemBuilder.PROPERTY_RELATION);

        RepositoryUpdateManager repositoryUpdateManager = new RepositoryUpdateManager(connectionItem, relations) {

            @Override
            public Set<EUpdateItemType> getTypes() {
                Set<EUpdateItemType> types = new HashSet<EUpdateItemType>();
                types.add(EUpdateItemType.NODE_PROPERTY);
                types.add(EUpdateItemType.NODE_SCHEMA);
                types.add(EUpdateItemType.JOB_PROPERTY_HEADERFOOTER);
                types.add(EUpdateItemType.NODE_SAP_IDOC);
                return types;
            }

        };
        if (!ConnectionColumnUpdateManager.getInstance().getColumnRenameMap().isEmpty()) {
            repositoryUpdateManager.setColumnRenamedMap(ConnectionColumnUpdateManager.getInstance().getColumnRenameMap());
        }
        return repositoryUpdateManager.doWork(show, onlySimpleShow);
    }

    /**
     * DOC ycbai Comment method "updateValidationRuleConnection".
     *
     * @param connection
     * @return
     */
    public static boolean updateValidationRuleConnection(ConnectionItem connection) {
        return updateValidationRuleConnection(connection, true, false);
    }

    /**
     * DOC ycbai Comment method "updateValidationRuleConnection".
     *
     * @param connectionItem
     * @param show
     * @param onlySimpleShow
     * @return
     */
    public static boolean updateValidationRuleConnection(ConnectionItem connectionItem, boolean show, boolean onlySimpleShow) {
        List<IRepositoryViewObject> updateList = new ArrayList<IRepositoryViewObject>();
        List<Relation> relations = RelationshipItemBuilder.getInstance().getItemsRelatedTo(connectionItem.getProperty().getId(),
                RelationshipItemBuilder.LATEST_VERSION, RelationshipItemBuilder.VALIDATION_RULE_RELATION);

        RepositoryUpdateManager repositoryUpdateManager = new RepositoryUpdateManager(connectionItem, relations) {

            @Override
            public Set<EUpdateItemType> getTypes() {
                Set<EUpdateItemType> types = new HashSet<EUpdateItemType>();
                types.add(EUpdateItemType.NODE_VALIDATION_RULE);
                return types;
            }

        };
        return repositoryUpdateManager.doWork(show, onlySimpleShow);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> getTableIdAndNameMap(ConnectionItem connItem) {
        if (connItem == null) {
            return Collections.emptyMap();
        }
        Map<String, String> idAndNameMap = new HashMap<String, String>();
        Set<MetadataTable> tables = ConnectionHelper.getTables(connItem.getConnection());
        if (tables != null) {
            for (MetadataTable table : tables) {
                idAndNameMap.put(table.getId(), table.getLabel());
            }
        }
        return idAndNameMap;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> getTableIdAndNameMap(SAPFunctionUnit functionUnit) {
        if (functionUnit == null) {
            return Collections.emptyMap();
        }
        Map<String, String> idAndNameMap = new HashMap<String, String>();
        List tablesAll = new ArrayList();
        tablesAll.addAll(functionUnit.getTables());
        tablesAll.addAll(functionUnit.getInputTables());
        for (MetadataTable table : (List<MetadataTable>) tablesAll) {
            idAndNameMap.put(table.getId(), table.getLabel());
        }
        return idAndNameMap;
    }

    public static Map<String, String> getOldTableIdAndNameMap(ConnectionItem connItem, MetadataTable metadataTable,
            boolean creation) {
        Map<String, String> oldTableMap = getTableIdAndNameMap(connItem);
        if (creation && metadataTable != null) {
            oldTableMap.remove(metadataTable.getId());
        }
        return oldTableMap;
    }

    public static Map<String, String> getOldTableIdAndNameMap(SAPFunctionUnit functionUnit, MetadataTable metadataTable,
            boolean creation) {
        Map<String, String> oldTableMap = getTableIdAndNameMap(functionUnit);
        if (creation && metadataTable != null) {
            oldTableMap.remove(metadataTable.getId());
        }
        return oldTableMap;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> getSchemaRenamedMap(ConnectionItem connItem, Map<String, String> oldTableMap) {
        if (connItem == null || oldTableMap == null) {
            return Collections.emptyMap();
        }

        Map<String, String> schemaRenamedMap = new HashMap<String, String>();

        final String prefix = connItem.getProperty().getId() + UpdatesConstants.SEGMENT_LINE;
        Set<MetadataTable> tables = ConnectionHelper.getTables(connItem.getConnection());
        if (tables != null) {
            for (MetadataTable table : tables) {
                String oldName = oldTableMap.get(table.getId());
                String newName = table.getLabel();
                if (oldName != null && !oldName.equals(newName)) {
                    schemaRenamedMap.put(prefix + oldName, prefix + newName);
                }
            }
        }
        return schemaRenamedMap;
    }

    /**
     *
     * overload the method for TDQ-3930.
     *
     * @param connection
     * @param property
     * @param oldTableMap
     * @return
     */
    public static Map<String, String> getSchemaRenamedMap(Connection connection, Property property,
            Map<String, String> oldTableMap) {
        if (connection == null || oldTableMap == null) {
            return Collections.emptyMap();
        }

        Map<String, String> schemaRenamedMap = new HashMap<String, String>();

        final String prefix = property.getId() + UpdatesConstants.SEGMENT_LINE;
        Set<MetadataTable> tables = ConnectionHelper.getTables(connection);
        if (tables != null) {
            for (MetadataTable table : tables) {
                String oldName = oldTableMap.get(table.getId());
                String newName = table.getLabel();
                if (oldName != null && !oldName.equals(newName)) {
                    schemaRenamedMap.put(prefix + oldName, prefix + newName);
                }
            }
        }
        return schemaRenamedMap;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> getSchemaRenamedMap(SAPFunctionUnit functionUnit, ConnectionItem connItem,
            Map<String, String> oldTableMap) {
        if (functionUnit == null || oldTableMap == null) {
            return Collections.emptyMap();
        }

        Map<String, String> schemaRenamedMap = new HashMap<String, String>();

        final String prefix = connItem.getProperty().getId() + UpdatesConstants.SEGMENT_LINE;
        List tablesAll = new ArrayList();
        tablesAll.addAll(functionUnit.getTables());
        tablesAll.addAll(functionUnit.getInputTables());
        for (MetadataTable table : (List<MetadataTable>) tablesAll) {
            String oldName = oldTableMap.get(table.getId());
            String newName = table.getLabel();
            if (oldName != null && !oldName.equals(newName)) {
                schemaRenamedMap.put(prefix + oldName, prefix + newName);
            }
        }
        return schemaRenamedMap;
    }

    /**
     *
     * ggu Comment method "updateSchema".
     *
     * for repository wizard.
     */
    public static boolean updateSingleSchema(ConnectionItem connItem, final MetadataTable newTable,
            final IMetadataTable oldMetadataTable, Map<String, String> oldTableMap) {
        if (connItem == null) {
            return false;
        }
        Map<String, String> schemaRenamedMap = RepositoryUpdateManager.getSchemaRenamedMap(connItem, oldTableMap);
        boolean update = !schemaRenamedMap.isEmpty();

        if (!update) {
            if (newTable != null && oldMetadataTable != null && oldTableMap.containsKey(newTable.getId())) {
                if (GlobalServiceRegister.getDefault().isServiceRegistered(IMetadataManagmentService.class)) {
                    IMetadataManagmentService service = (IMetadataManagmentService) GlobalServiceRegister.getDefault()
                            .getService(IMetadataManagmentService.class);
                    IMetadataTable newMetadataTable = service.convertMetadataTable(newTable);
                    update = !oldMetadataTable.sameMetadataAs(newMetadataTable, IMetadataColumn.OPTIONS_NONE, true);
                    isAddColumn = isAddColumn(newMetadataTable, oldMetadataTable);
                }
            }
        }
        if (update) {
            // update
            return updateSchema(newTable, connItem, schemaRenamedMap, true, false);
        }
        return false;
    }

    public static boolean updateMultiSchema(ConnectionItem connItem, List<IMetadataTable> oldMetadataTable,
            Map<String, String> oldTableMap) {
        return updateMultiSchema(connItem, oldMetadataTable, oldTableMap, null);
    }

    public static boolean updateMultiSchema(ConnectionItem connItem, List<IMetadataTable> oldMetadataTable,
            Map<String, String> oldTableMap, String bwTableType) {
        if (connItem == null) {
            return false;
        }
        Map<String, String> schemaRenamedMap = RepositoryUpdateManager.getSchemaRenamedMap(connItem, oldTableMap);
        Map<String, EUpdateResult> deletedOrReselectTablesMap = null;
        boolean update = !schemaRenamedMap.isEmpty();
        boolean isDeleteOrReselect = false;
        Connection connection = connItem.getConnection();

        if (!update) {
            if (oldMetadataTable != null) {
                List<IMetadataTable> newMetadataTable = RepositoryUpdateManager
                        .getConversionMetadataTables(connItem.getConnection(), bwTableType);
                update = !RepositoryUpdateManager.sameAsMetadatTable(newMetadataTable, oldMetadataTable, oldTableMap);
                isAddColumn = isAddColumn(newMetadataTable, oldMetadataTable);
            }
        }
        /* if table has been deselect and select again,should propgate the update dialog */
        if (!update) {
            deletedOrReselectTablesMap = new HashMap<String, EUpdateResult>();

            List<IMetadataTable> newMetadataTable = new ArrayList<IMetadataTable>();
            if (coreService != null
                    && ((connection instanceof DatabaseConnection) || (connection instanceof GenericSchemaConnection))
                    || (connection instanceof SAPConnection)) {
                Set<org.talend.core.model.metadata.builder.connection.MetadataTable> newTables = null;
                if (bwTableType == null) {
                    newTables = ConnectionHelper.getTables(connection);
                } else {
                    newTables = SAPBWTableHelper.getBWTables(connection, bwTableType);
                }
                if (newTables != null) {
                    for (org.talend.core.model.metadata.builder.connection.MetadataTable originalTable : newTables) {
                        IMetadataTable conversionTable = coreService.convert(originalTable);
                        newMetadataTable.add(conversionTable);
                    }
                }
            }
            isDeleteOrReselect = isDeleteOrReselectMap(connItem, newMetadataTable, oldMetadataTable, deletedOrReselectTablesMap);
        }
        // update
        if (update) {
            return updateSchema(connItem, connItem, schemaRenamedMap, true, false);
        } else if (isDeleteOrReselect) {
            return updateDeleteOrReselectSchema(connItem, connItem, deletedOrReselectTablesMap, true, false);
        }
        return false;

    }

    protected static boolean isAddColumn(IMetadataTable tableFromMetadata, IMetadataTable tableFromProcess) {
        boolean isHaveAddColumn = false;
        for (IMetadataColumn columnFromMetadata : tableFromMetadata.getListColumns(true)) {
            boolean flag = false;
            for (IMetadataColumn columnFromProcess : tableFromProcess.getListColumns(true)) {
                if (columnFromMetadata.getLabel().equals(columnFromProcess.getLabel())) {
                    flag = true;
                }
            }
            if (!flag) {
                isHaveAddColumn = true;
                break;
            }
        }
        return isHaveAddColumn;
    }

    private static boolean isAddColumn(List<IMetadataTable> newTables, List<IMetadataTable> oldTables) {
        Map<String, IMetadataTable> id2TableMap = new HashMap<String, IMetadataTable>();
        for (IMetadataTable oldTable : oldTables) {
            id2TableMap.put(oldTable.getId(), oldTable);
        }

        for (IMetadataTable newTable : newTables) {
            IMetadataTable oldTable = id2TableMap.get(newTable.getId());
            if (oldTable == null) {
                return false;
            } else {
                if (isAddColumn(newTable, oldTable)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * DOC hywang Comment method "updateDeleteOrReselectSchema".
     */
    private static boolean updateDeleteOrReselectSchema(Object table, ConnectionItem connItem,
            Map<String, EUpdateResult> deletedOrReselectTablesMap, boolean show, boolean onlySimpleShow) {

        List<IRepositoryViewObject> updateList = new ArrayList<IRepositoryViewObject>();
        List<Relation> relations = RelationshipItemBuilder.getInstance().getItemsRelatedTo((connItem).getProperty().getId(),
                ItemCacheManager.LATEST_VERSION, RelationshipItemBuilder.PROPERTY_RELATION);

        /*
         * the id for schema which stored in .project file is like "_dlkjfhjkdfioi - metadata",not only indicate by a
         * single id but also table name,so if only find the relations by id and
         * RelationshipItemBuilder.PROPERTY_RELATION,it can't find
         */
        if (connItem instanceof GenericSchemaConnectionItem) {
            String id = (connItem).getProperty().getId();
            if (table instanceof MetadataTable) {
                id = id + " - " + ((MetadataTable) table).getLabel();
            }
            List<Relation> schemaRelations = RelationshipItemBuilder.getInstance().getItemsRelatedTo(id,
                    ItemCacheManager.LATEST_VERSION, RelationshipItemBuilder.SCHEMA_RELATION);
            if (!schemaRelations.isEmpty()) {
                relations.addAll(schemaRelations);
            }
        }

        RepositoryUpdateManager repositoryUpdateManager = new RepositoryUpdateManager(table, relations) {

            @Override
            public Set<EUpdateItemType> getTypes() {
                Set<EUpdateItemType> types = new HashSet<EUpdateItemType>();
                types.add(EUpdateItemType.NODE_SCHEMA);
                return types;
            }

        };

        // set renamed schema
        repositoryUpdateManager.setDeletedOrReselectTablesMap(deletedOrReselectTablesMap);

        final boolean doWork = repositoryUpdateManager.doWork(show, onlySimpleShow);
        repositoryUpdateManager.deletedOrReselectTablesMap.clear();
        return doWork;

    }

    /* hywang for bug 20024 */
    public static boolean isDeleteOrReselectMap(ConnectionItem connItem, List<IMetadataTable> newTables,
            List<IMetadataTable> oldTables, Map<String, EUpdateResult> deletedOrReselectTables) {
        for (IMetadataTable oldTable : oldTables) {
            String prefix;
            boolean isDeleted = true;
            String oldtableLabel = oldTable.getLabel();
            String oldtableId = oldTable.getId();
            for (IMetadataTable newTable : newTables) {
                String tableLabel = newTable.getLabel();
                String tableId = newTable.getId();
                if (tableLabel.equals(oldtableLabel)) {
                    isDeleted = false;
                    String newInnerIOType = newTable.getAdditionalProperties().get(SAPBWTableHelper.SAP_INFOOBJECT_INNER_TYPE);
                    String oldInnerIOType = oldTable.getAdditionalProperties().get(SAPBWTableHelper.SAP_INFOOBJECT_INNER_TYPE);
                    if (newInnerIOType != null) {
                        if (newInnerIOType.equals(oldInnerIOType) && !tableId.equals(oldtableId)) {
                            prefix = connItem.getProperty().getId() + UpdatesConstants.SEGMENT_LINE;
                            deletedOrReselectTables.put(prefix + tableLabel, EUpdateResult.RELOAD);
                        }
                        continue;
                    }
                    /* if table name is same but tableId is not same,means table has been deselect and reselect */
                    if (!tableId.equals(oldtableId)) {
                        prefix = connItem.getProperty().getId() + UpdatesConstants.SEGMENT_LINE;
                        deletedOrReselectTables.put(prefix + tableLabel, EUpdateResult.RELOAD);
                    }
                }
            }
            /* if can't find the name when looping the new tables,means the table has been removed */
            if (isDeleted) {
                prefix = connItem.getProperty().getId() + UpdatesConstants.SEGMENT_LINE;
                deletedOrReselectTables.put(prefix + oldtableLabel, EUpdateResult.DELETE);
            }
        }
        return !deletedOrReselectTables.isEmpty();
    }

    public static boolean updateWSDLConnection(ConnectionItem connectionItem, boolean show, final boolean onlySimpleShow) {
        List<IRepositoryViewObject> updateList = new ArrayList<IRepositoryViewObject>();
        List<Relation> relations = RelationshipItemBuilder.getInstance().getItemsRelatedTo(connectionItem.getProperty().getId(),
                RelationshipItemBuilder.LATEST_VERSION, RelationshipItemBuilder.PROPERTY_RELATION);

        RepositoryUpdateManager repositoryUpdateManager = new RepositoryUpdateManager(connectionItem, relations) {

            @Override
            public Set<EUpdateItemType> getTypes() {
                Set<EUpdateItemType> types = new HashSet<EUpdateItemType>();
                types.add(EUpdateItemType.NODE_PROPERTY);
                types.add(EUpdateItemType.NODE_SCHEMA);
                return types;
            }

        };
        return repositoryUpdateManager.doWork(true, false);
    }

    public static boolean updateMultiSchema(SAPFunctionUnit functionUnit, ConnectionItem connItem,
            List<IMetadataTable> oldMetadataTable, Map<String, String> oldTableMap) {
        if (functionUnit == null) {
            return false;
        }
        Map<String, String> schemaRenamedMap = RepositoryUpdateManager.getSchemaRenamedMap(functionUnit, connItem, oldTableMap);
        boolean update = !schemaRenamedMap.isEmpty();

        if (!update) {
            if (oldMetadataTable != null) {
                List<IMetadataTable> newMetadataTable = RepositoryUpdateManager.getConversionMetadataTables(functionUnit);
                update = !RepositoryUpdateManager.sameAsMetadatTable(newMetadataTable, oldMetadataTable, oldTableMap);
            }
        }
        // update
        if (update) {
            return updateSchema(functionUnit, connItem, schemaRenamedMap, true, false);
        }
        return false;

    }

    /**
     * MOD qiongli 2011-11-28 change this method 'private' into 'public'.it is used to judge if need to update DQ
     * analyses.
     */
    public static boolean sameAsMetadatTable(List<IMetadataTable> newTables, List<IMetadataTable> oldTables,
            Map<String, String> oldTableMap) {
        return sameAsMetadatTable(newTables, oldTables, oldTableMap, IMetadataColumn.OPTIONS_NONE);
    }

    public static boolean sameAsMetadatTable(List<IMetadataTable> newTables, List<IMetadataTable> oldTables,
            Map<String, String> oldTableMap, int options) {
        if (newTables == null || oldTables == null) {
            return false;
        }

        Map<String, IMetadataTable> id2TableMap = new HashMap<String, IMetadataTable>();
        for (IMetadataTable oldTable : oldTables) {
            id2TableMap.put(oldTable.getId(), oldTable);
        }

        for (IMetadataTable newTable : newTables) {
            IMetadataTable oldTable = id2TableMap.get(newTable.getId());
            if (oldTableMap.containsKey(newTable.getId())) { // not a new created table.
                if (oldTable == null) {
                    return false;
                } else {
                    if (!newTable.sameMetadataAs(oldTable, options)) {
                        return false;
                    }
                }
            }
        }
        return true;

    }

    /**
     *
     * xye Comment method "updateSAPFunction".
     *
     * @param sapFunction
     * @param show
     * @return
     */
    public static boolean updateSAPFunction(final SAPFunctionUnit sapFunction, boolean show, boolean onlySimpleShow) {
        List<IRepositoryViewObject> updateList = new ArrayList<IRepositoryViewObject>();
        List<Relation> relations = RelationshipItemBuilder.getInstance().getItemsRelatedTo(sapFunction.getId(),
                RelationshipItemBuilder.LATEST_VERSION, RelationshipItemBuilder.PROPERTY_RELATION);

        RepositoryUpdateManager repositoryUpdateManager = new RepositoryUpdateManager(sapFunction, relations) {

            @Override
            public Set<EUpdateItemType> getTypes() {
                Set<EUpdateItemType> types = new HashSet<EUpdateItemType>();
                types.add(EUpdateItemType.NODE_SAP_FUNCTION);
                types.add(EUpdateItemType.NODE_SCHEMA);
                return types;
            }

        };

        return repositoryUpdateManager.doWork(show, onlySimpleShow);
    }

    /**
     * DOC zli Comment method "updateSAPIDoc".
     *
     * @param sapIDoc
     * @param show
     * @param onlySimpleShow
     * @return
     */
    public static boolean updateSAPIDoc(final SAPIDocUnit sapIDoc, boolean show, boolean onlySimpleShow) {
        List<Relation> relations = RelationshipItemBuilder.getInstance().getItemsRelatedTo(sapIDoc.getId(),
                RelationshipItemBuilder.LATEST_VERSION, RelationshipItemBuilder.PROPERTY_RELATION);

        RepositoryUpdateManager repositoryUpdateManager = new RepositoryUpdateManager(sapIDoc, relations) {

            @Override
            public Set<EUpdateItemType> getTypes() {
                Set<EUpdateItemType> types = new HashSet<EUpdateItemType>();
                types.add(EUpdateItemType.NODE_SAP_IDOC);
                types.add(EUpdateItemType.NODE_SCHEMA);
                return types;
            }

        };

        return repositoryUpdateManager.doWork(show, onlySimpleShow);
    }

    /**
     *
     * xye Comment method "updateSAPFunction".
     *
     * @param sapFunction
     * @return
     */
    public static boolean updateSAPFunction(final SAPFunctionUnit sapFunction) {
        return updateSAPFunction(sapFunction, true, false);
    }

    /**
     * DOC zli Comment method "updateSAPIDoc".
     *
     * @param sapIDoc
     * @return
     */
    public static boolean updateSAPIDoc(final SAPIDocUnit sapIDoc) {
        return updateSAPIDoc(sapIDoc, true, false);
    }

    /**
     *
     * ggu Comment method "updateSchema".
     *
     * if show is false, will work for context menu action.
     */
    public static boolean updateSchema(final MetadataTable metadataTable, boolean show) {

        return updateSchema(metadataTable, null, null, show, false);
    }

    public static boolean updateSchema(final MetadataTable metadataTable, RepositoryNode node, boolean show,
            boolean onlySimpleShow) {
        ConnectionItem connItem = (ConnectionItem) node.getObject().getProperty().getItem();
        return updateSchema(metadataTable, connItem, null, show, onlySimpleShow);
    }

    protected static boolean updateSchema(final Object table, ConnectionItem connItem, Map<String, String> schemaRenamedMap,
            boolean show, boolean onlySimpleShow) {
        List<Relation> relations = RelationshipItemBuilder.getInstance().getItemsRelatedTo((connItem).getProperty().getId(),
                RelationshipItemBuilder.LATEST_VERSION, RelationshipItemBuilder.PROPERTY_RELATION);

        RepositoryUpdateManager repositoryUpdateManager = new RepositoryUpdateManager(table, relations) {

            @Override
            public Set<EUpdateItemType> getTypes() {
                Set<EUpdateItemType> types = new HashSet<EUpdateItemType>();
                types.add(EUpdateItemType.NODE_SCHEMA);
                return types;
            }

        };

        // set renamed schema
        repositoryUpdateManager.setSchemaRenamedMap(schemaRenamedMap);

        // set rename column
        if (!ConnectionColumnUpdateManager.getInstance().getColumnRenameMap().isEmpty()) {
            repositoryUpdateManager.setColumnRenamedMap(ConnectionColumnUpdateManager.getInstance().getColumnRenameMap());
        }

        return repositoryUpdateManager.doWork(show, onlySimpleShow);
    }

    /**
     *
     * ggu Comment method "updateQuery".
     *
     * for repository wizard.
     */
    public static boolean updateQuery(Query query) {

        return updateQueryObject(query, true, false);
    }

    public static boolean updateQuery(Query query, RepositoryNode node) {
        return updateQueryObject(query, true, false, node);
    }

    /**
     *
     * ggu Comment method "updateQuery".
     *
     * if show is false, will work for context menu action.
     */
    public static boolean updateQuery(Query query, boolean show) {
        return updateQueryObject(query, show, false);
    }

    public static boolean updateQuery(Query query, RepositoryNode node, boolean show, boolean onlySimpleShow) {
        return updateQueryObject(query, show, onlySimpleShow, node);
    }

    private static boolean updateQueryObject(Object parameter, boolean show, boolean onlySimpleShow) {
        List<Relation> relations = null;
        if (parameter instanceof Query) {
            relations = RelationshipItemBuilder.getInstance().getItemsRelatedTo(((Query) parameter).getId(),
                    RelationshipItemBuilder.LATEST_VERSION, RelationshipItemBuilder.QUERY_RELATION);
        } else if (parameter instanceof QueriesConnection) {
            relations = RelationshipItemBuilder.getInstance().getItemsRelatedTo(
                    ((QueriesConnection) parameter).getConnection().getId(), RelationshipItemBuilder.LATEST_VERSION,
                    RelationshipItemBuilder.QUERY_RELATION);
        }

        RepositoryUpdateManager repositoryUpdateManager = new RepositoryUpdateManager(parameter, relations) {

            @Override
            public Set<EUpdateItemType> getTypes() {
                Set<EUpdateItemType> types = new HashSet<EUpdateItemType>();
                types.add(EUpdateItemType.NODE_QUERY);
                return types;
            }

        };
        return repositoryUpdateManager.doWork(show, onlySimpleShow);
    }

    private static boolean updateQueryObject(Object parameter, boolean show, boolean onlySimpleShow, RepositoryNode node) {
        Item item = node.getObject().getProperty().getItem();
        List<Relation> relations = null;
        if (parameter instanceof Query) {
            String id = item.getProperty().getId();
            relations = RelationshipItemBuilder.getInstance().getItemsRelatedTo(id, RelationshipItemBuilder.LATEST_VERSION,
                    RelationshipItemBuilder.QUERY_RELATION);
        }

        RepositoryUpdateManager repositoryUpdateManager = new RepositoryUpdateManager(parameter, relations) {

            @Override
            public Set<EUpdateItemType> getTypes() {
                Set<EUpdateItemType> types = new HashSet<EUpdateItemType>();
                types.add(EUpdateItemType.NODE_QUERY);
                return types;
            }

        };
        return repositoryUpdateManager.doWork(show, onlySimpleShow);
    }

    /**
     *
     * ggu Comment method "updateContext".
     *
     * if show is false, will work for context menu action.
     */
    public static boolean updateContext(ContextItem item, boolean show) {
        return updateContext(null, item, show, false);
    }

    public static boolean updateContext(ContextItem item, boolean show, boolean onlySimpleShow) {
        return updateContext(null, item, show, onlySimpleShow, true);
    }

    /**
     *
     * ggu Comment method "updateContext".
     *
     * for repository wizard.
     */
    public static boolean updateContext(JobContextManager repositoryContextManager, ContextItem item) {

        return updateContext(repositoryContextManager, item, true, false);
    }

    private static boolean updateContext(JobContextManager repositoryContextManager, ContextItem item, boolean show,
            boolean onlySimpleShow) {
        return updateContext(repositoryContextManager, item, show, onlySimpleShow, false);
    }

    private static boolean updateContext(JobContextManager repositoryContextManager, ContextItem item, boolean show,
            boolean onlySimpleShow, boolean detectAddContextGroup) {
        List<Relation> relations = RelationshipItemBuilder.getInstance().getItemsRelatedTo(item.getProperty().getId(),
                RelationshipItemBuilder.LATEST_VERSION, RelationshipItemBuilder.CONTEXT_RELATION);

        RepositoryUpdateManager repositoryUpdateManager = new RepositoryUpdateManager(item, relations) {

            @Override
            public Set<EUpdateItemType> getTypes() {
                Set<EUpdateItemType> types = new HashSet<EUpdateItemType>();
                types.add(EUpdateItemType.CONTEXT);
                types.add(EUpdateItemType.CONTEXT_GROUP);
                return types;
            }

        };
        repositoryUpdateManager.checkAddContextGroup = detectAddContextGroup;
        repositoryUpdateManager.isConfigContextGroup = repositoryContextManager.isConfigContextGroup();
        if (repositoryContextManager != null) {
            // add for bug 9119 context group
            Map<ContextItem, List<IContext>> repositoryContextGroupMap = new HashMap<ContextItem, List<IContext>>();
            List<IContext> addGroupContext = repositoryContextManager.getAddGroupContext();
            if (!addGroupContext.isEmpty()) {
                repositoryContextGroupMap.put(item, addGroupContext);
            }
            repositoryUpdateManager.setRepositoryAddGroupContext(repositoryContextGroupMap);

            Map<ContextItem, List<IContext>> removeRepositoryContextGroupMap = new HashMap<ContextItem, List<IContext>>();
            List<IContext> removeGroupContext = repositoryContextManager.getRemoveGroupContext();
            if (!removeGroupContext.isEmpty()) {
                removeRepositoryContextGroupMap.put(item, removeGroupContext);
            }
            repositoryUpdateManager.setRepositoryRemoveGroupContext(removeRepositoryContextGroupMap);

            Map<ContextItem, List<IContext>> renameRepositoryContextGroupMap = new HashMap<ContextItem, List<IContext>>();
            Map<IContext, String> renameContextGroup = new HashMap<IContext, String>();
            Map<IContext, String> renameGroupContext = repositoryContextManager.getRenameGroupContext();
            List<IContext> renameGroupList = new ArrayList<IContext>();
            for (IContext renameGroup : renameGroupContext.keySet()) {
                renameGroupList.add(renameGroup);
                renameContextGroup.put(renameGroup, renameGroupContext.get(renameGroup));
            }
            if (!renameGroupContext.isEmpty()) {
                renameRepositoryContextGroupMap.put(item, renameGroupList);
            }
            repositoryUpdateManager.setRepositoryRenameGroupContext(renameRepositoryContextGroupMap);
            repositoryUpdateManager.setRenameContextGroup(renameContextGroup);

            Map<ContextItem, Map<String, String>> repositoryRenamedMap = new HashMap<ContextItem, Map<String, String>>();
            if (!repositoryContextManager.getNameMap().isEmpty()) {
                repositoryRenamedMap.put(item, repositoryContextManager.getNameMap());
            }
            repositoryUpdateManager.setContextRenamedMap(repositoryRenamedMap);

            // newly added parameters
            Map<Item, Set<String>> newParametersMap = new HashMap<Item, Set<String>>();
            if (!repositoryContextManager.getNewParameters().isEmpty()) {
                newParametersMap.put(item, repositoryContextManager.getNewParameters());
            }
            repositoryUpdateManager.setNewParametersMap(newParametersMap);
        }
        try {
            repositoryUpdateManager.updateConnection(item);
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }

        return repositoryUpdateManager.doWork(show, onlySimpleShow);
    }

    public Map<Item, Set<String>> getNewParametersMap() {
        return newParametersMap;
    }

    public void setNewParametersMap(Map<Item, Set<String>> newParametersMap) {
        this.newParametersMap = newParametersMap;
    }

    public static boolean updateAllJob() {
        return updateAllJob(false);
    }

    public static boolean updateAllJob(boolean isDetectAndUpdate) {
        RepositoryUpdateManager repositoryUpdateManager = new RepositoryUpdateManager(null, isDetectAndUpdate) {

            @Override
            public Set<IUpdateItemType> getTypes() {
                IUpdateItemType[] allUpdateItemTypes = UpdateManagerProviderDetector.INSTANCE.getAllUpdateItemTypes();
                Set<IUpdateItemType> types = new HashSet<IUpdateItemType>(Arrays.asList(allUpdateItemTypes));
                return types;
            }

        };
        return repositoryUpdateManager.doWork();
    }

    public static List<IMetadataTable> getConversionMetadataTables(Connection conn) {
        return getConversionMetadataTables(conn, null);
    }

    @SuppressWarnings("unchecked")
    public static List<IMetadataTable> getConversionMetadataTables(Connection conn, String bwTableType) {
        if (conn == null) {
            return Collections.emptyList();
        }
        List<IMetadataTable> tables = new ArrayList<IMetadataTable>();
        Set tables2 = null;
        if (bwTableType != null) {
            tables2 = SAPBWTableHelper.getBWTables(conn, bwTableType);
        } else {
            tables2 = ConnectionHelper.getTables(conn);
        }
        if (tables2 != null) {
            for (org.talend.core.model.metadata.builder.connection.MetadataTable originalTable : (Set<org.talend.core.model.metadata.builder.connection.MetadataTable>) tables2) {
                if (GlobalServiceRegister.getDefault().isServiceRegistered(IMetadataManagmentService.class)) {
                    IMetadataManagmentService service = (IMetadataManagmentService) GlobalServiceRegister.getDefault()
                            .getService(IMetadataManagmentService.class);
                    IMetadataTable conversionTable = service.convertMetadataTable(originalTable);
                    tables.add(conversionTable);
                }
            }
        }

        return tables;
    }

    @SuppressWarnings("unchecked")
    public static List<IMetadataTable> getConversionMetadataTables(SAPFunctionUnit functionUnit) {
        if (functionUnit == null) {
            return Collections.emptyList();
        }
        List<IMetadataTable> tables = new ArrayList<IMetadataTable>();
        List<MetadataTable> tablesAll = new ArrayList<MetadataTable>();
        tablesAll.addAll(functionUnit.getTables());
        tablesAll.addAll(functionUnit.getInputTables());
        for (org.talend.core.model.metadata.builder.connection.MetadataTable originalTable : tablesAll) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IMetadataManagmentService.class)) {
                IMetadataManagmentService service = (IMetadataManagmentService) GlobalServiceRegister.getDefault()
                        .getService(IMetadataManagmentService.class);
                IMetadataTable conversionTable = service.convertMetadataTable(originalTable);
                tables.add(conversionTable);
            }
        }

        return tables;
    }

    public static boolean updateJoblet(JobletProcessItem item, boolean show, boolean onlySimpleShow) {
        List<Relation> relations = RelationshipItemBuilder.getInstance().getItemsRelatedTo(item.getProperty().getId(),
                RelationshipItemBuilder.LATEST_VERSION, RelationshipItemBuilder.JOBLET_RELATION);

        RepositoryUpdateManager repositoryUpdateManager = new RepositoryUpdateManager(item, relations) {

            @Override
            public Set<EUpdateItemType> getTypes() {
                Set<EUpdateItemType> types = new HashSet<EUpdateItemType>();
                types.add(EUpdateItemType.RELOAD);
                return types;
            }

        };
        repositoryUpdateManager.setOnlyOpeningJob(true);

        return repositoryUpdateManager.doWork(show, onlySimpleShow);
    }

    public Map<String, EUpdateResult> getDeletedOrReselectTablesMap() {
        return deletedOrReselectTablesMap;
    }

    public void setDeletedOrReselectTablesMap(Map<String, EUpdateResult> deletedOrReselectTablesMap) {
        this.deletedOrReselectTablesMap = deletedOrReselectTablesMap;
    }

    // Added TDQ-11688 20170309 yyin
    public static boolean updateDQPattern(Item patternItem) {
        List<IRepositoryViewObject> updateList = new ArrayList<IRepositoryViewObject>();
        List<Relation> relations = RelationshipItemBuilder.getInstance().getItemsRelatedTo(patternItem.getProperty().getId(),
                RelationshipItemBuilder.LATEST_VERSION, RelationshipItemBuilder.PATTERN_RELATION);

        RepositoryUpdateManager repositoryUpdateManager = new RepositoryUpdateManager(patternItem, relations) {

            @Override
            public Set<EUpdateItemType> getTypes() {
                Set<EUpdateItemType> types = new HashSet<EUpdateItemType>();
                types.add(EUpdateItemType.NODE_PROPERTY);

                return types;
            }

        };
        return repositoryUpdateManager.doWork(true, false);
    }

    public static IRepositoryContextUpdateService findContextParameterUpdater(Connection connection) {
        if (CONTEXT_UPDATE_SERVICE_LIST == null) {
            CONTEXT_UPDATE_SERVICE_LIST = GlobalServiceRegister.getDefault()
                    .findAllService(IRepositoryContextUpdateService.class);
        }
        for (IRepositoryContextUpdateService updater : CONTEXT_UPDATE_SERVICE_LIST) {
            if (updater.accept(connection)) {
                return updater;
            }
        }

        LOGGER.error(
                "Can't find any connection context parameter updater for connection type:" + connection.getClass().getName());
        return null;
    }
}
