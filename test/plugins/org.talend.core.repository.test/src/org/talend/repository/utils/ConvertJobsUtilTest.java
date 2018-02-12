// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.RepositoryViewObject;
import org.talend.core.repository.ui.actions.MoveObjectAction;
import org.talend.core.repository.utils.ConvertJobsUtil;
import org.talend.core.repository.utils.ConvertJobsUtil.JobType;
import org.talend.repository.model.RepositoryNode;

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
}
