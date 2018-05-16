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
package org.talend.core.repository.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.FolderType;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.User;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.RepositoryConstants;

/**
 * This helper class contains a set of methods to perform basic operations on FolderItem objects.
 * 
 * DOC tguiu class global comment. Detailled comment <br/>
 * 
 * $Id: FolderHelper.java 44053 2010-06-12 09:14:16Z nma $
 * 
 */
public abstract class FolderHelper {

    protected Project project;

    private User connectedUser;

    /**
     * DOC tguiu FolderHelper constructor comment.
     * 
     * @param project
     */
    protected FolderHelper(Project project, User connectedUser) {
        this.project = project;
        this.connectedUser = connectedUser;

        if (ProjectManager.getInstance().getFolders(project).isEmpty()) {
            initialize();
        }
    }

    private void initialize() {
        for (ERepositoryObjectType type : (ERepositoryObjectType[]) ERepositoryObjectType.values()) {
            // if (type.isDQItemType()) {
            // continue;
            // }
            try {
                if (type.hasFolder()) {
                    String folderName = ERepositoryObjectType.getFolderName(type);
                    if (getFolder(new Path(folderName)) == null) {
                        createSystemFolder(new Path(folderName));
                    }
                }
            } catch (IllegalArgumentException iae) {
                // Some repository object type doesn't need a folder
            }
        }

        if (getFolder(new Path(RepositoryConstants.TEMP_DIRECTORY)) == null) {
            createSystemFolder(new Path(RepositoryConstants.TEMP_DIRECTORY));
        }

        if (getFolder(new Path("code/routines/system")) == null) { //$NON-NLS-1$  
            createSystemFolder(new Path("code/routines/system")); //$NON-NLS-1$  
        }
    }

    public void createSystemFolder(IPath path) {
        doCreateFolder(path, FolderType.SYSTEM_FOLDER_LITERAL);
    }

    public FolderItem createFolder(String path) {
        createFolder(new Path(path));
        return getFolder(path);
    }

    public void createFolder(IPath path) {
        FolderType folderType = FolderType.FOLDER_LITERAL;
        for (ERepositoryObjectType type : (ERepositoryObjectType[]) ERepositoryObjectType.values()) {
            if (type.hasFolder()) {
                String folderName = ERepositoryObjectType.getFolderName(type);
                if (folderName.equals(path.toString())) {
                    folderType = FolderType.SYSTEM_FOLDER_LITERAL;
                }
            }
        }
        doCreateFolder(path, folderType);

        if (folderType == FolderType.SYSTEM_FOLDER_LITERAL) {
            EObject currentFolder = getFolder(path).getParent();
            while ((currentFolder instanceof FolderItem) && ((FolderItem) currentFolder).getParent() != null) {
                ((FolderItem) currentFolder).setType(folderType);
                currentFolder = ((FolderItem) currentFolder).getParent();
            }
        }
    }

    private void doCreateFolder(IPath path, FolderType type) {
        int segmentCount = path.segmentCount();
        if (segmentCount < 1) {
            return;
        }
        FolderItem folder = null;
        EObject parent = project;
        for (int i = 0; i < segmentCount; i++) {
            String segment = path.segment(i);
            FolderItem child = findChildFolder(folder, segment);
            if (child == null) {
                child = PropertiesFactory.eINSTANCE.createFolderItem();
                child.setType(type);

                Property property = PropertiesFactory.eINSTANCE.createProperty();
                property.setId(EcoreUtil.generateUUID());
                property.setLabel(segment);
                property.setVersion(VersionUtils.DEFAULT_VERSION);
                property.setCreationDate(new Date());
                property.setAuthor(connectedUser);

                child.setProperty(property);

                createItemState(child);
                doCreateFolder(child);

                if (folder == null) {
                    ProjectManager.getInstance().getFolders(project).add(child);
                } else {
                    folder.getChildren().add(child);
                }
            }
            folder = child;
            folder.setParent(parent);
            final String folderPath = getFolderPath(folder, null);
            if (folderPath != null) {
                folder.getState().setPath(folderPath);
            }
            parent = folder;
        }
    }

    protected abstract void doCreateFolder(FolderItem folderItem);

    public void deleteFolder(String completePath) {
        deleteFolder(new Path(completePath));
    }

    public void deleteFolder(IPath path) {
        int segmentCount = path.segmentCount();
        if (segmentCount < 1) {
            return;
        }
        FolderItem folder = null;
        FolderItem previousFolder = null;
        for (int i = 0; i < segmentCount; i++) {
            String segment = path.segment(i);
            FolderItem child = findChildFolder(folder, segment);
            if (child == null) {
                return;
            }
            previousFolder = folder;
            folder = child;
        }
        if (previousFolder == null) {
            ProjectManager.getInstance().getFolders(project).remove(folder);
        } else {
            previousFolder.getChildren().remove(folder);
        }
        folder.getState().setDeleted(false);
        folder.setParent(null);
        removeFromResource(folder);
    }

