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
package org.talend.repository;

import org.talend.core.model.repository.IRepositoryViewObject;

/**
 * A repository element delta describes changes in repository element between two discrete points in time. Given a
 * delta, clients can access the element that has changed, and any children that have changed.
 * 
 * $Id: IRepositoryElementDelta.java 2007-1-4涓嬪?�3:40:26 bqian $
 * 
 */
public interface IRepositoryElementDelta {

    public IRepositoryViewObject getRepositoryObject();

}
