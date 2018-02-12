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

import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.helper.ByteArrayResource;
import org.talend.core.repository.constants.FileConstants;
import org.talend.repository.items.importexport.handlers.model.ImportItem;
import org.talend.repository.items.importexport.manager.ResourcesManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
@SuppressWarnings("nls")
public class HandlerUtilTest {

    private IPath propPath, itemPath;

    @Before
    public void initialize() {
        propPath = new Path("TEST/process/test_0.1.properties");
        itemPath = new Path("TEST/process/test_0.1.item");
    }

    @After
    public void clean() {
        propPath = null;
        itemPath = null;
    }

    @Test
    public void testGetURI() {
        Assert.assertEquals(URI.createURI("test_0.1.properties"), HandlerUtil.getURI(propPath));

        IPath path = new Path("test_0.1.properties");
        Assert.assertEquals(URI.createURI("test_0.1.properties"), HandlerUtil.getURI(path));
    }

    @Test
    public void testGetItemPath() {
        Item item = mock(Item.class);
        when(item.isNeedVersion()).thenReturn(true);
        Assert.assertEquals(itemPath, HandlerUtil.getItemPath(propPath, item));

        when(item.getFileExtension()).thenReturn(FileConstants.ITEM_EXTENSION);
        Assert.assertEquals(itemPath, HandlerUtil.getItemPath(propPath, item));

        when(item.getFileExtension()).thenReturn(FileConstants.SCREENSHOT_EXTENSION);
        Assert.assertEquals(new Path("TEST/process/test_0.1.screenshot"), HandlerUtil.getItemPath(propPath, item));

    }

    @Test
    public void testGetReferenceItemPath() {
        Assert.assertEquals(itemPath, HandlerUtil.getReferenceItemPath(propPath, FileConstants.ITEM_EXTENSION));

        IPath wsdlPath = new Path("TEST/process/test_0.1.wsdl");
        Assert.assertEquals(wsdlPath, HandlerUtil.getReferenceItemPath(propPath, "wsdl"));
    }

    @Test
    public void testGetValidProjectFilePath() {
        ResourcesManager resManager = mock(ResourcesManager.class);

        Set<IPath> pathes = new HashSet<IPath>();
        IPath projPath = new Path("TEST/" + FileConstants.LOCAL_PROJECT_FILENAME);
        pathes.add(projPath);
        pathes.add(propPath);
        pathes.add(itemPath);
        when(resManager.getPaths()).thenReturn(pathes);

        Assert.assertEquals(projPath, HandlerUtil.getValidProjectFilePath(resManager, itemPath));
        Assert.assertEquals(projPath, HandlerUtil.getValidProjectFilePath(resManager, propPath));
    }

    @Test
    public void testGetValidItemRelativePath() {
        ResourcesManager resManager = mock(ResourcesManager.class);
        Set<IPath> pathes = new HashSet<IPath>();
        IPath projPath = new Path("TEST/" + FileConstants.LOCAL_PROJECT_FILENAME);
        IPath itemRelativePath = new Path("process/test_0.1.item");
        pathes.add(projPath);
        when(resManager.getPaths()).thenReturn(pathes);
        Assert.assertEquals(itemRelativePath, HandlerUtil.getValidItemRelativePath(resManager, itemPath));
    }

    @Test
    public void testCreateResource4Null() {
        Assert.assertNull(HandlerUtil.createResource(null, null, false));
        Assert.assertNull(HandlerUtil.createResource(null, propPath, false));
    }

    @Test
    public void testCreateResource() {
        ImportItem importItem = new ImportItem(propPath);
        Assert.assertEquals(propPath, importItem.getPath());

        Resource res = HandlerUtil.createResource(importItem, propPath, false);
        Assert.assertNotNull(res);
        Assert.assertEquals(propPath.lastSegment(), res.getURI().toString());
        Assert.assertEquals(importItem.getResourceSet(), res.getResourceSet());
        Assert.assertEquals(1, importItem.getResourceSet().getResources().size());
        //
    }

    @Test
    public void testCreateResource4ByteArray() {
        ImportItem importItem = new ImportItem(itemPath);
        Assert.assertEquals(itemPath, importItem.getPath());

        Resource res = HandlerUtil.createResource(importItem, itemPath, true);
        Assert.assertNotNull(res);
        Assert.assertTrue(res instanceof ByteArrayResource);
        Assert.assertEquals(itemPath.lastSegment(), res.getURI().path());
        Assert.assertEquals(importItem.getResourceSet(), res.getResourceSet());
        Assert.assertEquals(1, importItem.getResourceSet().getResources().size());
        //
    }
}
