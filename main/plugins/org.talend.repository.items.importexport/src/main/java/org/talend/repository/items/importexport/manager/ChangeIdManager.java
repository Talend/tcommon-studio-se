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
package org.talend.repository.items.importexport.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Priority;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IGenericElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
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
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.joblet.model.JobletProcess;
import org.talend.repository.ProjectManager;
import org.talend.repository.items.importexport.handlers.model.ImportItem;
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
        currentProject = null;
    }

    public void add(ImportItem importItem) {

        prepareRelationshipItemBuilder(importItem.getItemProject());

        // update id-importItem map
        Property property = importItem.getProperty();
        if (property != null) {
            String id = property.getId();
            // record all importing id
            oldId2NewIdMap.put(id, null);
            List<ImportItem> itemRecords = id2ImportItemsMap.get(id);
            if (itemRecords == null) {
                itemRecords = new ArrayList<ImportItem>();
                id2ImportItemsMap.put(id, itemRecords);
            }
            itemRecords.add(importItem);

            Item item = property.getItem();
            if (item instanceof ConnectionItem) {
                idsNeed2CheckRefs.add(id);
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

    public void changeIds() throws Exception {
        buildRefIds2ItemIdsMap();
        for (Map.Entry<String, String> entry : oldId2NewIdMap.entrySet()) {
            changeId(entry.getValue(), entry.getKey());
        }
    }

    private void changeId(String newId, String oldId) throws Exception {

        if (newId == null || StringUtils.equals(newId, oldId)) {
            return;
        }

        Set<String> relationIds = new HashSet<String>();
        Collection<String> itemIds = refIds2ItemIdsMap.get(oldId);
        if (itemIds != null && !itemIds.isEmpty()) {
            relationIds.addAll(itemIds);
        }

        List<Relation> relations = getRelations(oldId);
        for (Relation relation : relations) {
            relationIds.add(relation.getId());
        }

        if (relationIds.isEmpty()) {
            return;
        }

        Set<String> changedIds = new HashSet<String>();
        for (String relationId : relationIds) {
            String id = relationId;
            if (!oldId2NewIdMap.containsKey(id)) {
                // means didn't import this item
                continue;
            }

            if (changedIds.contains(id)) {
                continue;
            } else {
                changedIds.add(id);
            }

            List<ImportItem> importItems = id2ImportItemsMap.get(id);
            ERepositoryObjectType repType = importItems.get(0).getRepositoryType();
            String newRelatedId = oldId2NewIdMap.get(id);
            if (newRelatedId == null) {
                // means the id didn't be changed
                newRelatedId = id;
            }
            List<IRepositoryViewObject> repViewObjs = getAllVersion(newRelatedId, repType);
            if (repViewObjs != null && !repViewObjs.isEmpty()) {
                for (IRepositoryViewObject repViewObj : repViewObjs) {
                    Property property = repViewObj.getProperty();
                    changeRelated(newId, oldId, property, getCurrentProject());
                }
            }
        }
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

    private void changeRelated(String newId, String oldId, Property property, org.talend.core.model.general.Project project)
            throws Exception {
        Item item = property.getItem();
        boolean modified = false;
        if (item instanceof ProcessItem) {
            modified = changeRelatedProcess(newId, oldId, item);
        } else if (item instanceof JobletProcessItem) {
            modified = changeRelatedProcess(newId, oldId, item);
        } else if (item instanceof ConnectionItem) {
            modified = changeRelatedConnection(newId, oldId, (ConnectionItem) item);
        } else {
            throw new Exception("Unsupported id change: id[" + property.getId() + "], name[" + property.getLabel() + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        if (modified) {
            ProxyRepositoryFactory.getInstance().save(project, item);
            RelationshipItemBuilder.getInstance().addOrUpdateItem(property.getItem());
        }
    }

    private boolean changeRelatedConnection(String newId, String oldId, ConnectionItem item) throws Exception {
        boolean modified = false;
        Connection conn = item.getConnection();
        String ctxId = conn.getContextId();
        if (StringUtils.equals(oldId, ctxId)) {
            conn.setContextId(newId);
            modified = true;
        } else {
            throw new Exception("Unhandled case for import: " + item.toString()); //$NON-NLS-1$
        }
        return modified;
    }

    private boolean changeRelatedProcess(String newId, String oldId, Item item) throws Exception {
        boolean modified = false;

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

        /**
         * 1. change context
         */
        // List contexts = item.getProcess().getContext();
        // if (contexts != null && !contexts.isEmpty()) {
        // changeValue(contexts, oldId, newId);
        // modified = true;
        // }
        IContextManager contextManager = process.getContextManager();
        if (contextManager != null) {
            changeValue(contextManager.getListContext(), oldId, newId);
            modified = true;
        }

        /**
         * 2. change elementParameters
         */
        changeValue(process.getElementParameters(), oldId, newId);
        modified = true;

        /**
         * 3. change the nodes like tRunjob, tMysql
         */
        List<? extends INode> nodes = process.getGraphicalNodes();
        if (nodes != null && !nodes.isEmpty()) {
            Iterator<? extends INode> nodeIter = nodes.iterator();
            while (nodeIter.hasNext()) {
                INode node = nodeIter.next();
                if (node != null) {
                    changeParamValueOfNode(node, oldId, newId);
                }
            }
            modified = true;
        }

        if (modified) {
            if (process instanceof IProcess2) {
                ProcessType processType = ((IProcess2) process).saveXmlFile();
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

    private boolean changeParamValueOfNode(INode node, String fromValue, String toValue) throws Exception {
        boolean changed = false;
        List<? extends IElementParameter> elementParameters = node.getElementParameters();
        if (elementParameters != null && !elementParameters.isEmpty()) {
            changeValue(elementParameters, fromValue, toValue);
            changed = true;
        }
        return changed;
    }

    private void changeValue(Object aim, String fromValue, String toValue) throws Exception {
        if (aim == null) {
            return;
        }
        if (aim instanceof IElementParameter) {
            if (aim instanceof IGenericElementParameter) {
                ((IGenericElementParameter) aim).setAskPropagate(Boolean.TRUE);
            }
            IElementParameter elemParameter = (IElementParameter) aim;
            Object elementParamValue = elemParameter.getValue();
            if (elementParamValue != null) {
                if (elementParamValue instanceof String) {
                    elemParameter.setValue(doReplace(elementParamValue.toString(), fromValue, toValue));
                } else {
                    changeValue(elementParamValue, fromValue, toValue);
                }
            }
            Map<String, IElementParameter> childParameters = elemParameter.getChildParameters();
            if (childParameters != null && !childParameters.isEmpty()) {
                changeValue(childParameters, fromValue, toValue);
            }
        } else if (aim instanceof ContextType) {
            changeValue(((ContextType) aim).getContextParameter(), fromValue, toValue);
        } else if (aim instanceof ContextParameterType) {
            ContextParameterType ctxParamType = (ContextParameterType) aim;
            String comment = ctxParamType.getComment();
            if (comment != null) {
                ctxParamType.setComment(doReplace(comment, fromValue, toValue));
            }
            String name = ctxParamType.getName();
            if (name != null) {
                ctxParamType.setName(doReplace(name, fromValue, toValue));
            }
            String prompt = ctxParamType.getPrompt();
            if (prompt != null) {
                ctxParamType.setPrompt(doReplace(prompt, fromValue, toValue));
            }

            // String rawValue = ctxParamType.getRawValue();
            // if (rawValue != null) {
            // ctxParamType.setRawValue(doReplace(rawValue, fromValue, toValue));
            // }

            String repCtxId = ctxParamType.getRepositoryContextId();
            if (repCtxId != null) {
                ctxParamType.setRepositoryContextId(doReplace(repCtxId, fromValue, toValue));
            }

            // String type = ctxParamType.getType();
            // if (type != null) {
            // ctxParamType.setType(doReplace(type, fromValue, toValue));
            // }

            String value = ctxParamType.getValue();
            if (value != null) {
                ctxParamType.setValue(doReplace(value, fromValue, toValue));
            }
        } else if (aim instanceof IContext) {
            changeValue(((IContext) aim).getContextParameterList(), fromValue, toValue);
        } else if (aim instanceof IContextParameter) {
            IContextParameter contextParameter = (IContextParameter) aim;
            String comment = contextParameter.getComment();
            if (comment != null) {
                contextParameter.setComment(doReplace(comment, fromValue, toValue));
            }
            String name = contextParameter.getName();
            if (name != null) {
                contextParameter.setName(doReplace(name, fromValue, toValue));
            }
            String prompt = contextParameter.getPrompt();
            if (prompt != null) {
                contextParameter.setPrompt(doReplace(prompt, fromValue, toValue));
            }
            String scriptCode = contextParameter.getScriptCode();
            if (scriptCode != null) {
                contextParameter.setScriptCode(doReplace(scriptCode, fromValue, toValue));
            }
            String source = contextParameter.getSource();
            if (source != null) {
                contextParameter.setSource(doReplace(source, fromValue, toValue));
            }

            // // reset type will clear the value
            // String type = contextParameter.getType();
            // if (type != null) {
            // contextParameter.setType(doReplace(type, fromValue, toValue));
            // }

            String value = contextParameter.getValue();
            if (value != null) {
                contextParameter.setValue(doReplace(value, fromValue, toValue));
            }
            String[] values = contextParameter.getValueList();
            if (values != null && 0 < values.length) {
                List<String> list = Arrays.asList(values);
                for (int i = 0; i < list.size(); i++) {
                    list.set(i, doReplace(list.get(i), fromValue, toValue));
                }
                contextParameter.setValueList(list.toArray(values));
            }
        } else if (aim instanceof String) {
            throw new Exception("Uncatched value type case!"); //$NON-NLS-1$
        } else if (aim instanceof List) {
            List aimList = (List) aim;
            for (int i = 0; i < aimList.size(); i++) {
                Object obj = aimList.get(i);
                if (obj instanceof String) {
                    aimList.set(i, doReplace(obj.toString(), fromValue, toValue));
                } else {
                    changeValue(obj, fromValue, toValue);
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
                        changeValue(value, fromValue, toValue);
                    }
                }
            }
        } else if (aim instanceof Iterable) {
            Iterator iter = ((Iterable) aim).iterator();
            while (iter.hasNext()) {
                // maybe not good
                changeValue(iter.next(), fromValue, toValue);
            }
            ExceptionHandler.process(new Exception("Unchecked id change type: " + aim.getClass().toString()), Priority.WARN); //$NON-NLS-1$
        } else if (aim instanceof Object[]) {
            Object[] objs = (Object[]) aim;
            for (Object obj : objs) {
                changeValue(obj, fromValue, toValue);
            }
        } else {
            // some types no need to be changed like Boolean
            // throw new Exception("Unhandled type: " + aim.getClass().getName()); //$NON-NLS-1$
        }
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
}
