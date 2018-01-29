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
package org.talend.repository.items.importexport.ui.managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.items.importexport.handlers.ImportExportHandlersManager;
import org.talend.repository.items.importexport.handlers.model.EmptyFolderImportItem;
import org.talend.repository.items.importexport.handlers.model.ImportItem;

/**
 * created by wchen on Aug 4, 2016 Detailled comment
 *
 */
public class FileResourcesUnityManagerTest {

    /**
     * DOC wchen Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * DOC wchen Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDoUnify() throws Exception {
        Bundle bundle = Platform.getBundle("org.talend.repository.items.importexport.ui.test");
        URL itemsUrl = bundle.getEntry("resources/empty_folder.zip");
        ImportExportHandlersManager manager = new ImportExportHandlersManager();
        File file = new File(FileLocator.toFileURL(itemsUrl).toURI());
        if (!file.exists()) {
            throw new FileNotFoundException("empty_folder.zip not found");
        }
        FileResourcesUnityManager resManager = new FileResourcesUnityManager(file);
        resManager.doUnify();

        Assert.assertTrue(!resManager.getEmptyFolders().isEmpty());

        List<ImportItem> importItems = manager.populateImportingItems(resManager, true, new NullProgressMonitor());

        Assert.assertEquals(importItems.size(), 5);
        for (int i = 0; i < 5; i++) {
            ImportItem item = importItems.get(i);
            Assert.assertEquals(item.getRepositoryType(), ERepositoryObjectType.PROCESS);
            switch (i) {
            case 0:
                Assert.assertEquals(item.getClass().getSimpleName(), ImportItem.class.getSimpleName());
                break;
            case 1:
            case 2:
            case 3:
            case 4:
                Assert.assertEquals(item.getClass().getSimpleName(), EmptyFolderImportItem.class.getSimpleName());
                break;

            default:
                break;
            }
        }
    }

}
