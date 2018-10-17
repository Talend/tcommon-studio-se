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
package org.talend.core.repository.recyclebin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.emf.EmfHelper;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.constants.FileConstants;
import org.talend.core.repository.model.ProjectRepositoryNode;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.utils.URIHelper;
import org.talend.model.recyclebin.RecycleBin;
import org.talend.model.recyclebin.RecycleBinFactory;
import org.talend.model.recyclebin.RecycleBinPackage;
import org.talend.model.recyclebin.TalendItem;
import org.talend.repository.ProjectManager;

/**
 * created by nrousseau on Jun 15, 2015 Detailled comment
 *
 */
public class RecycleBinManager {

    private static RecycleBinManager manager;

    private Map<String, RecycleBin> projectRecyclebins;

    private Map<RecycleBin, RecycleBin> lastSavedRecycleBinMap;

    private RecycleBinManager() {
        lastSavedRecycleBinMap = new HashMap<>();
        projectRecyclebins = new HashMap<>();
    }

    public static RecycleBinManager getInstance() {
        if (manager == null) {
            manager = new RecycleBinManager();
        }
        return manager;
    }

    public List<String> getDeletedFolders(Project project) {
        return new ArrayList<String>(project.getEmfProject().getDeletedFolders());
    }

    public void clearCache() {
        projectRecyclebins.clear();
        lastSavedRecycleBinMap.clear();
    }

    public void clearCache(Project project) {
        clearCache(project.getEmfProject());
    }

    public void clearCache(org.talend.core.model.properties.Project project) {
        String projectTechnicalLabel = project.getTechnicalLabel();
        RecycleBin removedRecycleBin = projectRecyclebins.remove(projectTechnicalLabel);
        if (removedRecycleBin != null) {
            lastSavedRecycleBinMap.remove(removedRecycleBin);
        }
    }

    public void clearIndex(Project project) {
        loadRecycleBin(project.getEmfProject(), true);
        projectRecyclebins.get(project.getTechnicalLabel()).getDeletedItems().clear();
        saveRecycleBin(project.getEmfProject());
    }

