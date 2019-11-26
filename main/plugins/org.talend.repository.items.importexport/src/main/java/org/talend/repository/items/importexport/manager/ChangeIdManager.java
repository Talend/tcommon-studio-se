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
package org.talend.repository.items.importexport.manager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Priority;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IGenericElementParameter;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.process.ProcessUtils;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.utils.ReflectionUtils;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.joblet.model.JobletProcess;
import org.talend.repository.ProjectManager;
import org.talend.repository.items.importexport.handlers.model.ImportItem;
import org.talend.repository.items.importexport.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * created by cmeng on Dec 8, 2016
 * Detailled comment
 *
 */
public class ChangeIdManager {

    private Map<Project, RelationshipItemBuilder> project2RelationshipMap = new HashMap<Project, RelationshipItemBuilder>();

    private Map<String, List<ImportItem>> id2ImportItemsMap = new HashMap<String, List<ImportItem>>();

    private Map<String, Collection<String>> refIds2ItemIdsMap = new HashMap<String, Collection<String>>();

    private Map<String, String> oldId2NewIdMap = new HashMap<String, String>();

    private Map<Object, String> item2IdMap = new HashMap<>();

    private Set<String> idsNeed2CheckRefs = new HashSet<String>();

    private org.talend.core.model.general.Project currentProject;

    public void clear() {
        if (!project2RelationshipMap.isEmpty()) {
            for (RelationshipItemBuilder itemBuilder : project2RelationshipMap.values()) {
                itemBuilder.unloadRelations();
            }
            project2RelationshipMap.clear();
        }
        id2ImportItemsMap.clear();
        refIds2ItemIdsMap.clear();
        oldId2NewIdMap.clear();
        idsNeed2CheckRefs.clear();
        item2IdMap.clear();
        currentProject = null;
    }

    public void add(ImportItem importItem) {
        if (importItem.isImported()) {
            return;
        }

        prepareRelationshipItemBuilder(importItem.getItemProject());

        // update id-importItem map
        Property property = importItem.getProperty();
        if (property != null) {
            String id = property.getId();
            // record all importing id
            if (!oldId2NewIdMap.containsKey(id)) {
                oldId2NewIdMap.put(id, null);
            }

            // same id with different versions
            List<ImportItem> itemRecords = id2ImportItemsMap.get(id);
            if (itemRecords == null) {
                itemRecords = new ArrayList<ImportItem>();
                id2ImportItemsMap.put(id, itemRecords);
            }
            if (!itemRecords.contains(importItem)) {
                itemRecords.add(importItem);
                Item item = property.getItem();
                if (item instanceof ConnectionItem) {
                    idsNeed2CheckRefs.add(id);
                }
            }
        }
    }

    public void mapOldId2NewId(String oldId, String newId) throws Exception {
        oldId2NewIdMap.put(oldId, newId);
        List<ImportItem> importItems = id2ImportItemsMap.get(oldId);
        if (importItems != null) {
            id2ImportItemsMap.put(newId, importItems);
        }
    }

    public void changeIds(IProgressMonitor monitor) throws Exception {
        buildRefIds2ItemIdsMap();

        Map<String, Set<String>> changeIdMap = buildChangeIdMap();

        for (Map.Entry<String, Set<String>> entry : changeIdMap.entrySet()) {
            checkCancel(monitor);
            String oldEffectedId = entry.getKey();
            if (!oldId2NewIdMap.containsKey(oldEffectedId)) {
                // means didn't import this item
                continue;
            }
            String newEffectedId = oldId2NewIdMap.get(oldEffectedId);
            if (StringUtils.equals(oldEffectedId, newEffectedId)) {
                continue;
            }
            if (StringUtils.isBlank(newEffectedId)) {
                // means the id didn't be changed
                newEffectedId = oldEffectedId;
            }
            List<ImportItem> importItems = id2ImportItemsMap.get(oldEffectedId);
            ERepositoryObjectType repType = importItems.get(0).getRepositoryType();
            List<IRepositoryViewObject> repViewObjs = getAllVersion(newEffectedId, repType);
            if (repViewObjs != null && !repViewObjs.isEmpty()) {
                for (IRepositoryViewObject repViewObj : repViewObjs) {
                    checkCancel(monitor);
                    Map<String, String> old2NewMap = new HashMap<>();
                    for (String oldId : entry.getValue()) {
                        String newId = oldId2NewIdMap.get(oldId);
                        if (newId == null || StringUtils.equals(newId, oldId)) {
                            continue;
                        }
                        old2NewMap.put(oldId, newId);
                    }
                    if (old2NewMap.isEmpty()) {
                        continue;
                    }
                    Property property = repViewObj.getProperty();
                    monitor.subTask(Messages.getString("ChangeIdManager_ApplyingNewIds", property.getDisplayName()));
                    changeRelated(monitor, old2NewMap, property, getCurrentProject());
                    String version = property.getVersion();
                    for (ImportItem importItem : importItems) {
                        if (StringUtils.equals(version, importItem.getItemVersion())) {
                            // update property, it will be used in following steps
                            importItem.setProperty(property);
                            break;
                        }
                    }
                }
            }
        }
    }

