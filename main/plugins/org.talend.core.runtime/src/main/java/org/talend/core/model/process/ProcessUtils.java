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
package org.talend.core.model.process;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ITDQItemService;
import org.talend.core.PluginChecker;
import org.talend.core.hadoop.IHadoopClusterService;
import org.talend.core.hadoop.repository.HadoopRepositoryUtil;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.SQLPatternItem;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IExtendedRepositoryNodeHandler;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryContentManager;
import org.talend.core.model.routines.RoutinesUtil;
import org.talend.core.model.utils.SQLPatternUtils;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.process.TalendProcessArgumentConstant;
import org.talend.core.ui.IJobletProviderService;
import org.talend.core.ui.ITestContainerCoreService;
import org.talend.core.ui.ITestContainerProviderService;
import org.talend.core.utils.BitwiseOptionUtils;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.MetadataType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.RoutinesParameterType;
import org.talend.designer.runprocess.ItemCacheManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IProxyRepositoryService;

/**
 * DOC bqian class global comment. Detailled comment
 */
@SuppressWarnings("unchecked")
public final class ProcessUtils {

    private static boolean jarWithContext;

    private static List<IProcess> fakeProcesses = new ArrayList<IProcess>();

    private static IHadoopClusterService hadoopClusterService = null;
    static {
        hadoopClusterService = HadoopRepositoryUtil.getHadoopClusterService();
    }

    public static void clearFakeProcesses() {
        for (IProcess p : fakeProcesses) {
            if (p instanceof IProcess2) {
                ((IProcess2) p).dispose();
            }
        }
        fakeProcesses.clear();
    }

    private static void createFakeProcesses(Collection<Item> items) {
        IDesignerCoreService designerCoreService = CoreRuntimePlugin.getInstance().getDesignerCoreService();
        if (items != null) {
            for (Item item : items) {
                if (item == null || existedFakeProcess(item.getProperty().getId())) {
                    continue;
                }
                IProcess process = null;
                if (item instanceof ProcessItem) {
                    process = designerCoreService.getProcessFromProcessItem((ProcessItem) item);
                } else if (item instanceof JobletProcessItem) {
                    process = designerCoreService.getProcessFromJobletProcessItem((JobletProcessItem) item);
                }
                if (process != null) {
                    fakeProcesses.add(process);
                }
            }
        }
    }

