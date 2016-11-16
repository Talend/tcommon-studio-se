// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Priority;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.TalendNature;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.ProjectReference;
import org.talend.core.model.properties.Property;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.repository.ProjectManager;
import org.talend.repository.items.importexport.handlers.HandlerUtil;
import org.talend.repository.items.importexport.handlers.imports.IImportItemsHandler;
import org.talend.repository.items.importexport.handlers.imports.ImportBasicHandler;
import org.talend.repository.items.importexport.handlers.model.ImportItem;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryConstants;

/**
 * created by cmeng on Nov 4, 2016 Detailled comment
 *
 */
public class ChangeIdManager {

    private Map<Project, RelationshipItemBuilder> project2RelationshipMap = new HashMap<Project, RelationshipItemBuilder>();

    private Map<String, List<ImportItem>> id2ImportItemsMap = new HashMap<String, List<ImportItem>>();

    private Map<String, String> newId2OldIdMap = new HashMap<String, String>();

    private Map<Project, IProject> project2EmfProject = new HashMap<Project, IProject>();

    /**
     * just use one importItem to get the project path
     */
    private Map<Project, ImportItem> project2OneImportItemMap = new HashMap<Project, ImportItem>();

    private ResourcesManager resManager;

    public void clear() {
        if (!project2RelationshipMap.isEmpty()) {
            for (RelationshipItemBuilder itemBuilder : project2RelationshipMap.values()) {
                itemBuilder.unloadRelations();
            }
            project2RelationshipMap.clear();
        }
        id2ImportItemsMap.clear();
        newId2OldIdMap.clear();
        if (!project2EmfProject.isEmpty()) {
            for (IProject project : project2EmfProject.values()) {
                try {
                    project.delete(false, false, new NullProgressMonitor());
                } catch (CoreException e) {
                    ExceptionHandler.process(e);
                }
            }
        }
        project2OneImportItemMap.clear();
        resManager = null;
    }

    public void add(ImportItem importItem) {
        // update project-relationshipBuilder map
        Project project = importItem.getItemProject();
        if (!project2OneImportItemMap.containsKey(project)) {
            project2OneImportItemMap.put(project, importItem);
        }

        // update id-importItem map
        Property property = importItem.getProperty();
        if (property != null) {
            String id = property.getId();
            List<ImportItem> itemRecords = id2ImportItemsMap.get(id);
            if (itemRecords == null) {
                itemRecords = new ArrayList<ImportItem>();
                id2ImportItemsMap.put(id, itemRecords);
            }
            itemRecords.add(importItem);
        }
    }

