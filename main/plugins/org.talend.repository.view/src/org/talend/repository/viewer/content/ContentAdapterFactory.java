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
package org.talend.repository.viewer.content;

import org.eclipse.core.runtime.IAdapterFactory;
import org.talend.core.repository.model.ProjectRepositoryNode;
import org.talend.repository.navigator.TalendRepositoryRoot;

public class ContentAdapterFactory implements IAdapterFactory {

    @Override
    public Object getAdapter(Object adaptableObject, Class adapterType) {
        if (adapterType == ProjectRepositoryNode.class) {
            if (adaptableObject instanceof TalendRepositoryRoot) {
                return ProjectRepositoryNode.getInstance();// this is of type ProjectRepositoryNode
            }// else return null
        }// else return null
        return null;
    }

    @Override
    public Class[] getAdapterList() {
        return new Class[] { ProjectRepositoryNode.class };
    }

}
