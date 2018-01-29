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

import java.util.Iterator;

import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.core.model.metadata.builder.connection.AbstractMetadataObject;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.SAPConnection;
import org.talend.core.model.metadata.builder.connection.SAPIDocUnit;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.ISubRepositoryObject;
import org.talend.core.model.repository.RepositoryObject;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC zli class global comment. Detailled comment
 */
public class SAPIDocRepositoryObject extends RepositoryObject implements ISubRepositoryObject {

    private SAPIDocUnit iDocUnit;

    private final IRepositoryViewObject repObj;

    public SAPIDocRepositoryObject(IRepositoryViewObject repObj, RepositoryNode functionNode, final SAPIDocUnit functionUnit) {
        this.repObj = repObj;
        this.iDocUnit = functionUnit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#getType()
     */
    @Override
    public ERepositoryObjectType getRepositoryObjectType() {
        return ERepositoryObjectType.METADATA_SAP_IDOC;
    }

    @Override
    public void setLabel(String value) {
        if (iDocUnit.getLabel() == null) {
            iDocUnit.setLabel(value);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.RepositoryObject#getLabel()
     */
    @Override
    public String getLabel() {
        return iDocUnit.getLabel();
    }

    @Override
    public Property getProperty() {
        Property property = repObj.getProperty();
        updateIdocUnit(property);
        return property;
    }

    @Override
    public String getVersion() {
        return repObj.getVersion();
    }

    @Override
    public String getId() {
        return iDocUnit.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.ISubRepositoryObject#getAbstractMetadataObject ()
     */
    @Override
    public AbstractMetadataObject getAbstractMetadataObject() {
        return iDocUnit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.ISubRepositoryObject#removeFromParent()
     */
    @Override
    public void removeFromParent() {
        iDocUnit.getConnection().getIDocs().remove(iDocUnit);
    }

    @Override
    public ERepositoryStatus getRepositoryStatus() {
        return repObj.getRepositoryStatus();
    }

    private void updateIdocUnit(Property property) {
        if (property == null) {
            return;
        }
        Connection connection = null;
        Item item = property.getItem();
        if (item instanceof ConnectionItem) {
            ConnectionItem cItem = (ConnectionItem) item;
            connection = cItem.getConnection();
        }
        if (connection instanceof SAPConnection) {
            SAPConnection sapConnection = (SAPConnection) connection;
            Iterator iterator = sapConnection.getIDocs().iterator();
            while (iterator.hasNext()) {
                Object fObj = iterator.next();
                if (fObj instanceof SAPIDocUnit) {
                    SAPIDocUnit unit = (SAPIDocUnit) fObj;
                    if (iDocUnit.getLabel() != null && iDocUnit.getLabel().equals(unit.getLabel())) {
                        iDocUnit = unit;
                    }
                }
            }
        }

    }

    @Override
    public ModelElement getModelElement() {
        return iDocUnit;
    }

    @Override
    public IRepositoryNode getRepositoryNode() {
        return repObj.getRepositoryNode();
    }

    @Override
    public boolean isDeleted() {
        return this.getProperty().getItem().getState().isDeleted();
    }

}
