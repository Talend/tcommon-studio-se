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
package org.talend.core.model.repository;

import java.util.Date;
import java.util.List;

import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.User;
import org.talend.repository.model.IRepositoryNode;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public interface IRepositoryViewObject {

    /**
     * Returns the process id. Returns -1 if process id is not set or cannot be retrieve.
     * 
     * @return the process id
     */
    public String getId();

    /**
     * Returns the process label. Returns null if process label is not set or cannot be retrieve.
     * 
     * @return the process id
     */
    public String getLabel();

    /**
     * Returns the process version. Returns null if process id is not set or cannot be retrieve.
     * 
     * @return the process version
     */
    public String getVersion();

    public User getAuthor();

    public String getStatusCode();

    public Date getCreationDate();

    public String getDescription();

    public Date getModificationDate();

    public String getPurpose();

    public ERepositoryObjectType getRepositoryObjectType();

    public List<IRepositoryViewObject> getChildren();

    public void setRepositoryNode(IRepositoryNode node);

    public IRepositoryNode getRepositoryNode();

    public Property getProperty();

    public boolean isDeleted();

    public String getProjectLabel();

    public String getPath();

    public ERepositoryStatus getRepositoryStatus();

    public ERepositoryStatus getInformationStatus();

    /**
     * DOC nrousseau Comment method "isModified".
     * 
     * @return
     */
    public boolean isModified();

}