    public void mapNewId2OldId(String newId, String oldId) throws Exception {
        String existOldId = newId2OldIdMap.get(newId);
        if (existOldId != null && !existOldId.equals(oldId)) {
            throw new Exception(
                    "bad new Id: " + newId + ", it is already linked to " + existOldId + ", but still try to link " + oldId); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        newId2OldIdMap.put(newId, oldId);
        List<ImportItem> importItems = id2ImportItemsMap.get(oldId);
        if (importItems != null) {
            id2ImportItemsMap.put(newId, importItems);
        }
    }

    public void changeId(String newId) throws Exception {
        String oldId = newId2OldIdMap.get(newId);
        if (StringUtils.isEmpty(oldId)) {
            return;
            // throw new Exception("Can't find the original id for new id: " + newId); //$NON-NLS-1$
        }

        List<ImportItem> importItems = id2ImportItemsMap.get(newId);
        for (ImportItem importItem : importItems) {
            try {
                changeId(newId, oldId, importItem);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
    }

    private void changeId(String newId, String oldId, ImportItem importItem) throws Exception {
        resolveItem(importItem);
        Project project = importItem.getItemProject();
        if (project == null) {
            throw new Exception("Can't get the project of importItem: " + importItem.toString()); //$NON-NLS-1$
        }
        RelationshipItemBuilder relationshipBuilder = project2RelationshipMap.get(project);
        if (relationshipBuilder == null) {
            throw new Exception("Can't get the relationshipItemBuilder of project: " + project.getLabel()); //$NON-NLS-1$
        }
        Collection<Relation> relationSet = new HashSet<Relation>();
        List<Relation> tmpRelationSet = relationshipBuilder.getItemsHaveRelationWith(oldId,
                importItem.getProperty().getVersion());
        if (tmpRelationSet != null) {
            relationSet.addAll(tmpRelationSet);
        }

        IProxyRepositoryFactory proxyRepFactory = ProxyRepositoryFactory.getInstance();
        org.talend.core.model.general.Project currentProject = ProjectManager.getInstance().getCurrentProject();
        for (Relation relation : relationSet) {
            String relatedId = relation.getId();
            List<ImportItem> relatedImportItems = id2ImportItemsMap.get(relatedId);
            if (relatedImportItems == null || relatedImportItems.isEmpty()) {
                // means not in the import list
                continue;
            }
            // baseItem only record specified version, means no version string like 'Latest'
            String relatedVersion = relation.getVersion();
            boolean changeAllRelated = false;
            if (StringUtils.isEmpty(relatedVersion)) {
                changeAllRelated = true;
            }
            for (ImportItem relatedImportItem : relatedImportItems) {
                boolean changeThisItem = false;
                if (changeAllRelated) {
                    changeThisItem = true;
                } else {
                    changeThisItem = StringUtils.equals(relatedVersion, relatedImportItem.getProperty().getVersion());
                }
                if (changeThisItem) {
                    try {
                        resolveItem(relatedImportItem);
                        Property property = relatedImportItem.getProperty();
                        org.talend.core.model.general.Project propertyProject = null;
                        boolean isImported = relatedImportItem.isImported();
                        if (isImported) {
                            // just use the id of property, since it has been changed to the new id during import
                            IRepositoryViewObject repViewObject = proxyRepFactory.getSpecificVersion(currentProject,
                                    property.getId(), property.getVersion(), true);
                            if (repViewObject == null) {
                                throw new Exception("Can't get the imported item: id[" + property.getId() + "], name[" //$NON-NLS-1$ //$NON-NLS-2$
                                        + property.getLabel() + "]"); //$NON-NLS-1$
                            }
                            property = repViewObject.getProperty();
                            propertyProject = currentProject;
                        } else {
                            propertyProject = relationshipBuilder.getAimProject();
                        }
                        changeRelated(newId, oldId, property, propertyProject);
                        if (isImported) {
                            RelationshipItemBuilder.getInstance().addOrUpdateItem(property.getItem());
                        }
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                }
            }
        }
    }

    private void changeRelated(String newId, String oldId, Property property, org.talend.core.model.general.Project project)
            throws Exception {
        Item item = property.getItem();
        if (item instanceof ProcessItem) {
            boolean modified = changeRelatedProcess(newId, oldId, (ProcessItem) item);
            if (modified) {
                ProxyRepositoryFactory.getInstance().save(project, item);
            }
        } else {
            throw new Exception("Unsupported id change: id[" + property.getId() + "], name[" + property.getLabel() + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
    }

    private boolean changeRelatedProcess(String newId, String oldId, ProcessItem item) throws Exception {
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
                item.setProcess(processType);
            } else {
                throw new Exception("Unhandled process type: id[" + item.getProperty().getId() + "], name[" //$NON-NLS-1$ //$NON-NLS-2$
                        + item.getProperty().getLabel() + "]"); //$NON-NLS-1$
            }
        }
        return modified;

    }

    private void prepareEmfProject(Project project, IPath projectFilePath) {
        IProject emfProject = project2EmfProject.get(project);
        if (emfProject == null) {
            String uniqueName = "IMPORT_" + String.valueOf(Calendar.getInstance().getTime().getTime()); //$NON-NLS-1$
            project.setLabel(uniqueName);
            project.setTechnicalLabel(uniqueName);
            try {
                emfProject = ResourcesPlugin.getWorkspace().getRoot().getProject(uniqueName);
                // IPath projectFilePath = HandlerUtil.getValidProjectFilePath(resManager, importItem.getPath());
                // projectFilePath = projectFilePath.removeLastSegments(1);
                createNewEmptyProject(project, emfProject, projectFilePath.toPortableString());
                project2EmfProject.put(project, emfProject);
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
        }

    }

    private static void createNewEmptyProject(Project project, IProject eclipseProject, String projectPath)
            throws PersistenceException {
        createNewEmptyProject(new NullProgressMonitor(), eclipseProject, projectPath, project.getTechnicalLabel(),
                new String[] { TalendNature.ID });
    }

    private static void createNewEmptyProject(IProgressMonitor progressMonitor, IProject eclipseProject, String projectPath,
            String description, String[] natureIds) throws PersistenceException {
        IProgressMonitor monitor = progressMonitor;
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        try {
            if (eclipseProject.exists()) {
                eclipseProject.delete(false, true, monitor);
            }
            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            IProjectDescription desc = workspace.newProjectDescription(description);
            desc.setLocation(new Path(projectPath));
            if (natureIds != null && natureIds.length != 0) {
                desc.setNatureIds(natureIds);
            }
            eclipseProject.create(desc, monitor);
        } catch (CoreException e) {
            throw new PersistenceException(e);
        }

        try {
            eclipseProject.open(monitor);
            eclipseProject.refreshLocal(IResource.DEPTH_INFINITE, monitor);
            IFolder tempFolder = eclipseProject.getFolder(RepositoryConstants.TEMP_DIRECTORY);
            if (!tempFolder.exists()) {
                tempFolder.create(true, true, monitor);
            }
        } catch (CoreException e) {
            throw new PersistenceException(e);
        }
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

    private void resolveItem(ImportItem importItem) throws Exception {
        if (!importItem.isResolved()) {
            IImportItemsHandler importHandler = importItem.getImportHandler();
            if (importHandler != null) {
                if (importHandler instanceof ImportBasicHandler) {
                    ((ImportBasicHandler) importHandler).resolveItem(resManager, importItem);
                } else {
                    // must throw exception to interrupt the execution for current item, otherwise we'll import an empty
                    // item
                    throw new Exception("Unhandled importItemHandler type: " + importHandler.getClass().getName()); //$NON-NLS-1$
                }
            }
        }
    }

    private String doReplace(String aimString, String from, String to) {
        return aimString.replaceAll("\\b" + from + "\\b", to); //$NON-NLS-1$//$NON-NLS-2$
    }

    public void setResourceManager(ResourcesManager resManager) {
        this.resManager = resManager;
    }

    public ResourcesManager getResourceManager() {
        return this.resManager;
    }

    public void prepare(ResourcesManager resManager) {
        setResourceManager(resManager);
        prepareEmfProjects();
        prepareRelationshipItemBuilders();
    }

    private void prepareEmfProjects() {
        if (project2EmfProject.isEmpty()) {
            for (Map.Entry<Project, ImportItem> entry : project2OneImportItemMap.entrySet()) {
                Project project = entry.getKey();
                ImportItem importItem = entry.getValue();
                project.setReference(true);
                IPath projectFilePath = HandlerUtil.getValidProjectFilePath(resManager, importItem.getPath());
                projectFilePath = projectFilePath.removeLastSegments(1);
                prepareEmfProject(project, projectFilePath);
            }

            // make all as reference projects since we don't know the relationship between the projects
            Set<Project> projects = project2OneImportItemMap.keySet();
            if (1 < projects.size()) {
                for (Project project : projects) {
                    EList refProjects = project.getReferencedProjects();
                    refProjects.clear();
                    for (Project refProject : projects) {
                        if (project == refProject) {
                            continue;
                        }
                        ProjectReference projectReference = org.talend.core.model.properties.PropertiesFactory.eINSTANCE
                                .createProjectReference();
                        projectReference.setBranch(null);
                        projectReference.setProject(project);
                        projectReference.setReferencedBranch(null);
                        projectReference.setReferencedProject(refProject);
                        refProjects.add(projectReference);
                    }
                }
            }
        }
    }

    private void prepareRelationshipItemBuilders() {
        if (project2RelationshipMap.isEmpty()) {
            for (Project project : project2OneImportItemMap.keySet()) {
                prepareRelationshipItemBuilder(project);
            }
        }
    }

    private void prepareRelationshipItemBuilder(Project project) {
        RelationshipItemBuilder itemBuilder = project2RelationshipMap.get(project);
        if (itemBuilder == null) {
            IProxyRepositoryFactory repFactory = ProxyRepositoryFactory.getInstance();
            itemBuilder = RelationshipItemBuilder.createInstance(repFactory, new org.talend.core.model.general.Project(project));
            project2RelationshipMap.put(project, itemBuilder);
        }
    }
}
