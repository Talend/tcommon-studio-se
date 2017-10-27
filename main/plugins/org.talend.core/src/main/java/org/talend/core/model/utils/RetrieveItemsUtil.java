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
package org.talend.core.model.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProjectReference;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.designer.runprocess.ItemCacheManager;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;

/**
 * cli class global comment. Detailled comment
 */
public final class RetrieveItemsUtil {

    private RetrieveItemsUtil() {
    }

    /**
     * 
     * cli Comment method "retrieveItems".
     * 
     */
    public static List<IRepositoryViewObject> retrieveItems(final ERepositoryObjectType[] types, final boolean withLastVersion,
            final boolean withDeleted, final boolean withLocked, final boolean withRefProject) throws PersistenceException {
        final Project curProject = ProjectManager.getInstance().getCurrentProject();
        final IProxyRepositoryFactory factory = CorePlugin.getDefault().getProxyRepositoryFactory();

        List<IRepositoryViewObject> results = new ArrayList<IRepositoryViewObject>();
        if (types != null) {
            for (ERepositoryObjectType type : types) {
                retrieveItems(factory, curProject, type, withLastVersion, withDeleted, withLocked, withRefProject, results);
            }

        }
        return results;
    }

    /**
     * 
     * cli Comment method "retrieveObjectByIndexWithType".
     * 
     * ? Did the index function support for Ref project or not?
     * 
     */
    public static List<IRepositoryViewObject> retrieveObjectByIndexWithType(String relatedId, final boolean withLastVersion,
            final boolean withDeleted, final boolean withLocked, final boolean withRefProject, String sourceRelation,
            String[] targetRelations) throws PersistenceException {
        if (relatedId == null || "".equals(relatedId.trim()) || RepositoryNode.NO_ID.equals(relatedId.trim())) { //$NON-NLS-1$
            return Collections.emptyList();
        }
        if (targetRelations == null) {
            return Collections.emptyList();
        }

        RelationshipItemBuilder relationsBuilder = RelationshipItemBuilder.getInstance();
        final List<Relation> itemsRelated = relationsBuilder.getItemsRelatedTo(relatedId, ItemCacheManager.LATEST_VERSION,
                sourceRelation);
        final IProxyRepositoryFactory factory = CorePlugin.getDefault().getProxyRepositoryFactory();

        List<IRepositoryViewObject> tmpObjects = new ArrayList<IRepositoryViewObject>();

        for (Relation r : itemsRelated) {
            if (ArrayUtils.contains(targetRelations, r.getType())) {
                IRepositoryViewObject lastVersion = factory.getLastVersion(r.getId());
                if (lastVersion == null) {
                    continue;
                }
                ERepositoryStatus status = factory.getStatus(lastVersion);

                if ((withDeleted || status != ERepositoryStatus.DELETED)
                        && (withLocked || status != ERepositoryStatus.LOCK_BY_OTHER && status != ERepositoryStatus.LOCK_BY_USER)) {

                    if (withLastVersion) {
                        if (!getTheSameObject(tmpObjects, lastVersion)) {
                            tmpObjects.add(lastVersion);
                        }

                    } else {
                        Item item = lastVersion.getProperty().getItem();
                        ERepositoryObjectType objectType = ERepositoryObjectType.getItemType(item);
                        List<IRepositoryViewObject> allVersion = factory.getAllVersion(lastVersion.getId(), item.getState()
                                .getPath(), objectType);
                        if (allVersion == null) {
                            continue;
                        }
                        for (IRepositoryViewObject version : allVersion) {
                            if (!getTheSameObject(tmpObjects, version)) {
                                tmpObjects.add(version);
                            }
                        }

                    }
                }
            }
        }

        return tmpObjects;

    }

