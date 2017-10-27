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
package org.talend.core.model.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;

/**
 * DOC PLV class global comment. Detailled comment
 */
public class MetadataColumnTest {

    @Test
    public void testMetadataColumn() {
        MetadataColumn column = ConnectionFactory.eINSTANCE.createMetadataColumn();
        column.setLabel("a");
        column.setName("b");
        assertEquals("b", column.getName());
        assertEquals("a", column.getLabel());
        column.setName("b");
        column.setLabel("a");
        assertNotSame(column.getLabel(), column.getName());
    }

}
