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
package org.talend.repository.items.importexport.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.repository.items.importexport.handlers.model.ImportItem;
import org.talend.repository.items.importexport.manager.ResourcesManager;

/**
 * created by wchen on Jul 21, 2017 Detailled comment
 *
 */
public class ImportExportHandlersManagerTest {

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
    public void testPopulateImportingItemsForEmptyFolders() throws Exception {
        ImportExportHandlersManager manager = new ImportExportHandlersManager();
        ResourcesManager resourceManager = new TestFileManager();
        resourceManager.addFolder("MyProject/process/normal_folder");
        List<ImportItem> populateImportingItems = manager.populateImportingItems(resourceManager, false, null);
        Assert.assertEquals(populateImportingItems.size(), 1);

        resourceManager = new TestFileManager();
        resourceManager.addFolder("MyProject");
        populateImportingItems = manager.populateImportingItems(resourceManager, false, null);
        Assert.assertEquals(populateImportingItems.size(), 0);

        resourceManager = new TestFileManager();
        resourceManager.addFolder("MyProject/abcd");
        populateImportingItems = manager.populateImportingItems(resourceManager, false, null);
        Assert.assertEquals(populateImportingItems.size(), 0);

        resourceManager = new TestFileManager();
        resourceManager.addFolder("MyProject/process/.svn");
        populateImportingItems = manager.populateImportingItems(resourceManager, false, null);
        Assert.assertEquals(populateImportingItems.size(), 0);

        resourceManager = new TestFileManager();
        resourceManager.addFolder("MyProject/process/.svn/prop-base");
        populateImportingItems = manager.populateImportingItems(resourceManager, false, null);
        Assert.assertEquals(populateImportingItems.size(), 0);

        resourceManager = new TestFileManager();
        resourceManager.addFolder("MyProject/process/.test");
        populateImportingItems = manager.populateImportingItems(resourceManager, false, null);
        Assert.assertEquals(populateImportingItems.size(), 0);

        resourceManager = new TestFileManager();
        resourceManager.addFolder("MyProject/process/001");
        populateImportingItems = manager.populateImportingItems(resourceManager, false, null);
        Assert.assertEquals(populateImportingItems.size(), 0);

    }

    class TestFileManager extends ResourcesManager {

        public TestFileManager() {
        }

        @Override
        public InputStream getStream(IPath path, ImportItem importItem) throws IOException {
            return null;
        }

        @Override
        public boolean collectPath2Object(Object root) {
            return false;
        }

        @Override
        public void closeResource() {
        }

        @Override
        public Object getRoot() {
            return null;
        };

    }
}
