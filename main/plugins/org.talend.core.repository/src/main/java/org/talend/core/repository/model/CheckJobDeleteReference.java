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
package org.talend.core.repository.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.map.MultiKeyMap;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.provider.ICheckDeleteItemReference;
import org.talend.core.repository.ui.actions.DeleteActionCache;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * 
 * DOC hcyi class global comment. Detailled comment
 */
public class CheckJobDeleteReference extends AbstractCheckDeleteItemReference implements ICheckDeleteItemReference {

    private static final String PROCESS_TYPE_PROCESS = "PROCESS_TYPE_PROCESS"; //$NON-NLS-1$

    private static final String USE_DYNAMIC_JOB = "USE_DYNAMIC_JOB"; //$NON-NLS-1$

    // almost move from the method checkRepositoryNodeFromProcess of DeleteAction class.
    @Override
    public Set<ItemReferenceBean> checkItemReferenceBeans(IProxyRepositoryFactory factory, DeleteActionCache deleteActionCache,
            IRepositoryViewObject object) {
        Item nodeItem = object.getProperty().getItem();
        boolean needCheckJobIfUsedInProcess = false;
        if (nodeItem instanceof ProcessItem) {
            needCheckJobIfUsedInProcess = true;
        }
        Set<ItemReferenceBean> list = new HashSet<ItemReferenceBean>();
        if (needCheckJobIfUsedInProcess) {
            Property property = object.getProperty();
            if (property != null) {
                String label = property.getLabel();
                String version = property.getVersion();
                ERepositoryObjectType type = object.getRepositoryObjectType();
                boolean isItemDeleted = factory.getStatus(object) == ERepositoryStatus.DELETED;
                Item item = property.getItem();
                if (!(item instanceof ProcessItem)) {
                    return list;
                }
                List<Relation> relations = RelationshipItemBuilder.getInstance().getItemsHaveRelationWith(property.getId());
                if (relations.isEmpty()) {
                    return list;
                }
                Set<Project> refParentProjects = new HashSet<Project>();
                try {
                    refParentProjects.add(ProjectManager.getInstance().getCurrentProject());
//                    refParentProjects.addAll(ProjectManager.getInstance().getReferencedProjects());
                    for (Project refP : refParentProjects) {

                        // get all types processes such as standard job, big data streaming job, big data batch job;
                        // since big data job also support subjobs
                        List<IRepositoryViewObject> processes = new LinkedList<IRepositoryViewObject>();
                        List<ERepositoryObjectType> allTypesOfProcess = ERepositoryObjectType.getAllTypesOfProcess();
                        Iterator<ERepositoryObjectType> processIter = allTypesOfProcess.iterator();
                        while (processIter.hasNext()) {
                            ERepositoryObjectType process = processIter.next();
                            List<IRepositoryViewObject> repositoryViewObjectList = factory.getAll(refP, process, true);
                            if (repositoryViewObjectList != null) {
                                processes.addAll(repositoryViewObjectList);
                            }
                        }

                        deleteActionCache.setProcessList(processes);
                        Iterator<IRepositoryViewObject> processObjectIter = deleteActionCache.getProcessList().iterator();
                        while (processObjectIter.hasNext()) {
                            IRepositoryViewObject process = processObjectIter.next();
                            Property property2 = process.getProperty();
                            if (isOpenedItem(property2, deleteActionCache.getOpenProcessMap())) {
                                // will be checked in the opened item list.
                                continue;
                            }
                            Relation current = new Relation();
                            current.setId(property2.getId());
                            current.setType(RelationshipItemBuilder.JOB_RELATION);
                            current.setVersion(property2.getVersion());
                            if (!relations.contains(current)) {
                                continue;
                            }
                            boolean isDelete = factory.getStatus(process) == ERepositoryStatus.DELETED;
                            Item item2 = property2.getItem();
                            if (item == item2) {
                                continue;
                            }

                            String path = item2.getState().getPath();
                            boolean found = false;
                            ItemReferenceBean bean = new ItemReferenceBean();
                            bean.setItemName(label);
                            bean.setItemVersion(version);
                            bean.setItemType(type);
                            bean.setItemDeleted(isItemDeleted);
                            bean.setReferenceItemName(property2.getLabel());
                            bean.setReferenceItemVersion(property2.getVersion());
                            bean.setReferenceItemType(process.getRepositoryObjectType());
                            bean.setReferenceItemPath(path);
                            bean.setReferenceProjectName(refP.getLabel());
                            bean.setReferenceItemDeleted(isDelete);
                            for (ItemReferenceBean b : list) {
                                if (b.equals(bean)) {
                                    found = true;
                                    b.addNodeNum();
                                    break;
                                }
                            }
                            if (!found) {
                                list.add(bean);
                            }
                        }
                    }

                    for (IProcess2 openedProcess : deleteActionCache.getOpenedProcessList()) {
                        for (INode node : openedProcess.getGraphicalNodes()) {
                            boolean equals = false;
                            IElementParameter processTypeParam = node.getElementParameter(PROCESS_TYPE_PROCESS);
                            if (processTypeParam != null) {
                                IElementParameter isUseDynamicJob = node.getElementParameter(USE_DYNAMIC_JOB);
                                if (isUseDynamicJob != null && (Boolean) isUseDynamicJob.getValue()) {
                                    String[] jobsID = ((String) processTypeParam.getValue()).split(";"); //$NON-NLS-1$
                                    for (String jobID : jobsID) {
                                        if (property.getId().equals(jobID)) {
                                            equals = true;
                                            break;
                                        }
                                    }
                                } else if (property.getId().equals(processTypeParam.getValue())) {
                                    equals = true;
                                }
                            }
                            boolean isDelete = factory.getStatus(openedProcess) == ERepositoryStatus.DELETED;
                            Property property2 = openedProcess.getProperty();
                            Item item2 = property2.getItem();
                            String path = item2.getState().getPath();
                            if (equals) {
                                boolean found = false;
                                ItemReferenceBean bean = new ItemReferenceBean();
                                bean.setItemName(label);
                                bean.setItemVersion(version);
                                bean.setItemType(type);
                                bean.setItemDeleted(isItemDeleted);
                                bean.setReferenceItemName(property2.getLabel());
                                bean.setReferenceItemVersion(property2.getVersion());
                                bean.setReferenceItemType(ERepositoryObjectType.getItemType(item2));
                                bean.setReferenceItemPath(path);

                                bean.setReferenceProjectName(ProjectManager.getInstance().getProject(property2).getLabel());
                                bean.setReferenceItemDeleted(isDelete);
                                for (ItemReferenceBean b : list) {
                                    if (b.equals(bean)) {
                                        found = true;
                                        b.addNodeNum();
                                        break;
                                    }
                                }
                                if (!found) {
                                    list.add(bean);
                                }
                            }
                        }
                    }
                } catch (PersistenceException e) {
                    ExceptionHandler.process(e);
                }
            }
        }

        /*
         * If the reference job or joblet is in the recycle bin but the joblet is not then no need to check; If both the
         * joblet and the reference job or joblet are all in the recycle bin and they are all in the delete list then no
         * need to check them; If both the joblet and the reference job or joblet are all not in the recycle bin and
         * they are all in the delete list then no need to check them too.
         */
        Iterator<ItemReferenceBean> it = list.iterator();
        while (it.hasNext()) {
            ItemReferenceBean bean = it.next();
            if ((!bean.isItemDeleted() && bean.isReferenceItemDeleted())
                    || (bean.isItemDeleted() && bean.isReferenceItemDeleted() && isItemInDeleteList(bean, true))
                    || (!bean.isItemDeleted() && !bean.isReferenceItemDeleted() && isItemInDeleteList(bean, true))) {
                it.remove();
            }
        }

        return list;
    }

    private boolean isOpenedItem(Property property, MultiKeyMap openProcessMap) {
        if (property == null) {
            return false;
        }
        return (openProcessMap.get(property.getId(), property.getLabel(), property.getVersion()) != null);
    }

}
