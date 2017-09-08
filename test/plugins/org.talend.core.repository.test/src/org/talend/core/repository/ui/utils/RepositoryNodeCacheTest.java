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
package org.talend.core.repository.ui.utils;

import org.junit.Assert;
import org.junit.Test;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.StableRepositoryNode;


/**
 * created by cmeng on Apr 24, 2017
 * Detailled comment
 *
 */
public class RepositoryNodeCacheTest {

    @Test
    public void testCache() {
        StableRepositoryNode container = new StableRepositoryNode(null, "SAP", ECoreImage.FOLDER_CLOSE_ICON); //$NON-NLS-1$
        container.setChildrenObjectType(ERepositoryObjectType.METADATA_CON_TABLE);
        container.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_SAP_BW_INFOCUBE);
        RepositoryNodeCache rnc = new RepositoryNodeCache(container);
        StableRepositoryNode child = new StableRepositoryNode(null, "SAP BW", ECoreImage.FOLDER_CLOSE_ICON); //$NON-NLS-1$
        child.setChildrenObjectType(ERepositoryObjectType.METADATA_CON_TABLE);
        child.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_SAP_BW_INFOCUBE);
        rnc.addChildFolderCache(child);
        Assert.assertTrue(child == rnc.removeChildFolderCache(child));
    }
}