    private Map<String, Set<String>> buildChangeIdMap() {
        Map<String, Set<String>> effectedIdsMap = new HashMap<>();
        Set<String> metadataItemOldIds = new HashSet<>();

        for (Map.Entry<String, String> entry : oldId2NewIdMap.entrySet()) {
            String oldId = entry.getKey();
            String newId = entry.getValue();
            if (newId == null || StringUtils.equals(newId, oldId)) {
                continue;
            }

            Set<String> effectedRelationIds = new HashSet<String>();
            Collection<String> itemIds = refIds2ItemIdsMap.get(oldId);
            if (itemIds != null && !itemIds.isEmpty()) {
                effectedRelationIds.addAll(itemIds);
            }

            List<Relation> effectedRelations = getRelations(oldId);
            for (Relation relation : effectedRelations) {
                effectedRelationIds.add(ProcessUtils.getPureItemId(relation.getId()));
            }

            List<ImportItem> importItems = id2ImportItemsMap.get(oldId);
            if (importItems != null && !importItems.isEmpty()) {
                ImportItem importItem = importItems.get(0);
                if (importItem != null) {
                    if (importItem.getItem() instanceof ConnectionItem) {
                        metadataItemOldIds.add(oldId);
                    }
                }
            }

            if (effectedRelationIds.isEmpty()) {
                continue;
            }

            Set<String> idSet = effectedIdsMap.get(oldId);
            if (idSet == null) {
                idSet = new HashSet<>();
                effectedIdsMap.put(oldId, idSet);
            }
            idSet.addAll(effectedRelationIds);
        }

        Map<String, Set<String>> changeIdMap = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : effectedIdsMap.entrySet()) {
            String oldId = entry.getKey();
            Set<String> effectedIds = entry.getValue();

            for (String effectedId : effectedIds) {
                Set<String> changeSet = changeIdMap.get(effectedId);
                if (changeSet == null) {
                    changeSet = new HashSet<>();
                    changeIdMap.put(effectedId, changeSet);
                }
                changeSet.add(oldId);
            }
        }

