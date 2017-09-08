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

import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.repository.IRepositoryElementDelta;

/**
 * DOC bqian concrete class of interface IRepositoryElementDelta <br/>
 * 
 * $Id: RepositoryElementDelta.java 2007-1-4下午04:12:08 bqian $
 * 
 */
public class RepositoryElementDelta implements IRepositoryElementDelta {

    private IRepositoryViewObject repositoryObject;

    /**
     * DOC bqian RepositoryElementDelta constructor comment.
     */
    public RepositoryElementDelta(IRepositoryViewObject repositoryObject) {
        this.repositoryObject = repositoryObject;
    }

    public IRepositoryViewObject getRepositoryObject() {
        return repositoryObject;
    }
}
