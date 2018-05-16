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

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.talend.core.model.properties.Property;

/**
 * created by wchen on 2013-8-22 Detailled comment
 * 
 */
public interface IExtendedRepositoryNodeHandler {

    /**
     * 
     * DOC wchen Comment method "getProperty".
     * 
     * @param repository nodeObject
     * @return property of this repository object
     */
    public Property getProperty(Object nodeObject);

    public Object getParent(Object repositoryNode);

    /**
     * 
     * DOC get ERepositoryObjectType for repository selection
     * 
     * @param repository selected object
     * @return
     */
    public ERepositoryObjectType getObjectType(Object repositoryNode);

    /**
     * 
     * DOC get root ERepositoryObjectType for repository selection
     * 
     * @param repository selected object
     * @return
     */
    public ERepositoryObjectType getRootType(Object repositoryNode);

    /**
     * 
     * DOC collect all Hierarchical Mapper related resources,includes mapper repositoryObject resouece and other related
     * structures,namespace... resoueces
     * 
     * @param mapper repositoryObject
     * @return
     */
    public List<IResource> getRepositoryNodeAndDependencies(IRepositoryViewObject repositoryObject);

    /**
     * 
     * DOC collect mapper referenced RepositoryObject
     * 
     * @param mapper repositoryObject
     * @return
     */
    public List<IRepositoryViewObject> getRepositoryObjectDependencies(IRepositoryViewObject repositoryObject);

}