        // record all metadata connections, because some metadata doesn't record relationships, like hadoop cluster
        for (String metadataOldId : metadataItemOldIds) {
            Set<String> set = changeIdMap.get(metadataOldId);
            if (set == null) {
                set = new HashSet<>();
                changeIdMap.put(metadataOldId, set);
            }
            set.addAll(metadataItemOldIds);
        }
        return changeIdMap;
    }

    private List<IRepositoryViewObject> getAllVersion(String id, ERepositoryObjectType repType) throws Exception {
        List<IRepositoryViewObject> repViewObjs = null;
        if (repType != null) {
            repViewObjs = ProxyRepositoryFactory.getInstance().getAllVersion(getCurrentProject(), id, null, repType);
        } else {
            repViewObjs = ProxyRepositoryFactory.getInstance().getAllVersion(getCurrentProject(), id, true);
        }
        return repViewObjs;
    }

    private void buildRefIds2ItemIdsMap() throws Exception {
        for (String id : idsNeed2CheckRefs) {
            Collection<String> refIds = getRelatedIdsIfNeeded(id);
            if (refIds != null && !refIds.isEmpty()) {
                for (String refId : refIds) {
                    Collection<String> ids = refIds2ItemIdsMap.get(refId);
                    if (ids == null) {
                        ids = new HashSet<String>();
                        refIds2ItemIdsMap.put(refId, ids);
                    }
                    ids.add(id);
                }
            }
        }
    }

    private Collection<String> getRelatedIdsIfNeeded(String oldId) throws Exception {
        Collection<String> relatedIds = new HashSet<String>();
        if (!idsNeed2CheckRefs.contains(oldId)) {
            return relatedIds;
        }

        String givenId = oldId2NewIdMap.get(oldId);
        if (givenId == null) {
            givenId = oldId;
        }

        List<IRepositoryViewObject> givenObjs = getAllVersion(givenId, id2ImportItemsMap.get(givenId).get(0).getRepositoryType());

        if (givenObjs != null && !givenObjs.isEmpty()) {
            for (IRepositoryViewObject givenObj : givenObjs) {
                Item item = givenObj.getProperty().getItem();
                if (item instanceof ConnectionItem) {
                    String ctxId = ((ConnectionItem) item).getConnection().getContextId();
                    if (ctxId != null && !ctxId.isEmpty()) {
                        relatedIds.add(ctxId);
                    }
                } else {
                    throw new Exception("Unsupportted type when importing: " + item.toString());
                }
            }
        }

        return relatedIds;
    }

    private List<Relation> getRelations(String id) {
        List<Relation> relations = new ArrayList<Relation>();
        Collection<RelationshipItemBuilder> relationshipBuilders = project2RelationshipMap.values();
        for (RelationshipItemBuilder relationshipBuilder : relationshipBuilders) {
            List<Relation> list = relationshipBuilder.getItemsHaveRelationWith(id, null);
            if (list != null && !list.isEmpty()) {
                relations.addAll(list);
            }
        }
        return relations;
    }

    private void changeRelated(IProgressMonitor monitor, Map<String, String> old2NewMap, Property property,
            org.talend.core.model.general.Project project) throws Exception {
        checkCancel(monitor);
        Item item = property.getItem();
        boolean modified = false;
        if (item instanceof ProcessItem) {
            modified = changeRelatedProcess(monitor, old2NewMap, item);
        } else if (item instanceof JobletProcessItem) {
            modified = changeRelatedProcess(monitor, old2NewMap, item);
        } else if (item instanceof ConnectionItem) {
            modified = changeRelatedConnection(monitor, old2NewMap, (ConnectionItem) item);
        } else {
            throw new Exception("Unsupported id change: id[" + property.getId() + "], name[" + property.getLabel() + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        if (modified) {
            ProxyRepositoryFactory.getInstance().save(project, item);
            // RelationshipItemBuilder.getInstance().addOrUpdateItem(property.getItem());
        }
    }

    private boolean changeRelatedConnection(IProgressMonitor monitor, Map<String, String> old2NewMap, ConnectionItem item)
            throws Exception {
        return changeRelatedObject(monitor, old2NewMap, item.getConnection(), new Stack<>(), new HashMap<>());
    }

    private boolean changeRelatedObject(IProgressMonitor monitor, Map<String, String> old2NewMap, Object conn,
            Stack<Object> visitStack, Map<String, List<Object>> changedMap) throws Exception {
        checkCancel(monitor);
        if (conn == null) {
            return false;
        }
        boolean isEmpty = true;
        for (Map.Entry<String, String> entry : old2NewMap.entrySet()) {
            if (entry.getValue() != null) {
                isEmpty = false;
                break;
            }
        }
        if (isEmpty) {
            return false;
        }

        if (conn instanceof Collection || conn instanceof Map) {
            throw new Exception("Bad usage of function, can't be Collection or Map here!");
        }
        boolean modified = false;
        Collection<Field> fields = ReflectionUtils.getAllDeclaredFields(conn.getClass());
        for (Field field : fields) {
            if (field.isEnumConstant() || field.getType().isPrimitive()) {
                continue;
            }
            field.setAccessible(true);
            Object obj = field.get(conn);
            if (obj != null) {
                if (visitStack.contains(obj)) {
                    continue;
                }
                if (obj.getClass() == Object.class) {
                    continue;
                } else if (isBasicType(obj.getClass())) {
                    continue;
                } else if (field.getType() == String.class) {
                    if (Modifier.isFinal(field.getModifiers())) {
                        continue;
                    }
                    for (Map.Entry<String, String> entry : old2NewMap.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        if (value == null || StringUtils.equals(key, value)) {
                            continue;
                        }
                        // update latest value
                        obj = field.get(conn);
                        if (obj == null) {
                            break;
                        }
                        String newValue = doReplace(obj.toString(), key, value);
                        if (!StringUtils.equals(obj.toString(), newValue)) {
                            field.set(conn, newValue);
                        }
                        modified = true;
                    }
                } else {
                    try {
                        for (Map.Entry<String, String> entry : old2NewMap.entrySet()) {
                            String key = entry.getKey();
                            String value = entry.getValue();
                            if (value == null || StringUtils.equals(key, value)) {
                                continue;
                            }
                            changeValue(monitor, obj, key, value, visitStack, changedMap);
                            modified = true;
                        }
                    } catch (InterruptedException e) {
                        throw e;
                    } catch (UnsupportedOperationException e) {
                        if (CommonsPlugin.isDebugMode()) {
                            ExceptionHandler.process(e);
                        }
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                }
            }
        }
        return modified;
    }

    private boolean isBasicType(Class clz) {
        List<Class> basicTypes = Arrays.asList(Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class,
                Float.class, Double.class, Void.class);
        return basicTypes.contains(clz);
    }

    private void setStringValue(Object model, Field field, String newValue) throws Exception {
        String fieldName = field.getName();
        String setMethodName = "set" + ("" + fieldName.charAt(0)).toUpperCase()
                + (0 < fieldName.length() ? fieldName.substring(1) : "");
        boolean hasSetMethod = false;
        try {
            Method method = model.getClass().getMethod(setMethodName, String.class);
            hasSetMethod = (method != null);
        } catch (Exception e) {
            if (CommonsPlugin.isDebugMode()) {
                ExceptionHandler.process(e);
            }
        }
        if (hasSetMethod) {
            ReflectionUtils.invokeMethod(model, setMethodName, new Object[] { newValue }, String.class);
        } else {
            field.set(model, newValue);
        }
    }

    private boolean changeRelatedProcess(IProgressMonitor monitor, Map<String, String> old2NewMap, Item item) throws Exception {
        checkCancel(monitor);
        ProcessType processType = null;
        if (item instanceof ProcessItem) {
            ProcessItem processItem = (ProcessItem) item;
            processType = processItem.getProcess();
        } else if (item instanceof JobletProcessItem) {
            JobletProcessItem processItem = (JobletProcessItem) item;
            processType = processItem.getJobletProcess();
        } else {
            throw new Exception("Unhandled process type: id[" + item.getProperty().getId() + "], name[" //$NON-NLS-1$ //$NON-NLS-2$
                    + item.getProperty().getLabel() + "]"); //$NON-NLS-1$
        }

        boolean modified = changeRelatedObject(monitor, old2NewMap, processType, new Stack<>(), new HashMap<>());

        if (modified) {
            /**
             * designerCoreService must not be null
             */
            IDesignerCoreService designerCoreService = (IDesignerCoreService) GlobalServiceRegister.getDefault()
                    .getService(IDesignerCoreService.class);

            IProcess process = designerCoreService.getProcessFromItem(item);
            if (process == null) {
                throw new Exception("Can't get process of item: id[" + item.getProperty().getId() + "], name[" //$NON-NLS-1$ //$NON-NLS-2$
                        + item.getProperty().getLabel() + "]"); //$NON-NLS-1$
            }
            if (process instanceof IProcess2) {
                processType = ((IProcess2) process).saveXmlFile();
                if (item instanceof ProcessItem) {
                    ((ProcessItem) item).setProcess(processType);
                } else if (item instanceof JobletProcessItem) {
                    ((JobletProcessItem) item).setJobletProcess((JobletProcess) processType);
                } else {
                    throw new Exception("Unhandled process type: id[" + item.getProperty().getId() + "], name[" //$NON-NLS-1$ //$NON-NLS-2$
                            + item.getProperty().getLabel() + "]"); //$NON-NLS-1$
                }
            } else {
                throw new Exception("Unhandled process type: id[" + item.getProperty().getId() + "], name[" //$NON-NLS-1$ //$NON-NLS-2$
                        + item.getProperty().getLabel() + "]"); //$NON-NLS-1$
            }
        }
        return modified;

    }

    private void changeValue(IProgressMonitor monitor, Object aim, String fromValue, String toValue, Stack<Object> visitStack,
            Map<String, List<Object>> changedMap) throws Exception {
        checkCancel(monitor);
        if (toValue == null) {
            /**
             * toValue must be a string, do you need ""?
             */
            throw new IllegalArgumentException();
        }
        if (aim == null) {
            return;
        } else if (visitStack.contains(aim)) {
            return;
        } else {
            visitStack.push(aim);
        }
        List<Object> changedListForKey = changedMap.get(fromValue);
        if (changedListForKey == null) {
            changedListForKey = new LinkedList<>();
            changedMap.put(fromValue, changedListForKey);
        }
        try {
            if (changedListForKey.contains(aim)) {
                return;
            }
            if (aim instanceof IElementParameter) {
                if (aim instanceof IGenericElementParameter) {
                    ((IGenericElementParameter) aim).setAskPropagate(Boolean.TRUE);
                }
                Map<String, String> old2NewMap = new HashMap<>();
                old2NewMap.put(fromValue, toValue);
                changeRelatedObject(monitor, old2NewMap, aim, visitStack, changedMap);
            } else if (aim instanceof EObject) {
                Map<String, String> old2NewMap = new HashMap<>();
                old2NewMap.put(fromValue, toValue);
                changeRelatedObject(monitor, old2NewMap, aim, visitStack, changedMap);
            } else if (aim instanceof List) {
                List aimList = (List) aim;
                for (int i = 0; i < aimList.size(); i++) {
                    Object obj = aimList.get(i);
                    if (obj instanceof String) {
                        aimList.set(i, doReplace(obj.toString(), fromValue, toValue));
                    } else {
                        changeValue(monitor, obj, fromValue, toValue, visitStack, changedMap);
                    }
                }
            } else if (aim instanceof Map) {
                Map aimMap = (Map) aim;
                if (aimMap != null && !aimMap.isEmpty()) {
                    Object key1 = aimMap.keySet().iterator().next();
                    if (key1 instanceof String) {
                        // maybe need to consider the order like LinkedHashMap
                        Object value = aimMap.get(fromValue);
                        if (value != null) {
                            aimMap.remove(fromValue);
                            aimMap.put(toValue, value);
                        }
                    }
                    Iterator<Map.Entry> iter = aimMap.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = iter.next();
                        Object value = entry.getValue();
                        if (value instanceof String) {
                            entry.setValue(doReplace(value.toString(), fromValue, toValue));
                        } else {
                            changeValue(monitor, value, fromValue, toValue, visitStack, changedMap);
                        }
                    }
                }
            } else if (aim instanceof Map.Entry) {
                Map.Entry aimEntry = (Entry) aim;
                Object value = aimEntry.getValue();
                if (value instanceof String) {
                    aimEntry.setValue(doReplace((String) value, fromValue, toValue));
                } else {
                    changeValue(monitor, value, fromValue, toValue, visitStack, changedMap);
                }

            } else if (aim instanceof Iterable) {
                Iterator iter = ((Iterable) aim).iterator();
                while (iter.hasNext()) {
                    // maybe not good
                    changeValue(monitor, iter.next(), fromValue, toValue, visitStack, changedMap);
                }
                ExceptionHandler.process(new Exception("Unchecked id change type: " + aim.getClass().toString()), Priority.WARN); //$NON-NLS-1$
            } else if (aim instanceof Object[]) {
                Object[] objs = (Object[]) aim;
                for (Object obj : objs) {
                    changeValue(monitor, obj, fromValue, toValue, visitStack, changedMap);
                }
            }
        } finally {
            visitStack.pop();
            changedListForKey.add(aim);
        }
    }

    public void updateTestContainerParentId(IProgressMonitor monitor, Item testContainerItem) throws Exception {
        changeRelatedObject(monitor, this.oldId2NewIdMap, testContainerItem, new Stack<>(), new HashMap<>());
    }

    private String doReplace(String aimString, String from, String to) {
        return aimString.replaceAll("\\b" + from + "\\b", to); //$NON-NLS-1$//$NON-NLS-2$
    }

    private void prepareRelationshipItemBuilder(Project project) {
        RelationshipItemBuilder itemBuilder = project2RelationshipMap.get(project);
        if (itemBuilder == null) {
            IProxyRepositoryFactory repFactory = ProxyRepositoryFactory.getInstance();
            itemBuilder = RelationshipItemBuilder.createInstance(repFactory, new org.talend.core.model.general.Project(project));
            project2RelationshipMap.put(project, itemBuilder);
        }
    }

    private org.talend.core.model.general.Project getCurrentProject() {
        if (currentProject == null) {
            currentProject = ProjectManager.getInstance().getCurrentProject();
        }
        return currentProject;
    }

    public Map<Object, String> getItemToIdMap() {
        return item2IdMap;
    }

    private void checkCancel(IProgressMonitor monitor) throws Exception {
        if (monitor == null) {
            return;
        }
        if (monitor.isCanceled() || Thread.currentThread().isInterrupted()) {
            throw new InterruptedException(Messages.getString("IProgressMonitor_UserCancelled"));
        }
    }

}
