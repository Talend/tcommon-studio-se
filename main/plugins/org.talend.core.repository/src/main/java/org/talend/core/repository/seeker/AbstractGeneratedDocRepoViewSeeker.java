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
package org.talend.core.repository.seeker;

import java.util.Collections;
import java.util.List;

import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.nodes.IProjectRepositoryNode;

public abstract class AbstractGeneratedDocRepoViewSeeker extends AbstractRepoViewSeeker {

    @Override
    protected List<ERepositoryObjectType> getValidationTypes() {
        List<ERepositoryObjectType> validationTypes = super.getValidationTypes();
        validationTypes.add(getValidType());
        return validationTypes;
    }

    @Override
    protected List<IRepositoryNode> getRootTypeRepositoryNodes(IProjectRepositoryNode root, ERepositoryObjectType itemType) {
        if (validType(itemType)) {
            return super.getRootTypeRepositoryNodes(root, getProcessType());
        }
        return Collections.emptyList();
    }

    @Override
    protected List<ERepositoryObjectType> getPreExpandTypes() {
        List<ERepositoryObjectType> preExpandTypes = super.getPreExpandTypes();

        preExpandTypes.add(ERepositoryObjectType.DOCUMENTATION);
        preExpandTypes.add(ERepositoryObjectType.getType("GENERATED")); //$NON-NLS-1$
        preExpandTypes.add(getProcessType());
        preExpandTypes.remove(null); // remove all null
        return preExpandTypes;
    }

    protected abstract ERepositoryObjectType getValidType();

    protected abstract ERepositoryObjectType getProcessType();

}
