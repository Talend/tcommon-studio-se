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

import java.util.Date;

import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.User;

/**
 * Defines a repository object, an object that can be stored in the repository. <br/>
 * 
 * $Id: IRepositoryObject.java 44053 2010-06-12 09:14:16Z nma $
 * 
 */
public interface IRepositoryObject extends IRepositoryViewObject {

    public void setId(String id);

    public void setLabel(String label);

    public void setVersion(String version);

    public void setAuthor(User author);

    public void setStatusCode(String statusCode);

    /**
     * won't set the date any more, it will be deal with when save file
     */
    @Deprecated
    public void setCreationDate(Date value);

    public void setDescription(String value);

    /**
     * won't set the date any more, it will be deal with when save file
     */
    @Deprecated
    public void setModificationDate(Date value);

    public void setPurpose(String value);

    public Property getProperty();

    public void setProperty(Property property);

}