    private static boolean existedFakeProcess(String id) {
        for (IProcess p : fakeProcesses) {
            if (p.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    private static IProcess[] checkAndGetFakeProcesses(Collection<Item> items) {
        List<IProcess> tmpProcesses = new ArrayList<IProcess>();

        createFakeProcesses(items);
        if (items != null) {
            List<String> ids = new ArrayList<String>();
            for (Item item : items) {
                String id = item.getProperty().getId();
                if (!ids.contains(id)) {
                    for (IProcess p : fakeProcesses) {
                        if (p != null && p.getId().equals(id)) {
                            ids.add(p.getId());
                            tmpProcesses.add(p);
                        }
                    }
                }
            }
        }
        return tmpProcesses.toArray(new IProcess[0]);
    }

    public static Collection<IRepositoryViewObject> getProcessDependencies(ERepositoryObjectType dependencyType,
            Collection<Item> items) {
        return getProcessDependencies(dependencyType, items, true);
    }

    public static Collection<IRepositoryViewObject> getProcessDependencies(ERepositoryObjectType dependencyType,
            Collection<Item> items, boolean withSystem) {

        if (dependencyType == ERepositoryObjectType.CONTEXT) {
            return getContextDependenciesOfProcess(items);
        } else if (dependencyType == ERepositoryObjectType.METADATA) {
            return getMetadataDependenciesOfProcess(items);
        } else if (dependencyType == ERepositoryObjectType.PROCESS) {
            return getChildPorcessDependenciesOfProcess(items, true);
        } else if (dependencyType == ERepositoryObjectType.JOBLET) {
            return getJobletDependenciesOfProcess(items, true);
        } else if (dependencyType == ERepositoryObjectType.SQLPATTERNS) {
            return getSQLTemplatesDependenciesOfProcess(items, withSystem);
        } else if (dependencyType == ERepositoryObjectType.ROUTINES) {
            return getRoutineDependenciesOfProcess(items, withSystem, true);
        } else {
            return Collections.emptyList();
        }

    }

    public static Collection<IRepositoryViewObject> getAllProcessDependencies(Collection<Item> items) {
        final List<IRepositoryViewObject> repositoryObjects = new ArrayList<IRepositoryViewObject>();
        if (items == null) {
            return repositoryObjects;
        }
        for (Item item : items) {
            checkItemDependencies(item, repositoryObjects);
        }
        return repositoryObjects;
    }

    private static void checkItemDependencies(Item item, List<IRepositoryViewObject> repositoryObjects) {
        if (item == null) {
            return;
        }
        IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        RelationshipItemBuilder builder = RelationshipItemBuilder.getInstance();

        List<Relation> relations = builder.getItemsRelatedTo(item.getProperty().getId(), item.getProperty().getVersion(),
                RelationshipItemBuilder.JOB_RELATION);
        // TDQ-12325 should contain joblet related dependencies.
        relations.addAll(builder.getItemsRelatedTo(item.getProperty().getId(), item.getProperty().getVersion(),
                RelationshipItemBuilder.JOBLET_RELATION));
        for (Relation relation : relations) {
            IRepositoryViewObject obj = null;
            try {
                if (RelationshipItemBuilder.MAPPER_RELATION.equals(relation.getType())) {
                    IRepositoryViewObject mapperObj = factory.getLastVersion(relation.getId());
                    if (mapperObj != null) {
                        if (!repositoryObjects.contains(mapperObj)) {
                            repositoryObjects.add(mapperObj);
                        }
                        for (IExtendedRepositoryNodeHandler nodeHandler : RepositoryContentManager.getExtendedNodeHandler()) {
                            List<IRepositoryViewObject> nodesAndDependencies = nodeHandler
                                    .getRepositoryObjectDependencies(mapperObj);
                            for (IRepositoryViewObject refObject : nodesAndDependencies) {
                                if (!repositoryObjects.contains(refObject)) {
                                    repositoryObjects.add(refObject);
                                }
                            }
                        }
                    }
                } else if (RelationshipItemBuilder.ROUTINE_RELATION.equals(relation.getType())) {
                    obj = RoutinesUtil.getRoutineFromName(relation.getId());
                } else {
                    obj = factory.getLastVersion(relation.getId());
                }
                if (obj != null) {
                    updateRepositoryObjects(repositoryObjects, obj);
                    IRepositoryViewObject clusterObj = getHadoopClusterConnectionIfNeeded(obj, factory);
                    if (clusterObj != null) {
                        updateRepositoryObjects(repositoryObjects, clusterObj);
                    }
                }
            } catch (PersistenceException et) {
                ExceptionHandler.process(et);
            }
        }
    }

    /**
     * DOC cmeng Comment method "updateRepositoryObjects".
     *
     * @param repositoryObjects
     * @param obj
     */
    private static void updateRepositoryObjects(List<IRepositoryViewObject> repositoryObjects, IRepositoryViewObject obj) {
        if (!repositoryObjects.contains(obj)) {
            repositoryObjects.add(obj);
            // TDQ-12162,should call 'checkItemDependencies(...)' first. for
            // example,"sub job C->sub job B->main job A->Rule"
            // export script C,if call 'checkAllVerSionLatest(...)' first,the dependency X won't be added.
            checkItemDependencies(obj.getProperty().getItem(), repositoryObjects);
            checkAllVerSionLatest(repositoryObjects, obj);
        }
    }

    /**
     * To check the obj, if the obj is a subItem of a Hadoop Cluster Metadata, then need to get the Cluster connection
     * item too.
     *
     * @param obj
     * @param factory
     * @return
     */
    private static IRepositoryViewObject getHadoopClusterConnectionIfNeeded(IRepositoryViewObject obj,
            IProxyRepositoryFactory factory) {
        if (hadoopClusterService == null || obj == null) {
            return null;
        }
        Property property = obj.getProperty();
        if (property == null) {
            return null;
        }
        Item subItem = property.getItem();
        if (subItem == null) {
            return null;
        }
        if (!hadoopClusterService.isHadoopSubItem(subItem)) {
            return null;
        }
        Item clusterItem = hadoopClusterService.getHadoopClusterBySubitemId(property.getId());
        if (clusterItem == null) {
            return null;
        }
        Property clusterProperty = clusterItem.getProperty();
        if (clusterProperty == null) {
            return null;
        }
        try {
            return factory.getLastVersion(clusterProperty.getId());
        } catch (PersistenceException et) {
            ExceptionHandler.process(et);
        }
        return null;
    }

    private static void checkAllVerSionLatest(List<IRepositoryViewObject> repositoryObjects, IRepositoryViewObject object) {
        IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        RelationshipItemBuilder builder = RelationshipItemBuilder.getInstance();
        List<Relation> relations = builder.getItemsJobRelatedTo(object.getId(), object.getVersion(),
                RelationshipItemBuilder.JOB_RELATION);
        for (Relation relation : relations) {
            try {
                IRepositoryViewObject obj = factory.getLastVersion(relation.getId());
                if (obj != null) {
                    if (!repositoryObjects.contains(obj)) {
                        repositoryObjects.add(obj);
                        checkAllVerSionLatest(repositoryObjects, obj);
                    }
                }
            } catch (PersistenceException et) {
                ExceptionHandler.process(et);
            }
        }
    }

    public static Collection<IRepositoryViewObject> getAllProcessDependencies(Collection<Item> items, boolean withSystem) {
        Collection<IRepositoryViewObject> dependencies = new ArrayList<IRepositoryViewObject>();
        dependencies = getAllProcessDependencies(items);

        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQItemService.class)) {
            ITDQItemService tdqItemService = (ITDQItemService) GlobalServiceRegister.getDefault().getService(
                    ITDQItemService.class);
            if (tdqItemService != null && tdqItemService.hasProcessItemDependencies(items)) {
                dependencies.addAll(tdqItemService.getProcessItemDependencies(items));
            }
        }
        return dependencies;
    }

    private static Collection<IRepositoryViewObject> getContextDependenciesOfProcess(Collection<Item> items) {
        Collection<IRepositoryViewObject> repositoryObjects = new ArrayList<IRepositoryViewObject>();
        for (Item item : items) {
            if (item == null) {
                continue;
            }
            ProcessType process = null;
            if (item instanceof ProcessItem) {
                process = ((ProcessItem) item).getProcess();
            } else if (item instanceof JobletProcessItem) {
                process = ((JobletProcessItem) item).getJobletProcess();
            }
            if (process != null && !process.getContext().isEmpty()) {
                ContextType contextType = (ContextType) process.getContext().get(0);
                for (ContextParameterType param : (List<ContextParameterType>) contextType.getContextParameter()) {
                    String repositoryContextId = param.getRepositoryContextId();
                    if (repositoryContextId != null && !"".equals(repositoryContextId)) { //$NON-NLS-1$
                        try {
                            IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
                            IRepositoryViewObject lastVersion = factory.getLastVersion(repositoryContextId);
                            if (lastVersion != null) {
                                if (!repositoryObjects.contains(lastVersion)) {
                                    repositoryObjects.add(lastVersion);
                                }
                            }

                        } catch (PersistenceException e) {
                            ExceptionHandler.process(e);
                        }

                    }
                }

            }
        }
        return repositoryObjects;
    }

    private static Collection<IRepositoryViewObject> getMetadataDependenciesOfProcess(Collection<Item> items) {
        Collection<IRepositoryViewObject> repositoryObjects = new ArrayList<IRepositoryViewObject>();
        for (IProcess process : checkAndGetFakeProcesses(items)) {
            if (process != null) {
                List<INode> nodes = (List<INode>) process.getGraphicalNodes();
                for (INode node : nodes) {
                    // List<IElementParameter> eleParams = (List<IElementParameter>) node.getElementParameters();
                    // for (IElementParameter elementParameter : eleParams) {

                    IElementParameter propertyParam = node.getElementParameter("PROPERTY");//$NON-NLS-1$
                    String repositoryMetadataId = ""; //$NON-NLS-1$
                    if (propertyParam != null) {
                        Map<String, IElementParameter> propertyChildParameters = propertyParam.getChildParameters();
                        if (propertyChildParameters != null) {
                            IElementParameter subPropertyTypeElement = propertyChildParameters.get("REPOSITORY_PROPERTY_TYPE");
                            if (subPropertyTypeElement != null) {
                                repositoryMetadataId = (String) subPropertyTypeElement.getValue();
                            }
                        }
                        addRepositoryObject(repositoryMetadataId, repositoryObjects);
                    }
                    IElementParameter schemaParam = node.getElementParameter("SCHEMA");//$NON-NLS-1$
                    if (schemaParam != null) {
                        Map<String, IElementParameter> schemaChildParameters = schemaParam.getChildParameters();
                        if (schemaChildParameters != null) {
                            IElementParameter subSchemaElement = schemaChildParameters.get("REPOSITORY_SCHEMA_TYPE");
                            if (subSchemaElement != null) {
                                repositoryMetadataId = (String) subSchemaElement.getValue();
                            }
                        }
                        addRepositoryObject(repositoryMetadataId, repositoryObjects);
                    }
                    IElementParameter querystoreParam = node.getElementParameter("QUERYSTORE");//$NON-NLS-1$
                    if (querystoreParam != null) {
                        Map<String, IElementParameter> queryChildParameters = querystoreParam.getChildParameters();
                        if (queryChildParameters != null) {
                            IElementParameter subQueryElement = queryChildParameters.get("REPOSITORY_QUERYSTORE_TYPE");
                            if (subQueryElement != null) {
                                repositoryMetadataId = (String) subQueryElement.getValue();
                            }
                        }
                        addRepositoryObject(repositoryMetadataId, repositoryObjects);
                    }

                    // handle custom eleParams
                    List<IElementParameter> eleParams = (List<IElementParameter>) node.getElementParameters();
                    for (IElementParameter elementParameter : eleParams) {
                        if (elementParameter.getFieldType().equals(EParameterFieldType.SURVIVOR_RELATION)) {
                            repositoryMetadataId = (String) elementParameter.getValue();
                        }
                        addRepositoryObject(repositoryMetadataId, repositoryObjects);
                    }

                }

            }

        }
        // }
        return repositoryObjects;
    }

    private static void addRepositoryObject(String repositoryMetadataId, Collection<IRepositoryViewObject> repositoryObjects) {
        String[] id = repositoryMetadataId.split(" - "); //$NON-NLS-1$
        if (id.length > 0) {

            if (repositoryMetadataId != null && !repositoryMetadataId.equals("")) { //$NON-NLS-1$
                try {
                    IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
                    IRepositoryViewObject lastVersion = factory.getLastVersion(id[0].trim());
                    if (lastVersion != null) {
                        if (!repositoryObjects.contains(lastVersion)) {
                            repositoryObjects.add(lastVersion);
                        }
                    }
                } catch (PersistenceException e) {
                    ExceptionHandler.process(e);
                }

            }
        }
    }

    private static Collection<IRepositoryViewObject> getChildPorcessDependenciesOfProcess(Collection<Item> items,
            boolean needCheckSubProcess) {
        List<IRepositoryViewObject> returnListObject = new ArrayList<IRepositoryViewObject>();
        Map<String, Item> returnItems = new HashMap<String, Item>();
        // Collection<IRepositoryObject> repositoryObjects = new ArrayList<IRepositoryObject>();
        for (IProcess process : checkAndGetFakeProcesses(items)) {
            if (process != null) {
                List<INode> nodes = (List<INode>) process.getGraphicalNodes();
                for (INode node : nodes) {
                    IElementParameter processParam = node.getElementParameter("PROCESS"); //$NON-NLS-1$
                    if (processParam != null) {
                        String repositoryProcessId = (String) processParam.getChildParameters()
                                .get("PROCESS_TYPE_PROCESS").getValue(); //$NON-NLS-1$
                        String repositoryProcessVersion = (String) processParam.getChildParameters().get("PROCESS_TYPE_VERSION")
                                .getValue();
                        if (repositoryProcessId != null && !repositoryProcessId.equals("")) { //$NON-NLS-1$
                            try {
                                IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
                                IRepositoryViewObject foundObject = null;
                                if (repositoryProcessVersion == null
                                        || repositoryProcessVersion.equals(ItemCacheManager.LATEST_VERSION)) {
                                    foundObject = factory.getLastVersion(repositoryProcessId);

                                } else {
                                    List<IRepositoryViewObject> allVersion = factory.getAllVersion(repositoryProcessId);
                                    if (allVersion != null && !allVersion.isEmpty()) {
                                        for (IRepositoryViewObject object : allVersion) {
                                            if (VersionUtils.compareTo(object.getVersion(), repositoryProcessVersion) == 0) {
                                                foundObject = object;
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (foundObject != null) {
                                    if (!returnListObject.contains(foundObject)) {
                                        // repositoryObjects.add(lastVersion);
                                        returnListObject.add(foundObject);
                                    }
                                    if (needCheckSubProcess) {

                                        Item item2 = foundObject.getProperty().getItem();
                                        if (item2 != null) {
                                            Item foundItem = returnItems.get(item2.getProperty().getId());
                                            if (foundItem == null) {
                                                returnItems.put(item2.getProperty().getId(), item2);
                                                returnListObject.addAll(getContextDependenciesOfProcess(returnItems.values()));
                                                returnListObject.addAll(getMetadataDependenciesOfProcess(returnItems.values()));
                                                returnListObject.addAll(getChildPorcessDependenciesOfProcess(
                                                        returnItems.values(), needCheckSubProcess));
                                                returnListObject.addAll(getJobletDependenciesOfProcess(returnItems.values(),
                                                        needCheckSubProcess));
                                            }
                                        }
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
        return returnListObject;

    }

    private static Collection<IRepositoryViewObject> getJobletDependenciesOfProcess(Collection<Item> items,
            boolean needCheckSubProcess) {
        Map<String, Item> returnItems = new HashMap<String, Item>();
        Collection<IRepositoryViewObject> repositoryObjects = new ArrayList<IRepositoryViewObject>();
        for (IProcess process : checkAndGetFakeProcesses(items)) {
            if (process != null) {
                List<INode> nodes = (List<INode>) process.getGraphicalNodes();
                for (INode node : nodes) {

                    if (PluginChecker.isJobLetPluginLoaded()) {
                        IJobletProviderService service = (IJobletProviderService) GlobalServiceRegister.getDefault().getService(
                                IJobletProviderService.class);
                        if (service != null && service.isJobletComponent(node)) {
                            Property jobletItem = service.getJobletComponentItem(node);
                            if (jobletItem == null) {
                                continue;
                            }
                            String repositoryMetadataId = jobletItem.getId();
                            if (repositoryMetadataId != null && !repositoryMetadataId.equals("")) { //$NON-NLS-1$
                                try {
                                    IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
                                    IRepositoryViewObject lastVersion = factory.getLastVersion(repositoryMetadataId);
                                    if (lastVersion != null) {
                                        if (!repositoryObjects.contains(lastVersion)) {
                                            repositoryObjects.add(lastVersion);
                                        }
                                        if (needCheckSubProcess) {
                                            Item childItem = lastVersion.getProperty().getItem();
                                            if (childItem != null) {
                                                Item foundItem = returnItems.get(childItem.getProperty().getId());
                                                if (foundItem == null) {
                                                    returnItems.put(childItem.getProperty().getId(), childItem);
                                                    repositoryObjects
                                                            .addAll(getContextDependenciesOfProcess(returnItems.values()));
                                                    repositoryObjects.addAll(getMetadataDependenciesOfProcess(returnItems
                                                            .values()));
                                                    repositoryObjects.addAll(getChildPorcessDependenciesOfProcess(
                                                            returnItems.values(), needCheckSubProcess));
                                                    repositoryObjects.addAll(getJobletDependenciesOfProcess(returnItems.values(),
                                                            needCheckSubProcess));
                                                }

                                            }
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

        }
        return repositoryObjects;
    }

    private static Collection<IRepositoryViewObject> getSQLTemplatesDependenciesOfProcess(Collection<Item> items,
            boolean withSytem) {
        Collection<IRepositoryViewObject> repositoryObjects = new ArrayList<IRepositoryViewObject>();
        for (IProcess process : checkAndGetFakeProcesses(items)) {
            if (process != null) {
                List<INode> nodes = (List<INode>) process.getGraphicalNodes();
                for (INode node : nodes) {
                    IElementParameter sqlTemplateParam = node.getElementParameter("SQLPATTERN_VALUE"); //$NON-NLS-1$
                    if (sqlTemplateParam != null && sqlTemplateParam.getFieldType() == EParameterFieldType.TABLE) {
                        List<Map<String, Object>> values = (List<Map<String, Object>>) sqlTemplateParam.getValue();
                        if (values != null) {
                            for (Map<String, Object> line : values) {
                                Object object = line.get(SQLPatternUtils.SQLPATTERNLIST);
                                if (object instanceof String) {
                                    String[] idAndLable = ((String) object).split(SQLPatternUtils.ID_SEPARATOR);
                                    if (idAndLable.length > 1) {
                                        IRepositoryViewObject repositoryObject = SQLPatternUtils
                                                .getLastVersionRepositoryObjectById(idAndLable[0]);
                                        if (repositoryObject != null && repositoryObject.getLabel().equals(idAndLable[1])) {
                                            Item item2 = repositoryObject.getProperty().getItem();
                                            if (item2 instanceof SQLPatternItem) {
                                                if (!((SQLPatternItem) item2).isSystem() || ((SQLPatternItem) item2).isSystem()
                                                        && withSytem) {
                                                    repositoryObjects.add(repositoryObject);
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
        }
        return repositoryObjects;
    }

    private static Collection<IRepositoryViewObject> getRoutineDependenciesOfProcess(Collection<Item> items, boolean withSystem,
            boolean needCheckSubProcess) {
        Collection<IRepositoryViewObject> repositoryObjects = new HashSet<IRepositoryViewObject>();

        IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();

        List<IRepositoryViewObject> systemRoutines = null;
        if (withSystem) {
            systemRoutines = RoutinesUtil.getCurrentSystemRoutines();
        }
        for (Item item : items) {
            if (item instanceof ProcessItem) {
                for (RoutinesParameterType infor : (List<RoutinesParameterType>) ((ProcessItem) item).getProcess()
                        .getParameters().getRoutinesParameter()) {
                    // no need system item
                    if (withSystem && systemRoutines != null) {
                        for (IRepositoryViewObject obj : systemRoutines) {
                            if (obj.getLabel().equals(infor.getName()) && obj.getId().equals(infor.getId())) {
                                repositoryObjects.add(obj);
                                break;
                            }
                        }
                    } else {
                        try {
                            if (infor.getId() != null) { // to avoid warning in case of null id
                                IRepositoryViewObject lastVersion = factory.getLastVersion(infor.getId());
                                if (lastVersion != null) {
                                    repositoryObjects.add(lastVersion);
                                }
                            }
                        } catch (PersistenceException e) {
                            ExceptionHandler.process(e);
                        }

                    }

                }

            }
        }
        if (needCheckSubProcess) {

            Collection<IRepositoryViewObject> dependenciesJobs = getChildPorcessDependenciesOfProcess(items, needCheckSubProcess);
            if (!dependenciesJobs.isEmpty()) {
                List<Item> dependenciesJobItems = new ArrayList<Item>();
                for (IRepositoryViewObject obj : dependenciesJobs) {
                    dependenciesJobItems.add(obj.getProperty().getItem());
                }
                Collection<IRepositoryViewObject> routineDependenciesOfProcess = getRoutineDependenciesOfProcess(
                        dependenciesJobItems, withSystem, needCheckSubProcess);
                repositoryObjects.addAll(routineDependenciesOfProcess);
            }
        }
        return repositoryObjects;
    }

    public static MetadataType getOutputMetadata(ProcessType processType) {
        for (Object nodeObject : processType.getNode()) {
            NodeType nodeType = (NodeType) nodeObject;
            if (nodeType.getComponentName().equals("tBufferOutput")) { //$NON-NLS-1$
                MetadataType metadataType = (MetadataType) nodeType.getMetadata().get(0);
                return metadataType;
            }
        }
        return null;
    }

    public static boolean isTestContainer(IProcess process) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
            ITestContainerProviderService testContainerService = (ITestContainerProviderService) GlobalServiceRegister
                    .getDefault().getService(ITestContainerProviderService.class);
            if (testContainerService != null) {
                return testContainerService.isTestContainerProcess(process);
            }
        }
        return false;
    }

    public static String getTestDataValue(IProcess process, String instance, String testData) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
            ITestContainerProviderService testContainerService = (ITestContainerProviderService) GlobalServiceRegister
                    .getDefault().getService(ITestContainerProviderService.class);
            if (testContainerService != null) {
                return testContainerService.getTestDataValue(process, instance, testData);
            }
        }
        return null;
    }

    public static String getTestDataParamemter(IProcess process, String instance, String testData) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
            ITestContainerProviderService testContainerService = (ITestContainerProviderService) GlobalServiceRegister
                    .getDefault().getService(ITestContainerProviderService.class);
            if (testContainerService != null) {
                return testContainerService.getTestDataParamemter(process, instance, testData);
            }
        }
        return null;
    }

    public static List<String> getTestInstances(IProcess process) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
            ITestContainerProviderService testContainerService = (ITestContainerProviderService) GlobalServiceRegister
                    .getDefault().getService(ITestContainerProviderService.class);
            if (testContainerService != null) {
                return testContainerService.getTestInstances(process);
            }
        }
        return new ArrayList<String>();
    }

    public static String getInstanceContext(IProcess process, String instance) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
            ITestContainerProviderService testContainerService = (ITestContainerProviderService) GlobalServiceRegister
                    .getDefault().getService(ITestContainerProviderService.class);
            if (testContainerService != null) {
                return testContainerService.getInstanceContext(process, instance);
            }
        }
        return null;
    }

    public static int getAssertAmount(IProcess process) {
        int count = 0;
        if (process instanceof IProcess2) {
            Item item = ((IProcess2) process).getProperty().getItem();
            if (item instanceof ProcessItem) {
                for (Object obj : ((ProcessItem) item).getProcess().getNode()) {
                    if (obj instanceof NodeType) {
                        // tAssert will be sued on DI job, tCollectAndCheck will be used on Spark job
                        if (((NodeType) obj).getComponentName().equals("tAssert")
                                || ((NodeType) obj).getComponentName().equals("tCollectAndCheck")) {
                            count++;
                        }
                    }
                }
                return count;
            }
        } else {
            for (INode node : process.getGraphicalNodes()) {
                // tAssert will be sued on DI job, tCollectAndCheck will be used on Spark job
                if (node.getComponent().getName().equals("tAssert") || node.getComponent().getName().equals("tCollectAndCheck")) {
                    count++;
                }
            }
        }
        return count;
    }

    public static boolean isSpark(IProcess process) {
        if (process instanceof IProcess2) {
            return false;
        }
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerCoreService.class)) {
            ITestContainerCoreService testContainerService = (ITestContainerCoreService) GlobalServiceRegister.getDefault()
                    .getService(ITestContainerCoreService.class);
            if (testContainerService != null) {
                return testContainerService.isSpark(((IProcess2) process).getProperty().getItem());
            }
        }
        return false;
    }

    public static boolean isSparkStreming(IProcess process) {
        if (process instanceof IProcess2) {
            return false;
        }
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerCoreService.class)) {
            ITestContainerCoreService testContainerService = (ITestContainerCoreService) GlobalServiceRegister.getDefault()
                    .getService(ITestContainerCoreService.class);
            if (testContainerService != null) {
                return testContainerService.isSparkStreaming(((IProcess2) process).getProperty().getItem());
            }
        }
        return false;
    }

    public static List<String> getTestData(IProcess process, String instanceName) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
            ITestContainerProviderService testContainerService = (ITestContainerProviderService) GlobalServiceRegister
                    .getDefault().getService(ITestContainerProviderService.class);
            if (testContainerService != null) {
                return testContainerService.getTestData(process, instanceName);
            }
        }
        return new ArrayList<String>();
    }

    public static Item getTestContainerBaseItem(Item item) {
        Item baseItem = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
            ITestContainerProviderService testContainerService = (ITestContainerProviderService) GlobalServiceRegister
                    .getDefault().getService(ITestContainerProviderService.class);
            if (testContainerService != null) {
                try {
                    baseItem = testContainerService.getParentJobItem(item);
                } catch (PersistenceException e) {
                    ExceptionHandler.process(e);
                }
            }
        }
        return baseItem;
    }

    public static IProcess getTestContainerBaseProcess(IProcess process) {
        IProcess baseProcess = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
            ITestContainerProviderService testContainerService = (ITestContainerProviderService) GlobalServiceRegister
                    .getDefault().getService(ITestContainerProviderService.class);
            if (testContainerService != null) {
                try {
                    baseProcess = testContainerService.getParentJobProcess(process);
                } catch (PersistenceException e) {
                    ExceptionHandler.process(e);
                }
            }
        }
        return baseProcess;
    }

    public static boolean isOptionChecked(Map<String, Object> argumentsMap, String key, Object... args) {
        if (key != null && argumentsMap != null) {
            final Object value = argumentsMap.get(key);
            if (value instanceof Boolean) {
                return (Boolean) value;
            } else if (value instanceof String) {
                return Boolean.parseBoolean((String) value);
            } else if (value instanceof Integer) {
                if (args != null && args.length > 0) {
                    if (key.equals(TalendProcessArgumentConstant.ARG_GENERATE_OPTION)) { // for generated option
                        boolean checked = true;
                        for (Object arg : args) {
                            if (!(arg instanceof Integer) || !BitwiseOptionUtils.containOption((Integer) value, (Integer) arg)) {
                                checked = false;
                                break;
                            }
                        }
                        return checked;
                    }
                }
            }
        }
        return false;
    }

    public static String getOptionValue(Map<String, Object> argumentsMap, String key, String defaultValue, Object... args) {
        if (key != null && argumentsMap != null) {
            final Object value = argumentsMap.get(key);
            if (value != null) {
                return value.toString();
            }
        }
        return defaultValue;
    }

    public static int getOptionValue(Map<String, Object> argumentsMap, String key, int defaultValue, Object... args) {
        if (key != null && argumentsMap != null) {
            final Object value = argumentsMap.get(key);
            if (value != null && value instanceof Integer) {
                return (Integer) value;
            }
        }
        return defaultValue;
    }

    public static List getOptionValue(Map<String, Object> argumentsMap, String key, List defaultValue, Object... args) {
        if (key != null && argumentsMap != null) {
            final Object value = argumentsMap.get(key);
            if (value != null && value instanceof List) {
                return (List) value;
            }
        }
        if (defaultValue == null) {
            return Collections.emptyList();
        }
        return defaultValue;
    }

    public static boolean isRequiredBeans(IProcess process) {
        boolean needBeans = false;
        if (process == null) {
            needBeans = true; // only check have the beans items.
        } else {
            if (process instanceof IProcess2) {
                Property property = ((IProcess2) process).getProperty();
                if (property != null && property.getItem() != null) { // same as isStandardJob in JavaProcessor
                    ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(property.getItem());
                    // route job
                    if (itemType != null && itemType.equals(ERepositoryObjectType.PROCESS_ROUTE)) {
                        needBeans = true;
                    }
                }
            }
        }

        if (needBeans && GlobalServiceRegister.getDefault().isServiceRegistered(IProxyRepositoryService.class)) {
            IProxyRepositoryService service = (IProxyRepositoryService) GlobalServiceRegister.getDefault().getService(
                    IProxyRepositoryService.class);
            ERepositoryObjectType beansType = ERepositoryObjectType.valueOf("BEANS"); //$NON-NLS-1$
            try {
                IProxyRepositoryFactory factory = service.getProxyRepositoryFactory();
                List<IRepositoryViewObject> all = factory.getAll(beansType);
                if (!all.isEmpty()) { // has bean
                    return true;
                }
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
        }
        return false;
    }

    public static boolean isRequiredPigUDFs(IProcess process) {
        boolean needPigUDF = false;
        if (process == null) {
            needPigUDF = true; // only check have the pigudf items.
        } else {
            if (process instanceof IProcess2) {
                Property property = ((IProcess2) process).getProperty();
                if (property != null) { // same as isStandardJob in JavaProcessor
                    // only for tPigMap?
                    for (INode n : process.getGeneratingNodes()) {
                        if (n.getComponent().getName().equals("tPigMap")) { //$NON-NLS-1$
                            needPigUDF = true;
                            break;
                        }
                    }
                }
            }
        }

        if (needPigUDF && GlobalServiceRegister.getDefault().isServiceRegistered(IProxyRepositoryService.class)) {
            IProxyRepositoryService service = (IProxyRepositoryService) GlobalServiceRegister.getDefault().getService(
                    IProxyRepositoryService.class);
            IProxyRepositoryFactory factory = service.getProxyRepositoryFactory();
            try {
                List<IRepositoryViewObject> pigUdfsObjects = factory.getAll(ERepositoryObjectType.PIG_UDF);
                if (!pigUdfsObjects.isEmpty()) {
                    /*
                     * FIXME, don't know need check the this api or not. seem it's not useful. so return true so far
                     * directly.
                     */
                    // Set<String> neededPigudf = process.getNeededPigudf();
                    // if (neededPigudf != null && !neededPigudf.isEmpty()) {
                    // return true;
                    // }

                    return true;
                }
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
        }

        return false;
    }

    public static boolean jarNeedsToContainContext() {
        return jarWithContext;
    }

    public static void setJarWithContext(boolean jarWithContext) {
        ProcessUtils.jarWithContext = jarWithContext;
    }

    /** Find the distribution where the generated jar rquired to have the context files inside **/
    public static boolean needsToHaveContextInsideJar(ProcessItem processItem) {
        EList<ElementParameterType> parameters = processItem.getProcess().getParameters().getElementParameter();
        for (ElementParameterType pt : parameters) {
            if (pt.getName().equals("DISTRIBUTION")) { //$NON-NLS-1$
                String value = pt.getValue();
                if ("MICROSOFT_HD_INSIGHT".equals(value) //$NON-NLS-1$
                        || "GOOGLE_CLOUD_DATAPROC".equals(value) //$NON-NLS-1$
                        || "CLOUDERA_ALTUS".equals(value)) { //$NON-NLS-1$
                    return true;
                }
            }
        }
        return false;
    }

}
