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
package org.talend.core.model.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Path;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IProxyRepositoryService;

/**
 * Represents a folder as a repository object.<br/>
 * 
 * A folder has (in this version) no version, no author.
 * 
 * $Id: Folder.java 44053 2010-06-12 09:14:16Z nma $
 * 
 */
public class Folder extends RepositoryObject implements IRepositoryObject {

    private ERepositoryObjectType type;

    private String projectLabel;

    private String path;

    public Folder(String id, String label) {
        super(id, label);
    }

    public Folder(Property property, ERepositoryObjectType type) {
        super(property.getId(), property.getLabel());
        FolderItem folderItem = PropertiesFactory.eINSTANCE.createFolderItem();
        folderItem.setProperty(super.getProperty());
        super.getProperty().setItem(folderItem);
        ItemState itemState = PropertiesFactory.eINSTANCE.createItemState();
        folderItem.setState(itemState);

        itemState.setDeleted(property.getItem().getState().isDeleted());
        path = property.getItem().getState().getPath();
        itemState.setPath(path);
        org.talend.core.model.properties.Project emfproject = ProjectManager.getInstance().getProject(property.getItem());
        this.projectLabel = emfproject.getLabel();

        this.type = type;
    }

    /**
     * Returns the type.
     * 
     * @return ERepositoryObjectType.FOLDER
     */
    @Override
    public ERepositoryObjectType getRepositoryObjectType() {
        return ERepositoryObjectType.FOLDER;
    }

    public ERepositoryObjectType getContentType() {
        return this.type;
    }

    @Override
    public List<IRepositoryViewObject> getChildren() {
        List<IRepositoryViewObject> toReturn = new ArrayList<IRepositoryViewObject>();
        // disable the code for now, will return an empty list.
        // in all case the code before must have some classcast exception since the children of folder can be something
        // else than folder item !!

        // FolderItem folderItem = (FolderItem) getProperty().getItem();
        //
        // for (Object current : folderItem.getChildren()) {
        // IRepositoryObject currentChild = new Folder(((FolderItem) current).getProperty(), getContentType());
        // toReturn.add(currentChild);
        // }

        return toReturn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.RepositoryObject#getProperty()
     */
    @Override
    public Property getProperty() {
        if (this.projectLabel == null) {
            // for SQL builder
            return super.getProperty();
        }
        IProxyRepositoryFactory factory = null;
        factory = ((IProxyRepositoryService) GlobalServiceRegister.getDefault().getService(IProxyRepositoryService.class))
                .getProxyRepositoryFactory();
        Project project = ProjectManager.getInstance().getProjectFromProjectLabel(this.projectLabel);
        FolderItem folderItem = factory.getFolderItem(project, type,
                new Path(StringUtils.join(new String[] { path, getLabel() }, "/"))); //$NON-NLS-1$

        if (folderItem == null) {
            // seems doesn't work only for job and joblet documentation.
            return super.getProperty();
        }
        return folderItem.getProperty();
    }

}
