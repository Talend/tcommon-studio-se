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
package org.talend.repository.items.importexport.handlers;

import static org.mockito.Mockito.*;

import java.io.InputStream;

import org.eclipse.core.runtime.IPath;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.repository.items.importexport.handlers.model.ImportItem;
import org.talend.repository.items.importexport.manager.ResourcesManager;

/**
 * created by ggu on Apr 28, 2014 Detailled comment
 * 
 */
public class ImportHandlerHelperTest {

    private ImportHandlerHelper helper;

    @Before
    public void setUp() {
        helper = new ImportHandlerHelper();
    }

    @After
    public void clean() {
        helper = null;
    }

    @Test
    public void testComputeImportItem4NullResourcesManager() throws Exception {

        ResourcesManager resManager = mock(ResourcesManager.class);
        Assert.assertNull(helper.computeImportItem(null, resManager, null, false));

    }

    @Test
    public void testComputeImportItem4NullPath() throws Exception {

        IPath path = mock(IPath.class);
        Assert.assertNull(helper.computeImportItem(null, null, path, false));
    }

    @SuppressWarnings("nls")
    @Test
    public void testComputeImportItem4NonPropertiesFile() throws Exception {
        ResourcesManager resManager = mock(ResourcesManager.class);
        IPath path = mock(IPath.class);

        when(path.lastSegment()).thenReturn("test.txt");
        Assert.assertNull(helper.computeImportItem(null, resManager, path, false));

        when(path.lastSegment()).thenReturn("test.abc");
        Assert.assertNull(helper.computeImportItem(null, resManager, path, false));
    }

    // @Test
    public void testComputeImportItem() throws Exception {

        IPath path = mock(IPath.class);
        when(path.lastSegment()).thenReturn("test_01.properties");
        // when(path.removeLastSegments(1)).thenReturn("test_01.properties");
        // PTODO
        ResourcesManager resManager = mock(ResourcesManager.class);
        InputStream inputStream = mock(InputStream.class);
        when(resManager.getStream(path)).thenReturn(inputStream);
        ImportItem importItem = helper.computeImportItem(null, resManager, path, false);
        Assert.assertNull(importItem);

    }
}
