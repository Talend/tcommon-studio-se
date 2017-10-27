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
package org.talend.core.model.metadata.builder.database.manager;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.talend.core.model.metadata.builder.database.manager.dbs.AS400ExtractManagerTest;
import org.talend.core.model.metadata.builder.database.manager.dbs.AccessExtractManagerTest;
import org.talend.core.model.metadata.builder.database.manager.dbs.ExtractManager4NormalTest;
import org.talend.core.model.metadata.builder.database.manager.dbs.GeneralJDBCExtractManagerTest;
import org.talend.core.model.metadata.builder.database.manager.dbs.HSQLDBExtractManager4InProgressTest;
import org.talend.core.model.metadata.builder.database.manager.dbs.HSQLDBExtractManagerTest;
import org.talend.core.model.metadata.builder.database.manager.dbs.IBMDB2ExtractManagerTest;
import org.talend.core.model.metadata.builder.database.manager.dbs.JavaDBExtractManagerTest;
import org.talend.core.model.metadata.builder.database.manager.dbs.MSSQLExtractManagerTest;
import org.talend.core.model.metadata.builder.database.manager.dbs.OracleExtractManagerTest;
import org.talend.core.model.metadata.builder.database.manager.dbs.SASExtractManagerTest;
import org.talend.core.model.metadata.builder.database.manager.dbs.TeradataExtractManagerTest;

/**
 * created by ggu on Jul 4, 2012 Detailled comment
 * 
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ ExtractManagerFactoryTest.class, AccessExtractManagerTest.class, AS400ExtractManagerTest.class,
        ExtractManager4NormalTest.class, GeneralJDBCExtractManagerTest.class, HSQLDBExtractManagerTest.class,
        HSQLDBExtractManager4InProgressTest.class, IBMDB2ExtractManagerTest.class, JavaDBExtractManagerTest.class,
        MSSQLExtractManagerTest.class, OracleExtractManagerTest.class, SASExtractManagerTest.class,
        TeradataExtractManagerTest.class })
public class ExtractManagerTestSuite {
    /**
     * test suite
     */
}
