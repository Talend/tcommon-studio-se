// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

import java.util.Date;
import java.util.List;

import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.User;
import org.talend.repository.model.IRepositoryNode;

/**
 * Defines a empty repository object. <br/>
 * 
 * @author ftang $Id: EmptyRepositoryObject.java $
 * 
 */
public class EmptyRepositoryObject implements IRepositoryObject {

    public User getAuthor() {
        return null;
    }

    public Date getCreationDate() {
        return null;
    }

    public String getDescription() {
        return ""; //$NON-NLS-1$
    }

    public String getId() {
        return ""; //$NON-NLS-1$
    }

    public String getLabel() {
        return ""; //$NON-NLS-1$
    }

    public Date getModificationDate() {
        return null;
    }

    public Property getProperty() {
        return null;
    }

    public void setProperty(Property property) {
    }

    public String getPurpose() {
        return ""; //$NON-NLS-1$
    }

    public String getStatusCode() {
        return ""; //$NON-NLS-1$
    }

    public ERepositoryObjectType getRepositoryObjectType() {
        return null;
    }

    public String getVersion() {
        return ""; //$NON-NLS-1$
    }

    public void setAuthor(User author) {
    }

    public void setCreationDate(Date value) {
    }

    public void setDescription(String value) {
    }

    public void setId(String id) {
    }

    public void setLabel(String label) {
    }

    public void setModificationDate(Date value) {
    }

    public void setPurpose(String value) {
    }

    public void setStatusCode(String statusCode) {
    }

    public void setVersion(String version) {
    }

    public List<IRepositoryViewObject> getChildren() {
        // TODO Auto-generated method stub
        return null;
    }

    public IRepositoryNode getRepositoryNode() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setRepositoryNode(IRepositoryNode node) {
        // TODO Auto-generated method stub

    }

    public String getProjectLabel() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isDeleted() {
        // TODO Auto-generated method stub
        return false;
    }

    public String getPath() {
        // TODO Auto-generated method stub
        return null;
    }

    public ERepositoryStatus getInformationStatus() {
        // TODO Auto-generated method stub
        return null;
    }

    public ERepositoryStatus getRepositoryStatus() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isModified() {
        // TODO Auto-generated method stub
        return false;
    }

}
