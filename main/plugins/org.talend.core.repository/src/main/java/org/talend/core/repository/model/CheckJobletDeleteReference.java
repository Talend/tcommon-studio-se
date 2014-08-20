package org.talend.core.repository.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.map.MultiKeyMap;
import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.ICheckDeleteItemReference;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ItemReferenceBean;
import org.talend.repository.ui.actions.DeleteActionCache;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class CheckJobletDeleteReference extends AbstractCheckDeleteItemReference implements ICheckDeleteItemReference {

    // almost move from the method checkRepositoryNodeFromProcess of DeleteAction class.
    @Override
    public Set<ItemReferenceBean> checkItemReferenceBeans(IProxyRepositoryFactory factory, DeleteActionCache deleteActionCache,
    		IRepositoryViewObject object) {
        Item nodeItem = object.getProperty().getItem(); // hywang add
        boolean needCheckJobletIfUsedInProcess = false;
        if (nodeItem instanceof JobletProcessItem) {
            needCheckJobletIfUsedInProcess = true;
        }
        Set<ItemReferenceBean> list = new HashSet<ItemReferenceBean>();
        if (needCheckJobletIfUsedInProcess) {
            Property property = object.getProperty();
            if (property != null) {
                String label = property.getLabel();
                String version = property.getVersion();
                ERepositoryObjectType type = object.getRepositoryObjectType();
                boolean isItemDeleted = factory.getStatus(object) == ERepositoryStatus.DELETED;
                Item item = property.getItem();
                if (!(item instanceof JobletProcessItem)) {
                    return list;
                }
                List<Relation> relations = RelationshipItemBuilder.getInstance().getItemsHaveRelationWith(property.getId());
                if (relations.isEmpty()) {
                	return list;
                }
                Set<Project> refParentProjects = new HashSet<Project>();
                try {
                    refParentProjects.add(ProjectManager.getInstance().getCurrentProject());
                    refParentProjects.addAll(ProjectManager.getInstance().getReferencedProjects());
                    for (Project refP : refParentProjects) {
                        List<IRepositoryViewObject> processes = new ArrayList<IRepositoryViewObject>(50);

                        ERepositoryObjectType jobType = ERepositoryObjectType.PROCESS;
                        if (jobType != null) {
                            List<IRepositoryViewObject> jobs = factory.getAll(refP, jobType, true);
                            checkRelationshipItems(factory, jobs, RelationshipItemBuilder.JOB_RELATION,
    								list, label, version, type, isItemDeleted,
    								item, relations, refP, deleteActionCache);
                        }
                        ERepositoryObjectType jobletType = ERepositoryObjectType.JOBLET;
                        if (jobletType != null) {
                            List<IRepositoryViewObject> joblets = factory.getAll(refP, jobletType, true);
                            checkRelationshipItems(factory, joblets, RelationshipItemBuilder.JOBLET_RELATION,
    								list, label, version, type, isItemDeleted,
    								item, relations, refP,deleteActionCache);
                        }
                        deleteActionCache.setProcessList(processes);
                        for (IProcess2 openedProcess : deleteActionCache.getOpenedProcessList()) {
                            for (INode node : openedProcess.getGraphicalNodes()) {
                                boolean equals = node.getComponent().getName().equals(label);
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

	private void checkRelationshipItems(IProxyRepositoryFactory factory, List<IRepositoryViewObject> objects, String curRepoObjectType, Set<ItemReferenceBean> list,
			String label, String version, ERepositoryObjectType type,
			boolean isItemDeleted, Item item, List<Relation> relations,
			Project refP, DeleteActionCache deleteActionCache) {
		for (IRepositoryViewObject process : objects) {
		    Property property2 = process.getProperty();
		    boolean isDelete = factory.getStatus(process) == ERepositoryStatus.DELETED;
		    Item item2 = property2.getItem();
		    if (item == item2) {
		        continue;
		    }
		    if (isOpenedItem(property2, deleteActionCache.getOpenProcessMap())) {
		    	// will be checked in the opened item list.
		    	continue;
		    }
		    Relation current = new Relation();
		    current.setId(property2.getId());
		    current.setType(curRepoObjectType);
		    current.setVersion(property2.getVersion());
		    if (!relations.contains(current)) {
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

    private boolean isOpenedItem(Property property, MultiKeyMap openProcessMap) {
        if (property == null) {
            return false;
        }
        return (openProcessMap.get(property.getId(), property.getLabel(), property.getVersion()) != null);
    }

}
