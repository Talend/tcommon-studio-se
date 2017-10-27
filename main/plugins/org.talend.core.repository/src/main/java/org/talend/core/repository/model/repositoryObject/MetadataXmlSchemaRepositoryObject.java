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
package org.talend.core.repository.model.repositoryObject;

import java.util.Date;
import java.util.List;

import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.core.model.metadata.MetadataXmlSchema;
import org.talend.core.model.metadata.builder.connection.AbstractMetadataObject;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.User;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.ISubRepositoryObject;
import org.talend.cwm.xml.TdXmlSchema;
import org.talend.repository.model.IRepositoryNode;

import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class MetadataXmlSchemaRepositoryObject extends MetadataXmlSchema implements ISubRepositoryObject {

    private final IRepositoryViewObject viewObject;

    private IRepositoryNode repositoryNode;

    private final TdXmlSchema tdXmlSchema;

    public IRepositoryViewObject getViewObject() {
        return this.viewObject;
    }

    @Override
    public IRepositoryNode getRepositoryNode() {
        return this.repositoryNode;
    }

    @Override
    public void setRepositoryNode(IRepositoryNode repositoryNode) {
        this.repositoryNode = repositoryNode;
    }

    public TdXmlSchema getTdXmlSchema() {
        return this.tdXmlSchema;
    }

    @Override
    public List<IRepositoryViewObject> getChildren() {
        return this.viewObject.getChildren();
    }

    /**
     * DOC klliu MetadataXmlSchemaRepositoryObject constructor comment.
     * 
     * @param repositoryViewObject
     * @param tdXmlSchema
     */
    public MetadataXmlSchemaRepositoryObject(IRepositoryViewObject repositoryViewObject, TdXmlSchema tdXmlSchema) {
        this.viewObject = repositoryViewObject;
        this.tdXmlSchema = tdXmlSchema;
    }

    @Override
    public String getLabel() {
        return getTdXmlSchema().getName();
    }

    @Override
    public String getId() {
        return getTdXmlSchema().getName();
    }

    @Override
    public AbstractMetadataObject getAbstractMetadataObject() {
        return null;
    }

    @Override
    public ERepositoryObjectType getRepositoryObjectType() {
        return ERepositoryObjectType.MDM_SCHEMA;
    }

    @Override
    public boolean isDeleted() {
        return false;
    }

    @Override
    public void removeFromParent() {
    }

    private void updateCatalog(Property property) {
    }

    // @Override
    @Override
    public Property getProperty() {
        Property property = viewObject.getProperty();
        updateCatalog(property);
        return property;
    }

    @Override
    public String getVersion() {
        return viewObject.getVersion();
    }

    @Override
    public User getAuthor() {
        return viewObject.getAuthor();
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
    public ERepositoryStatus getRepositoryStatus() {
        return viewObject.getRepositoryStatus();
    }

    @Override
    public String getStatusCode() {
        return viewObject.getStatusCode();
    }

    @Override
    public ModelElement getModelElement() {
        return this.tdXmlSchema;
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
