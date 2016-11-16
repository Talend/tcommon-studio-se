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
package org.talend.repository.metadata.seeker;

import java.util.List;

import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.seeker.AbstractRepoViewSeeker;

public abstract class AbstractMetadataRepoViewSeeker extends AbstractRepoViewSeeker {

    @Override
    protected List<ERepositoryObjectType> getPreExpandTypes() {
        List<ERepositoryObjectType> preExpandTypes = super.getPreExpandTypes();
        preExpandTypes.add(ERepositoryObjectType.METADATA);
        return preExpandTypes;
    }
}