    protected abstract void removeFromResource(FolderItem folder);

    private static FolderItem find(List folders, String name) {
        for (Iterator it = folders.iterator(); it.hasNext();) {
            FolderItem folder = (FolderItem) it.next();
            if (folder.getProperty().getLabel().equals(name)) {
                return folder;
            }
        }
        return null;
    }

    public Set<IPath> listFolders() {
        HashSet<IPath> folders = new HashSet<IPath>();
        list(folders, ProjectManager.getInstance().getFolders(project), new Path("")); //$NON-NLS-1$
        return folders;
    }

    private void list(HashSet<IPath> collector, List<FolderItem> content, IPath currentPath) {
        for (Object o : content) {
            if (o instanceof FolderItem) {
                FolderItem folder = (FolderItem) o;
                IPath folderPath = currentPath.append(folder.getProperty().getLabel());
                collector.add(folderPath);
                list(collector, folder.getChildren(), folderPath);
            }
        }
    }

    public FolderItem getFolder(String pathStr) {
        return getFolder(new Path(pathStr));
    }

    public FolderItem getFolder(IPath path) {
        int segmentCount = path.segmentCount();
        FolderItem folder = null;
        for (int i = 0; i < segmentCount; i++) {
            String segment = path.segment(i);
            FolderItem child = findChildFolder(folder, segment);
            if (child == null) {
                return null;
            }
            folder = child;
        }
        return folder;
    }

    public void moveFolder(String completeOldPath, String completeNewPath) {
        FolderItem folder = getFolder(completeOldPath);
        if (folder == null) {
            return;
        }
        FolderItem destination = getFolder(new Path(completeNewPath).removeLastSegments(1));
        if (destination == null) {
            return;
        }
        destination.getChildren().add(folder);

    }

    public void renameFolder(String completePath, String label) {
        FolderItem folder = getFolder(completePath);
        if (folder != null) {
            folder.getProperty().setLabel(label);
        }
    }

    public FolderItem findChildFolder(FolderItem parent, String name) {
        List children;

        EObject parentItem;

        try {
            if (parent != null) {
                children = parent.getChildren();
                parentItem = parent;
            } else {
                children = ProjectManager.getInstance().getFolders(project);
                parentItem = project;
            }
            // see the bug "6458".
            EList list = new BasicEList(children);

            for (Iterator it = list.iterator(); it.hasNext();) {
                Item item = (Item) it.next();
                if (item.getProperty() == null) { // invalid folder
                    children.remove(item);
                    continue;
                }
                item.setParent(parentItem);
                if (item instanceof FolderItem) {
                    // TDI-29841, if in win, case insensitive issue for folder, must ignore case
                    if (item.getProperty().getLabel().equalsIgnoreCase(name)) {
                        return (FolderItem) item;
                    }
                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        }
        return null;
    }

    /**
     * Returns <code>true</code> if a folder at the same level of <code>folder</code> with a different id and wich label
     * is <code>label</code> exists.
     * 
     * @param folder
     * @param label
     * @return
     */
    public boolean pathExists(FolderItem folder, String label) {
        for (Object child : ((FolderItem) folder.getParent()).getChildren()) {
            if (child instanceof FolderItem) {
                FolderItem folderChild = (FolderItem) child;
                if (!folderChild.getProperty().getId().equals(folder.getProperty().getId())
                        && folderChild.getProperty().getLabel().equals(label)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void createItemState(FolderItem folder) {
        ItemState itemState = PropertiesFactory.eINSTANCE.createItemState();
        folder.setState(itemState);
        doCreateItemState(folder);
    }

    private String getFolderPath(FolderItem folder, String path) {
        if (FolderType.FOLDER_LITERAL.equals(folder.getType())) {
            final EObject parent = folder.getParent();
            if (parent instanceof FolderItem) {
                FolderItem parentFolder = (FolderItem) parent;
                if (FolderType.SYSTEM_FOLDER_LITERAL.equals(parentFolder.getType())) {
                    if (path == null) {
                        path = "";
                    }
                    return path;
                }

                if (path == null || "".equals(path)) {
                    path = parentFolder.getProperty().getLabel();
                } else {
                    path = parentFolder.getProperty().getLabel() + "/" + path;
                }
                return getFolderPath(parentFolder, path);
            }
            return path;

        } else {
            return path;
        }

    }

    public abstract void doCreateItemState(FolderItem folder);

    public String getFullFolderPath(FolderItem folder) {
        return getFullFolderPath(folder, "");
    }

    private String getFullFolderPath(FolderItem folder, String path) {
        if (folder.getParent() instanceof FolderItem) {
            return getFullFolderPath((FolderItem) folder.getParent(), folder.getProperty().getLabel() + "/" + path);
        }
        return folder.getProperty().getLabel() + "/" + path;
    }

}
