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
package org.talend.core.repository.model.repositoryObject;

import java.util.Date;
import java.util.List;

import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.core.model.metadata.MetadataCatalog;
import org.talend.core.model.metadata.builder.connection.AbstractMetadataObject;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.User;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.ISubRepositoryObject;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.repository.model.IRepositoryNode;

import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.Catalog;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class MetadataCatalogRepositoryObject extends MetadataCatalog implements ISubRepositoryObject {

    private final IRepositoryViewObject viewObject;

    public IRepositoryViewObject getViewObject() {
        return this.viewObject;
    }

    private Catalog catalog;

    private IRepositoryNode repositoryNode;

    public Catalog getCatalog() {
        return catalog;
    }

    /**
     * DOC klliu MetadataCatalogRepositoryObject constructor comment.
     * 
     * Arguments must not be null.
     * 
     * @param repositoryViewObject
     * @param catalog
     */
    public MetadataCatalogRepositoryObject(IRepositoryViewObject repositoryViewObject, Catalog catalog) {
        assert catalog != null;
        assert repositoryViewObject != null;

        this.viewObject = repositoryViewObject;
        this.catalog = catalog;
    }

    @Override
    public Property getProperty() {
        Property property = viewObject.getProperty();
        // update catalog
        updateCatalog(property);
        return property;
    }

    @Override
    public String getVersion() {
        return viewObject.getVersion();
    }

    @Override
    public String getLabel() {
        return getCatalog().getName();
    }

    @Override
    public String getId() {
        return getCatalog().getName();
    }

    @Override
    public AbstractMetadataObject getAbstractMetadataObject() {
        return null;
    }

    @Override
    public void removeFromParent() {
    }

    /**
     * update the Catalog object according to the Property, because when the connection has been reloaded, the catalog
     * object should be changed, so need use the new catalog object.
     * 
     * @param property
     */
    private void updateCatalog(Property property) {
        if (catalog != null) {
            Item item = property.getItem();
            if (item != null && item instanceof ConnectionItem) {
                Connection connection = ((ConnectionItem) item).getConnection();
                if (connection != null) {
                    Catalog catalog2 = CatalogHelper.getCatalog(connection, catalog.getName());
                    if (catalog2 != null) {
                        catalog = catalog2;
                    }
                }
            }
        }
    }

    @Override
    public User getAuthor() {
        return viewObject.getAuthor();
    }

    @Override
    public List<IRepositoryViewObject> getChildren() {
        return this.viewObject.getChildren();
    }

    @Override
    public Date getCreationDate() {
        return viewObject.getCreationDate();
    }

    @Override
    public String getDescription() {
        return viewObject.getDescription();
    }

    @Override
    public ERepositoryStatus getInformationStatus() {
        return viewObject.getInformationStatus();
    }

    @Override
    public Date getModificationDate() {
        return viewObject.getModificationDate();
    }

    @Override
    public String getPath() {
        return viewObject.getPath();
    }

    @Override
    public String getProjectLabel() {
        return viewObject.getProjectLabel();
    }

    @Override
    public String getPurpose() {
        return viewObject.getPurpose();
    }

    @Override
    public IRepositoryNode getRepositoryNode() {
        return this.repositoryNode;
    }

    @Override
    public ERepositoryStatus getRepositoryStatus() {
        return viewObject.getRepositoryStatus();
    }

    @Override
    public String getStatusCode() {
        return viewObject.getStatusCode();
    }

    @Override
    public ERepositoryObjectType getRepositoryObjectType() {
        return ERepositoryObjectType.METADATA_CON_CATALOG;
    }

    @Override
    public boolean isDeleted() {
        return false;
    }

    @Override
    public void setRepositoryNode(IRepositoryNode node) {
        this.repositoryNode = node;
    }

    @Override
    public ModelElement getModelElement() {
        return this.catalog;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryViewObject#isModified()
     */
    @Override
    public boolean isModified() {
        return viewObject.isModified();
    }

}
