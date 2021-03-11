// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;

/*
 * Created by bhe on Mar 30, 2020
 */
public class RepositoryNodeTest {

    @Test
    public void testEquals() throws Exception {
        RepositoryNode parent = new RepositoryNode(null, null, ENodeType.STABLE_SYSTEM_FOLDER);
        parent.setProperties(EProperties.LABEL, "system");
        parent.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.PROCESS);
        parent.setType(ENodeType.STABLE_SYSTEM_FOLDER);

        assertNotNull(parent);

        RepositoryNode parentNew = new RepositoryNode(null, null, ENodeType.STABLE_SYSTEM_FOLDER);
        parentNew.setProperties(EProperties.LABEL, "system");
        parentNew.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.PROCESS);
        parentNew.setType(ENodeType.STABLE_SYSTEM_FOLDER);

        assertEquals(parent, parentNew);
        assertEquals(parent.hashCode(), parentNew.hashCode());

        RepositoryNode son = new RepositoryNode(null, null, ENodeType.STABLE_SYSTEM_FOLDER);
        son.setProperties(EProperties.LABEL, ERepositoryObjectType.PROCESS);
        son.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.PROCESS);

        assertNotNull(son);

        assertFalse(parent.equals(son));

    }
}