    public List<IRepositoryViewObject> getDeletedObjects(Project project) {
        loadRecycleBin(project.getEmfProject(), true);
        List<IRepositoryViewObject> deletedObjects = new ArrayList<IRepositoryViewObject>();
        final EList<TalendItem> deletedItems = projectRecyclebins.get(project.getTechnicalLabel()).getDeletedItems();
        List<TalendItem> notDeletedItems = new ArrayList<TalendItem>();
        for (TalendItem deletedItem : deletedItems) {
            try {
                final ERepositoryObjectType type = ERepositoryObjectType.getType(deletedItem.getType());
                // ignore the generated doc in recycle bin
                if (type != null && (type.equals(ERepositoryObjectType.JOB_DOC) || type.equals(ERepositoryObjectType.JOBLET_DOC)
                        || type.equals(ERepositoryObjectType.valueOf("ROUTE_DOC")))) { //$NON-NLS-1$
                    continue;
                }
                IRepositoryViewObject object = ProxyRepositoryFactory.getInstance().getLastVersion(project, deletedItem.getId(),
                        deletedItem.getPath(), type);
                if (object == null) {
                    object = ProxyRepositoryFactory.getInstance().getLastVersion(project, deletedItem.getId());
                }
                if (object != null) {
                    Item item = object.getProperty().getItem();
                    boolean hasSubItem = false;
                    if (item instanceof ConnectionItem) {
                        hasSubItem = ProjectRepositoryNode.getInstance().hasDeletedSubItem((ConnectionItem) item);
                    }
                    if (object.isDeleted() || hasSubItem) {
                        deletedObjects.add(object);
                    } else {
                        // need remove it.
                        notDeletedItems.add(deletedItem);
                    }
                } else {
                    // need remove it.
                    notDeletedItems.add(deletedItem);
                }
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
        }
        // clean
        deletedItems.removeAll(notDeletedItems);
        return deletedObjects;
    }

    public void addToRecycleBin(Project project, Item item) {
        addToRecycleBin(project, item, false);
    }

    public void addToRecycleBin(Project project, Item item, boolean skipAutoSave) {
        loadRecycleBin(project.getEmfProject(), true);
        boolean contains = false;
        for (TalendItem deletedItem : projectRecyclebins.get(project.getTechnicalLabel()).getDeletedItems()) {
            if (item.getProperty().getId().equals(deletedItem.getId())) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            TalendItem recBinItem = RecycleBinFactory.eINSTANCE.createTalendItem();
            recBinItem.setId(item.getProperty().getId());
            recBinItem.setPath(item.getState().getPath());
            recBinItem.setType(ERepositoryObjectType.getItemType(item).getType());
            projectRecyclebins.get(project.getTechnicalLabel()).getDeletedItems().add(recBinItem);
        }
        if (!skipAutoSave) {
            saveRecycleBin(project);
        }
    }

    public void removeFromRecycleBin(Project project, Item item) {
        removeFromRecycleBin(project, item, false);
    }

    public void removeFromRecycleBin(Project project, Item item, boolean skipAutoSave) {
        loadRecycleBin(project.getEmfProject(), true);
        TalendItem itemToDelete = null;
        for (TalendItem deletedItem : projectRecyclebins.get(project.getTechnicalLabel()).getDeletedItems()) {
            if (item.getProperty().getId().equals(deletedItem.getId())) {
                itemToDelete = deletedItem;
                break;
            }
        }
        if (itemToDelete != null) {
            projectRecyclebins.get(project.getTechnicalLabel()).getDeletedItems().remove(itemToDelete);
            if (!skipAutoSave) {
                saveRecycleBin(project);
            }
        }
    }

    public RecycleBin getRecycleBin(Project project) {
        return getRecycleBin(project.getEmfProject());
    }

    public RecycleBin getRecycleBin(org.talend.core.model.properties.Project project) {
        loadRecycleBin(project, true);
        return projectRecyclebins.get(project.getTechnicalLabel());
    }

    private void loadRecycleBin(org.talend.core.model.properties.Project project, boolean isSynchronizeToProject) {
        if (projectRecyclebins.get(project.getTechnicalLabel()) != null) {
            // already loaded, nothing to do. Don't do any force reload
            return;
        }
        Resource resource = getResource(project);
        try {
            if (resource != null) {
                resource.load(null);
                RecycleBin rbin = (RecycleBin) EcoreUtil.getObjectByType(resource.getContents(),
                        RecycleBinPackage.eINSTANCE.getRecycleBin());
                projectRecyclebins.put(project.getTechnicalLabel(), rbin);
            } else {
                projectRecyclebins.put(project.getTechnicalLabel(), RecycleBinFactory.eINSTANCE.createRecycleBin());
            }
        } catch (IOException e) {
            ExceptionHandler.process(e);
            // if there is any exception, just set a new resource
            projectRecyclebins.put(project.getTechnicalLabel(), RecycleBinFactory.eINSTANCE.createRecycleBin());
        }

        RecycleBin recycleBin = projectRecyclebins.get(project.getTechnicalLabel());
        if (recycleBin != null) {
            RecycleBin lastSavedRecycleBin = EcoreUtil.copy(recycleBin);
            lastSavedRecycleBinMap.put(recycleBin, lastSavedRecycleBin);
        }

        // Synchronize delete folder to project
        if (isSynchronizeToProject) {
            project.getDeletedFolders().clear();
            for (String deletedFolder : recycleBin.getDeletedFolders()) {
                project.getDeletedFolders().add(deletedFolder);
            }  
        } 
    }

    public RecycleBin loadRecycleBin(IPath recycleBinIndexPath) throws Exception {
        Resource resource = createRecycleBinResource(recycleBinIndexPath);
        resource.load(null);
        return loadRecycleBin(resource);
    }

    public RecycleBin loadRecycleBin(Resource resource) {
        return (RecycleBin) EcoreUtil.getObjectByType(resource.getContents(), RecycleBinPackage.eINSTANCE.getRecycleBin());
    }

    public void saveRecycleBin(Project project) {
        saveRecycleBin(project.getEmfProject());
    }

    public void saveRecycleBin(org.talend.core.model.properties.Project project) {
        if (projectRecyclebins.get(project.getTechnicalLabel()) == null) {
            loadRecycleBin(project, false);
        }
        try {
            RecycleBin recycleBin = projectRecyclebins.get(project.getTechnicalLabel());
            boolean recycleBinChanged = true;
            if (recycleBin != null) {
                // Synchronize delete folder to recycleBin
                recycleBin.getDeletedFolders().clear();
                for (int i = 0; i < project.getDeletedFolders().size(); i++) {
                    recycleBin.getDeletedFolders().add((String) project.getDeletedFolders().get(i));
                }
                recycleBinChanged = !equals(recycleBin, lastSavedRecycleBinMap.get(recycleBin));
            }

            if (!recycleBinChanged) {
                return;
            }

            Resource resource = getResource(project);
            if (resource == null) {
                resource = createRecycleBinResource(project);
            }
            resource.getContents().clear();
            recycleBin.setLastUpdate(new Date());
            resource.getContents().add(recycleBin);
            EmfHelper.saveResource(resource);
            lastSavedRecycleBinMap.put(recycleBin, EcoreUtil.copy(recycleBin));
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
    }

    private Resource getResource(org.talend.core.model.properties.Project project) {
        if (projectRecyclebins.get(project.getTechnicalLabel()) == null
                || projectRecyclebins.get(project.getTechnicalLabel()).eResource() == null) {
            IProject eclipseProject = ProjectManager.getInstance().getResourceProject(project);
            if (eclipseProject != null && eclipseProject.getFile(FileConstants.TALEND_RECYCLE_BIN_INDEX).exists()) {
                return createRecycleBinResource(project);
            }
            return null;
        }
        return projectRecyclebins.get(project.getTechnicalLabel()).eResource();
    }

    private Resource createRecycleBinResource(org.talend.core.model.properties.Project project) {
        IProject eclipseProject = ProjectManager.getInstance().getResourceProject(project);
        return createRecycleBinResource(eclipseProject.getFullPath().append(FileConstants.TALEND_RECYCLE_BIN_INDEX));
    }

    public Resource createRecycleBinResource(IPath recycleBinIndexPath) {
        URI uri = URIHelper.convert(recycleBinIndexPath);

        XMLResourceFactoryImpl resourceFact = new XMLResourceFactoryImpl();
        XMLResource resource = (XMLResource) resourceFact.createResource(uri);
        resource.getDefaultSaveOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, Boolean.TRUE);
        resource.getDefaultLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, Boolean.TRUE);

        resource.getDefaultSaveOptions().put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);

