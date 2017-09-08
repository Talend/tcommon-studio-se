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
package org.talend.metadata.managment.hive.handler;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.MetadataConnection;

/**
 * created by qiongli on 2014-3-11 Detailled comment
 * 
 */
public class CDH5YarnHandlerTest {

    /**
     * DOC qiongli Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * DOC qiongli Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link org.talend.metadata.managment.hive.handler.CDH5YarnHandler#getDefaultHadoopParameters()}.
     */
    @Test
    public void testGetDefaultHadoopParameters() {
        IMetadataConnection metadataConnection = new MetadataConnection();
        CDH5YarnHandler cdh5Handler = new CDH5YarnHandler(metadataConnection);
        Map<String, String> defaultHadoopParameters = cdh5Handler.getDefaultHadoopParameters();
        assertEquals(defaultHadoopParameters.get(CDH5YarnHandler.YARN_MB), CDH5YarnHandler.YARN_MB_VALUE);
        assertEquals(defaultHadoopParameters.get(CDH5YarnHandler.YARN_CLASSPATH), CDH5YarnHandler.YARN_CLASSPATH_VALUE);
        assertEquals(defaultHadoopParameters.get("mapreduce.map.memory.mb"), "1000"); //$NON-NLS-1$//$NON-NLS-2$
        assertEquals(defaultHadoopParameters.get("mapreduce.reduce.memory.mb"), "1000"); //$NON-NLS-1$//$NON-NLS-2$
    }

}
