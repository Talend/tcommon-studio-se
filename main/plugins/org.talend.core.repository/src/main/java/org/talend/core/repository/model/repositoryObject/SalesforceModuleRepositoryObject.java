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

import org.eclipse.emf.common.util.EList;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.core.model.metadata.builder.connection.AbstractMetadataObject;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.SalesforceModuleUnit;
import org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.ISubRepositoryObject;
import org.talend.core.model.repository.RepositoryObject;
import org.talend.cwm.helper.SubItemHelper;
import org.talend.repository.model.RepositoryNode;

import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class SalesforceModuleRepositoryObject extends RepositoryObject implements ISubRepositoryObject {

    private SalesforceModuleUnit moduleUnit;

    private final IRepositoryViewObject repObj;

    public SalesforceModuleRepositoryObject(IRepositoryViewObject repObj, RepositoryNode moduleNode,
            final SalesforceModuleUnit moduleUnit) {
        this.repObj = repObj;
        this.moduleUnit = moduleUnit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.RepositoryObject#getAdapter(java.lang .Class)
     */
    public Object getAdapter(Class adapter) {
        if (adapter == org.talend.core.model.metadata.builder.connection.MetadataTable.class) {
            return moduleUnit.getMetadataTable();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#getType()
     */
    @Override
    public ERepositoryObjectType getRepositoryObjectType() {
        return ERepositoryObjectType.METADATA_SALESFORCE_MODULE;
    }

    @Override
    public void setLabel(String value) {
        if (moduleUnit.getLabel() == null) {
            moduleUnit.setLabel(value);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.RepositoryObject#getLabel()
     */
    @Override
    public String getLabel() {
        return moduleUnit.getLabel();
    }

    @Override
    public boolean isDeleted() {
        final AbstractMetadataObject abstractMetadataObject = getAbstractMetadataObject();
        if (abstractMetadataObject != null) {
            return SubItemHelper.isDeleted(abstractMetadataObject);
        }
        return false;
    }

    @Override
    public Property getProperty() {
        Property property = repObj.getProperty();
        updateModuleUnit(property);
        return property;
    }

    @Override
    public String getVersion() {
        return repObj.getVersion();
    }

    @Override
    public String getId() {
        return moduleUnit.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.ISubRepositoryObject#getAbstractMetadataObject ()
     */
    @Override
    public AbstractMetadataObject getAbstractMetadataObject() {
        return this.moduleUnit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.ISubRepositoryObject#removeFromParent()
     */
    @Override
    public void removeFromParent() {
        moduleUnit.getConnection().getModules().remove(moduleUnit);
    }

    @Override
    public ERepositoryStatus getRepositoryStatus() {
        return repObj.getRepositoryStatus();
    }

    private void updateModuleUnit(Property property) {
        if (property == null) {
            return;
        }
        Connection connection = null;
        Item item = property.getItem();
        if (item instanceof ConnectionItem) {
            ConnectionItem cItem = (ConnectionItem) item;
            connection = cItem.getConnection();
        }
        if (connection instanceof SalesforceSchemaConnection) {
            SalesforceSchemaConnection salesforceConnection = (SalesforceSchemaConnection) connection;
            if (salesforceConnection.getModules() != null) {
                Iterator iterator = salesforceConnection.getModules().iterator();
                while (iterator.hasNext()) {
                    Object fObj = iterator.next();
                    if (fObj instanceof SalesforceModuleUnit) {
                        SalesforceModuleUnit unit = (SalesforceModuleUnit) fObj;
                        if (moduleUnit.getLabel() != null && moduleUnit.getLabel().equals(unit.getLabel())) {
                            moduleUnit = unit;
                        }
                    }
                }
            }
        }

    }

    @Override
    public ModelElement getModelElement() {
        return this.moduleUnit;
    }

    public MetadataTable getDefaultTable() {
        if (this.moduleUnit != null) {
            EList<MetadataTable> tables = moduleUnit.getTables();
            for (MetadataTable table : tables) {
                if (table.getLabel().equals(moduleUnit.getLabel())) {
                    return table;
                }
            }
            return tables.get(0);
        }
        return null;

    }

}
