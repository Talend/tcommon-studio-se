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
package org.talend.repository.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.repository.utils.ConvertJobsUtil;
import org.talend.core.repository.utils.ConvertJobsUtil.JobType;

/**
 *
 * DOC hwang class global comment. Detailled comment
 */
public class ConvertJobsUtilTest {

    @Test
    public void testGetTestCasePath() {
        // mock
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        Item item = PropertiesFactory.eINSTANCE.createProcessItem();
        String id = EcoreUtil.generateUUID();
        property.setId(id);
        item.setProperty(property);
        item.getProperty();
        //
        IPath path1 =  ConvertJobsUtil.getTestCasePath(item, JobType.STANDARD.getDisplayName());
        assertTrue(path1.equals(new Path(JobType.STANDARD.getERepositoryObjectType().getFolder()+File.separator+id)));

        IPath path2 =  ConvertJobsUtil.getTestCasePath(item, JobType.BIGDATABATCH.getDisplayName());
        assertTrue(path2.equals(new Path(JobType.BIGDATABATCH.getERepositoryObjectType().getFolder()+File.separator+id)));

        IPath path3 =  ConvertJobsUtil.getTestCasePath(item, JobType.BIGDATASTREAMING.getDisplayName());
        assertTrue(path3.equals(new Path(JobType.BIGDATASTREAMING.getERepositoryObjectType().getFolder()+File.separator+id)));

        IPath path4 =  ConvertJobsUtil.getTestCasePath(item, JobType.STANDARD.getDisplayName());
        assertTrue(path4.equals(new Path(JobType.STANDARD.getERepositoryObjectType().getFolder()+File.separator+id)));
    }

    @Test
    public void testGetCleanFrameworkName() {
        assertEquals(null, ConvertJobsUtil.getCleanFrameworkName(null));
        assertEquals("Spark", ConvertJobsUtil.getCleanFrameworkName("Spark (Deprecated)"));
        assertEquals("Spark", ConvertJobsUtil.getCleanFrameworkName("Spark"));
    }
    
    @Test
    public void testgetTestCaseJobTypeByPath() {
        // mock
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        Item item = PropertiesFactory.eINSTANCE.createProcessItem();
        String id = EcoreUtil.generateUUID();
        property.setId(id);
        item.setProperty(property);
        item.getProperty();
        IPath path1 =  ConvertJobsUtil.getTestCasePath(item, JobType.STANDARD.getDisplayName());
        assertEquals(JobType.STANDARD.getDisplayName(), ConvertJobsUtil.getTestCaseJobTypeByPath(path1.toOSString()));
        
        path1 =  ConvertJobsUtil.getTestCasePath(item, JobType.BIGDATABATCH.getDisplayName());
        assertEquals(JobType.BIGDATABATCH.getDisplayName(), ConvertJobsUtil.getTestCaseJobTypeByPath(path1.toOSString()));
    
        path1 =  ConvertJobsUtil.getTestCasePath(item, JobType.BIGDATASTREAMING.getDisplayName());
        assertEquals(JobType.BIGDATASTREAMING.getDisplayName(), ConvertJobsUtil.getTestCaseJobTypeByPath(path1.toOSString()));
    }
}
