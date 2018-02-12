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
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.AbstractMetadataObject;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.User;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.ISubRepositoryObject;
import org.talend.cwm.helper.SubItemHelper;
import org.talend.repository.model.IRepositoryNode;

import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class MetadataColumnRepositoryObject extends MetadataColumn implements ISubRepositoryObject {

    private final IRepositoryViewObject viewObject;

    private IRepositoryNode repositoryNode;

    public IRepositoryViewObject getViewObject() {
        return this.viewObject;
    }

    private org.talend.core.model.metadata.builder.connection.MetadataColumn tdColumn;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.ITDQMetadataColumn#getTdColumn()
     */
    public org.talend.core.model.metadata.builder.connection.MetadataColumn getTdColumn() {
        return this.tdColumn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.ITDQMetadataColumn#setTdColumn()
     */
    public void setTdColumn(org.talend.core.model.metadata.builder.connection.MetadataColumn tdColumn) {
        this.tdColumn = tdColumn;
    }

    /**
     * DOC klliu TDQMetadataColumnRepositoryObject constructor comment.
     */
    public MetadataColumnRepositoryObject(IRepositoryViewObject repositoryViewObject,
            org.talend.core.model.metadata.builder.connection.MetadataColumn column) {
        this.viewObject = repositoryViewObject;
        setTdColumn(column);
    }

    @Override
    public Property getProperty() {
        Property property = viewObject.getProperty();
        // update column
        updateColumn(property);
        return property;
    }

    // @Override
    @Override
    public String getVersion() {
        return viewObject.getVersion();
    }

    @Override
    public String getLabel() {
        return getTdColumn().getLabel();
    }

    @Override
    public void setLabel(String label) {
        this.getTdColumn().setLabel(label);
    }

    @Override
    public String getId() {
        return getTdColumn().getId();
    }

    @Override
    public void setId(String id) {
        getTdColumn().setId(id);
    }

    @Override
    public AbstractMetadataObject getAbstractMetadataObject() {
        return getTdColumn();
    }

    @Override
    public void removeFromParent() {
    }

    private void updateColumn(Property property) {
    }

    @Override
    public User getAuthor() {
        return viewObject.getAuthor();
    }

    @Override
    public List<IRepositoryViewObject> getChildren() {
        return viewObject.getChildren();
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
        return ERepositoryObjectType.METADATA_CON_COLUMN;
    }

    @Override
    public boolean isDeleted() {
        return SubItemHelper.isDeleted(getTdColumn());
    }

    @Override
    public void setRepositoryNode(IRepositoryNode node) {
        this.repositoryNode = node;
    }

    @Override
    public ModelElement getModelElement() {
        return this.tdColumn;
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