    private static boolean getTheSameObject(List<IRepositoryViewObject> tmpObjects, IRepositoryViewObject version) {
        if (version == null) {
            return true;
        }
        List<IRepositoryViewObject> tmpList = new ArrayList<IRepositoryViewObject>(tmpObjects);
        for (IRepositoryViewObject obj : tmpList) {
            if (obj.getProperty().equals(version.getProperty())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * cli Comment method "retrieveItems".
     * 
     * arguments set by preference
     */
    public static List<IRepositoryViewObject> retrieveItems(ERepositoryObjectType[] types) throws PersistenceException {
        final boolean withLastVersion = checkLastVersion();
        final boolean withDeleted = checkDeleted();
        final boolean withLocked = checkLocked();
        final boolean withRefProject = checkRefProject();

        return retrieveItems(types, withLastVersion, withDeleted, withLocked, withRefProject);

    }

    // public static List<IRepositoryViewObject> retrieveObjectByIndexWithType(String id, String indexType)
    // throws PersistenceException {
    // final boolean withLastVersion = checkLastVersion();
    // final boolean withDeleted = checkDeleted();
    // final boolean withLocked = checkLocked();
    // final boolean withRefProject = checkRefProject();
    //
    // return retrieveObjectByIndexWithType(id, withLastVersion, withDeleted, withLocked, withRefProject, indexType);
    // }

    private static boolean checkLastVersion() {
        return Boolean.parseBoolean(CorePlugin.getDefault().getDesignerCoreService().getPreferenceStore("checkOnlyLastVersion")); //$NON-NLS-1$
    }

    private static boolean checkDeleted() {
        return true; // maybe, need set in preference.
    }

    private static boolean checkLocked() {
        return true; // maybe, need set in preference.
    }

    private static boolean checkRefProject() {
        return true; // maybe, need set in preference.
    }

    @SuppressWarnings("unchecked")
    private static void retrieveItems(final IProxyRepositoryFactory factory, final Project project,
            final ERepositoryObjectType type, final boolean withLastVersion, final boolean withDeleted, final boolean withLocked,
            final boolean withRefProject, final List<IRepositoryViewObject> results) throws PersistenceException {
        // List<IRepositoryObject> tmpList = new ArrayList<IRepositoryObject>();

        List<IRepositoryViewObject> list = factory.getAll(project, type, withDeleted, !withLastVersion);
        if (list == null) {
            list = new ArrayList<IRepositoryViewObject>();
        }
        // Don't need this codes, because the argument "allVersions" in "factory.getAll".

        // if (onlyLastVersion) {
        // Set<String> processedIds = new HashSet<String>();
        // for (IRepositoryObject object : list) {
        // Property property = object.getProperty();
        // Item item = property.getItem();
        //
        // if (!processedIds.contains(property.getId())) { // avoid the duplicate last version
        // IRepositoryObject lastVersion = factory.getLastVersion(project, property.getId(), item.getState().getPath(),
        // type);
        // if (lastVersion != null) {
        // tmpList.add(lastVersion);
        // }
        // processedIds.add(property.getId());
        // }
        // }
        // } else {
        // tmpList.addAll(list);
        // }

        // locked
        if (withLocked) {
            results.addAll(list);
        } else {
            for (IRepositoryViewObject object : list) {
                if (factory.getStatus(object) != ERepositoryStatus.LOCK_BY_OTHER
                        && factory.getStatus(object) != ERepositoryStatus.LOCK_BY_USER) {
                    results.add(object);
                }
            }
        }
        if (withRefProject) {
            Context ctx = CorePlugin.getContext();
            if (ctx != null) {
                String parentBranch = ProjectManager.getInstance().getMainProjectBranch(project);

                EList refProjects = project.getEmfProject().getReferencedProjects();
                for (ProjectReference pRef : (List<ProjectReference>) refProjects) {
                    if (pRef.getBranch() == null || parentBranch.equals(pRef.getBranch())) {
                        Project refP = new Project(pRef.getReferencedProject());
                        retrieveItems(factory, refP, type, withLastVersion, withDeleted, withLocked, withRefProject, results);
                    }
                }
            }
        }

    }

}
