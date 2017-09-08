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
package org.talend.core.model.metadata.builder.database.manager.dbs;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.talend.core.database.EDatabaseTypeName;

/**
 * created by ggu on Jul 4, 2012 Detailled comment
 * 
 */
public class MSSQLExtractManagerTest extends AbstractTest4ExtractManager {

    @Before
    public void setUp() throws Exception {
        init(EDatabaseTypeName.MSSQL);
    }

    /**
     * 
     * DOC ggu Comment method "testGetTableNameBySynonyms".
     * 
     * @throws Exception
     */
    @Override
    @Test
    @Ignore
    public void testGetTableNameBySynonyms() throws Exception {
        PTODO();
    }

}