        resource.getDefaultLoadOptions().put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE);
        resource.getDefaultSaveOptions().put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE);

        resource.getDefaultLoadOptions().put(XMLResource.OPTION_USE_LEXICAL_HANDLER, Boolean.TRUE);
        return resource;
    }
    
    public static boolean equals(RecycleBin r1, RecycleBin r2) {
        if (r1 == null && r2 == null) {
            return true;
        }
        if (r1 == null || r2 == null) {
            return false;
        }
        boolean isEquals = false;
        Set<String> r1DeletedFolders = new HashSet<>(r1.getDeletedFolders());
        Set<String> r2DeletedFolders = new HashSet<>(r2.getDeletedFolders());

        /**
         * 1. check deleted folders
         */
        if (r1DeletedFolders.size() == r2DeletedFolders.size()) {
            r1DeletedFolders.removeAll(r2DeletedFolders);
            if (r1DeletedFolders.isEmpty()) {
                isEquals = true;
            } else {
                isEquals = false;
            }
        } else {
            isEquals = false;
        }

        /**
         * 2. check deleted items
         */
        if (isEquals) {
            EList<TalendItem> r1DeletedItems = r1.getDeletedItems();
            EList<TalendItem> r2DeletedItems = r2.getDeletedItems();
            if (r1DeletedItems.size() == r2DeletedItems.size()) {
                List<TalendItem> r1DeletedItemList = new LinkedList<>(r1DeletedItems);
                List<TalendItem> r2DeletedItemList = new LinkedList<>(r2DeletedItems);
                TalendItemComparator talendItemComparator = new TalendItemComparator();
                r1DeletedItemList.sort(talendItemComparator);
                r2DeletedItemList.sort(talendItemComparator);
                Iterator<TalendItem> iter1 = r1DeletedItemList.iterator();
                Iterator<TalendItem> iter2 = r2DeletedItemList.iterator();
                boolean differentList = false;
                while (iter1.hasNext() && iter2.hasNext()) {
                    TalendItem item1 = iter1.next();
                    TalendItem item2 = iter2.next();
                    if (talendItemComparator.compare(item1, item2) != 0) {
                        differentList = true;
                        break;
                    }
                }
                if (differentList) {
                    isEquals = false;
                } else {
                    isEquals = true;
                }
            } else {
                isEquals = false;
            }
        }
        return isEquals;
    }

    public static int compare(TalendItem item1, TalendItem item2) {
        if (item1 == null && item2 == null) {
            return 0;
        }
        if (item1 == null) {
            return -1;
        }
        if (item2 == null) {
            return 1;
        }
        int result = 0;

        String type1 = item1.getType();
        String type2 = item2.getType();
        if (type1 == null) {
            type1 = "";
        }
        if (type2 == null) {
            type2 = "";
        }
        result = type1.compareTo(type2);
        if (result != 0) {
            return result;
        }

        String path1 = item1.getPath();
        String path2 = item2.getPath();
        if (path1 == null) {
            path1 = "";
        }
        if (path2 == null) {
            path2 = "";
        }
        result = path1.compareTo(path2);
        if (result != 0) {
            return result;
        }

        String id1 = item1.getId();
        String id2 = item2.getId();
        if (id1 == null) {
            id1 = "";
        }
        if (id2 == null) {
            id2 = "";
        }
        result = id1.compareTo(id2);

        return result;
    }

    private static class TalendItemComparator implements Comparator<TalendItem> {

        @Override
        public int compare(TalendItem arg0, TalendItem arg1) {
            return RecycleBinManager.compare(arg0, arg1);
        }
    }
}
