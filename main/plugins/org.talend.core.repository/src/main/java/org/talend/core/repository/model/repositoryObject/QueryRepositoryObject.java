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
import java.util.Iterator;
import java.util.List;

import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.core.model.metadata.builder.connection.AbstractMetadataObject;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.QueriesConnection;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.User;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.ISubRepositoryObject;
import org.talend.cwm.helper.SubItemHelper;
import org.talend.repository.model.IRepositoryNode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class QueryRepositoryObject extends org.talend.core.model.metadata.Query implements ISubRepositoryObject {

    private final IRepositoryViewObject repObj;

    private Query query;

    public QueryRepositoryObject(IRepositoryViewObject repObj, Query table) {
        this.repObj = repObj;
        this.query = table;
    }

    public IRepositoryViewObject getViewObject() {
        return this.repObj;
    }

    @Override
    public Property getProperty() {
        Property property = repObj.getProperty();
        updateQuery(property);
        return property;
    }

    @Override
    public String getVersion() {
        return repObj.getVersion();
    }

    @Override
    public String getLabel() {
        return query.getLabel();
    }

    @Override
    public String getId() {
        return query.getId();
    }

    public Query getQuery() {
        return query;
    }

    @Override
    public AbstractMetadataObject getAbstractMetadataObject() {
        return getQuery();
    }

    @Override
    public void removeFromParent() {
        query.getQueries().getQuery().remove(query);
    }

    private void updateQuery(Property property) {
        if (property == null) {
            return;
        }
        Connection connection = null;
        Item item = property.getItem();
        if (item instanceof ConnectionItem) {
            ConnectionItem cItem = (ConnectionItem) item;
            connection = cItem.getConnection();
        }
        if (connection instanceof DatabaseConnection) {
            DatabaseConnection dbConn = (DatabaseConnection) connection;
            QueriesConnection queries = dbConn.getQueries();
            Iterator iterator = queries.getQuery().iterator();
            while (iterator.hasNext()) {
                Object next = iterator.next();
                if (next instanceof Query && query.getLabel() != null && query.getLabel().equals(((Query) next).getLabel())) {
                    query = (Query) next;
                }
            }
        }

    }

    @Override
    public User getAuthor() {
        return repObj.getAuthor();
    }

    @Override
    public List<IRepositoryViewObject> getChildren() {
        return repObj.getChildren();
    }

    @Override
    public Date getCreationDate() {
        return repObj.getCreationDate();
    }

    @Override
    public String getDescription() {
        return repObj.getDescription();
    }

    @Override
    public ERepositoryStatus getInformationStatus() {
        return repObj.getInformationStatus();
    }

    @Override
    public Date getModificationDate() {
        return repObj.getModificationDate();
    }

    @Override
    public String getPath() {
        return repObj.getPath();
    }

    @Override
    public String getProjectLabel() {
        return repObj.getProjectLabel();
    }

    @Override
    public String getPurpose() {
        return repObj.getPurpose();
    }

    @Override
    public IRepositoryNode getRepositoryNode() {
        return repObj.getRepositoryNode();
    }

    @Override
    public ERepositoryStatus getRepositoryStatus() {
        return repObj.getRepositoryStatus();
    }

    @Override
    public String getStatusCode() {
        return repObj.getStatusCode();
    }

    @Override
    public ERepositoryObjectType getRepositoryObjectType() {
        return ERepositoryObjectType.METADATA_CON_QUERY;
    }

    @Override
    public boolean isDeleted() {
        return SubItemHelper.isDeleted(query);
    }

    @Override
    public void setRepositoryNode(IRepositoryNode node) {
        repObj.setRepositoryNode(node);
    }

    @Override
    public ModelElement getModelElement() {
        return this.query;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryViewObject#isModified()
     */
    @Override
    public boolean isModified() {
        return repObj.isModified();
    }

}
