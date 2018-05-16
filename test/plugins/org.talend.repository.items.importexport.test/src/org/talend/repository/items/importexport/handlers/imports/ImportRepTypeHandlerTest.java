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
package org.talend.repository.items.importexport.handlers.imports;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.constants.FileConstants;
import org.talend.repository.items.importexport.manager.ResourcesManager;

import static org.mockito.Mockito.*;

/**
 * DOC ggu class global comment. Detailled comment
 */
@SuppressWarnings("nls")
public class ImportRepTypeHandlerTest {

    private IPath projPath;

    private IPath processPropPath1, processItemPath1, processItemPath2, processItemPath3, connPropPath, connItemPath;

    private Set<IPath> initPathes;

    @Before
    public void initialize() {
        projPath = new Path("TEST/" + FileConstants.LOCAL_PROJECT_FILENAME);

        processPropPath1 = new Path("TEST/process/test1_0.1.properties");
        processItemPath1 = new Path("TEST/process/test1_0.1.item");
        processItemPath2 = new Path("TEST/process/abc/xyz/test2_0.1.properties");
        processItemPath3 = new Path("TEST/process/abc/xyz/test2_0.1.item");
        connPropPath = new Path("TEST/metadata/connections/abc/xyz/test3_0.1.properties");
        connItemPath = new Path("TEST/metadata/connections/abc/xyz/test3_0.1.item");

        initPathes = new HashSet<IPath>();
        initPathes.add(projPath);
        initPathes.add(processPropPath1);
        initPathes.add(processItemPath1);
        initPathes.add(processItemPath2);
        initPathes.add(processItemPath3);
        initPathes.add(connPropPath);
        initPathes.add(connItemPath);
    }

    @Test
    public void testValid4Process() throws Exception {
        // process
        ImportRepTypeHandler basicHandler = new ImportRepTypeHandler();
        Map<String, String> data = new HashMap<String, String>();

        data.put("type", ERepositoryObjectType.PROCESS.getType());
        basicHandler.setInitializationData(null, null, data);

        ResourcesManager resManager = mock(ResourcesManager.class);
        Set<IPath> pathes = new HashSet<IPath>(initPathes);
        when(resManager.getPaths()).thenReturn(pathes);

        Assert.assertTrue(basicHandler.valid(resManager, processPropPath1));
        Assert.assertFalse(basicHandler.valid(resManager, processItemPath1));

        Assert.assertTrue(basicHandler.valid(resManager, processItemPath2));
        Assert.assertFalse(basicHandler.valid(resManager, processItemPath3));

        Assert.assertFalse(basicHandler.valid(resManager, connPropPath));
        Assert.assertFalse(basicHandler.valid(resManager, connItemPath));

        Assert.assertFalse(basicHandler.valid(resManager, projPath));
    }

    @Test
    public void testValid4Context() throws Exception {
        // for context
        ImportRepTypeHandler basicHandler = new ImportRepTypeHandler();

        Map<String, String> data = new HashMap<String, String>();
        data.put("type", ERepositoryObjectType.CONTEXT.getType());
        basicHandler.setInitializationData(null, null, data);

        ResourcesManager resManager = mock(ResourcesManager.class);
        Set<IPath> pathes = new HashSet<IPath>(initPathes);
        when(resManager.getPaths()).thenReturn(pathes);

        Assert.assertFalse(basicHandler.valid(resManager, processPropPath1));
        Assert.assertFalse(basicHandler.valid(resManager, processItemPath1));
        Assert.assertFalse(basicHandler.valid(resManager, processItemPath2));
        Assert.assertFalse(basicHandler.valid(resManager, processItemPath3));
        Assert.assertFalse(basicHandler.valid(resManager, connPropPath));
        Assert.assertFalse(basicHandler.valid(resManager, connItemPath));

        IPath contextPropPath = new Path("TEST/context/test_0.1.properties");
        IPath contextItemPath = new Path("TEST/context/test_0.1.item");
        pathes.add(contextPropPath);
        pathes.add(contextItemPath);
        Assert.assertTrue(basicHandler.valid(resManager, contextPropPath));
        Assert.assertFalse(basicHandler.valid(resManager, contextItemPath));
    }

    @Test
    public void testValid4Connection() throws Exception {
        // DB connections
        ImportRepTypeHandler basicHandler = new ImportRepTypeHandler();

        Map<String, String> data = new HashMap<String, String>();
        data.put("type", ERepositoryObjectType.PROCESS.getType() + "," + ERepositoryObjectType.METADATA_CONNECTIONS.getType());
        basicHandler.setInitializationData(null, null, data);

        ResourcesManager resManager = mock(ResourcesManager.class);
        Set<IPath> pathes = new HashSet<IPath>(initPathes);
        when(resManager.getPaths()).thenReturn(pathes);

        Assert.assertTrue(basicHandler.valid(resManager, processPropPath1));
        Assert.assertFalse(basicHandler.valid(resManager, processItemPath1));
        Assert.assertTrue(basicHandler.valid(resManager, processItemPath2));
        Assert.assertFalse(basicHandler.valid(resManager, processItemPath3));
        Assert.assertTrue(basicHandler.valid(resManager, connPropPath));
        Assert.assertFalse(basicHandler.valid(resManager, connItemPath));
    }

    @Test
    public void testValid4Unknown() throws Exception {
        ImportRepTypeHandler basicHandler = new ImportRepTypeHandler();

        Map<String, String> data = new HashMap<String, String>();
        data.put("type", "XXX");
        basicHandler.setInitializationData(null, null, data);

        ResourcesManager resManager = mock(ResourcesManager.class);
        Set<IPath> pathes = new HashSet<IPath>(initPathes);
        when(resManager.getPaths()).thenReturn(pathes);

        Assert.assertFalse(basicHandler.valid(resManager, processPropPath1));
        Assert.assertFalse(basicHandler.valid(resManager, processItemPath1));
        Assert.assertFalse(basicHandler.valid(resManager, processItemPath2));
        Assert.assertFalse(basicHandler.valid(resManager, processItemPath3));
        Assert.assertFalse(basicHandler.valid(resManager, connPropPath));
        Assert.assertFalse(basicHandler.valid(resManager, connItemPath));
    }
}
