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
package org.talend.core.repository.model;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.ProjectManager;

/**
 * created by ggu on Jul 9, 2014 Detailled comment
 *
 */
@SuppressWarnings("nls")
public class ProxyRepositoryFactoryTest {

    ProxyRepositoryFactory repFactory;

    @Before
    public void setUp() {
        repFactory = ProxyRepositoryFactory.getInstance();
    }

    /**
     * 
     * for TDI-29841.
     */
    @Test
    public void testGetFolderItem4LowerCase() {
        // make uniquely for folder. Else, if the folder existed, will be some problem for test.
        final long timeStamp = System.currentTimeMillis();
        final String prePath = String.valueOf(timeStamp) + '/';

        // case 1
        IPath path1 = new Path(prePath + "DATA_SOURCES/Gini/Extract/v2_IPER/v1_temp_solution");
        IPath newPath1 = getNewPath(path1);
        // if first time, will be same always. Except there is one same folder to be existed before,
        Assert.assertEquals(path1, newPath1);

        // case 2
        IPath path2 = new Path(prePath + "DATA_SOURCES/GIni/Extract/v2_IPER/v1_temp_solution");
        IPath newPath2 = getNewPath(path2);
        Assert.assertEquals(path1, newPath2); // will always get the path1, because it have existe now.

        // case 3
        IPath path3 = new Path(prePath + "DATA_SOURCES/GINI/Extract/v2_IPER/v1_temp_solution");
        IPath newPath3 = getNewPath(path3);
        Assert.assertEquals(path1, newPath3);

        // case 4, different path.
        IPath path4 = new Path(prePath + "DATA_SOURCES/Gini/Extract/v2_iper/v1_temp_solution");
        IPath newPath4 = getNewPath(path4);
        Assert.assertEquals(path1, newPath4);

        // case 5, different path.
        IPath path5 = new Path(prePath + "DATA_SOURCES/gini/Extract/v2_iper/v1_temp_solution");
        IPath newPath5 = getNewPath(path5);
        Assert.assertEquals(path1, newPath5);

        // case 6, has same parent folder.
        IPath path6 = new Path(prePath + "data_SOURCES/gini/Extract/v2_iper/v1_temp_solution" + "/test");
        IPath newPath6 = getNewPath(path6);
        IPath expectedPath6 = new Path(prePath + "DATA_SOURCES/Gini/Extract/v2_IPER/v1_temp_solution" + "/test");
        // the parent folder must be same as the value of the first case (case 1).
        Assert.assertEquals(expectedPath6, newPath6);

        // case 7, upper case
        IPath path7 = new Path(prePath + "data_SOURCES/gini/Extract/v2_iper/v1_temp_solution" + "/TeSt");
        IPath newPath7 = getNewPath(path7);
        // the parent folder is same as the value of the case 6.
        Assert.assertEquals(expectedPath6, newPath7);
    }

    /**
     * 
     * for TDI-29841.
     */
    @Test
    public void testGetFolderItem4UpperCase() {

        // make uniquely for folder. Else, if the folder existed, will be some problem for test.
        final long timeStamp = System.currentTimeMillis();
        final String prePath = String.valueOf(timeStamp) + '/';

        // case 1
        IPath path1 = new Path(prePath + "DATA_SOURCES/GINI/Extract/v2_IPER/v1_temp_solution");
        IPath newPath1 = getNewPath(path1);
        // if first time, will be same always. Except there is one same folder to be existed before,
        Assert.assertEquals(path1, newPath1);

        // case 2
        IPath path2 = new Path(prePath + "DATA_SOURCES/GIni/Extract/v2_IPER/v1_temp_solution");
        IPath newPath2 = getNewPath(path2);
        Assert.assertEquals(path1, newPath2); // will always get the path1, because it have existe now.

        // case 3
        IPath path3 = new Path(prePath + "DATA_SOURCES/gini/Extract/v2_IPER/v1_temp_solution");
        IPath newPath3 = getNewPath(path3);
        Assert.assertEquals(path1, newPath3);

        // case 4, different path.
        IPath path4 = new Path(prePath + "DATA_SOURCES/Gini/Extract/v2_iper/v1_temp_solution");
        IPath newPath4 = getNewPath(path4);
        Assert.assertEquals(path1, newPath4);

        // case 5, different path.
        IPath path5 = new Path(prePath + "DATA_SOURCES/gini/Extract/v2_iper/v1_temp_solution");
        IPath newPath5 = getNewPath(path5);
        Assert.assertEquals(path1, newPath5);

        // case 6, has same parent folder.
        IPath path6 = new Path(prePath + "data_SOURCES/gini/Extract/v2_iper/v1_temp_solution" + "/test");
        IPath newPath6 = getNewPath(path6);
        IPath expectedPath6 = new Path(prePath + "DATA_SOURCES/GINI/Extract/v2_IPER/v1_temp_solution" + "/test");
        // the parent folder must be same as the value of the first case (case 1).
        Assert.assertEquals(expectedPath6, newPath6);

        // case 7, upper case
        IPath path7 = new Path(prePath + "data_SOURCES/gini/Extract/v2_iper/v1_temp_solution" + "/TeSt");
        IPath newPath7 = getNewPath(path7);
        // the parent folder is same as the value of the case 6.
        Assert.assertEquals(expectedPath6, newPath7);

    }

    private IPath getNewPath(IPath path) {
        FolderItem folderItem = repFactory.getFolderItem(ProjectManager.getInstance().getCurrentProject(),
                ERepositoryObjectType.PROCESS, path);
        Assert.assertNotNull(folderItem);
        return new Path(folderItem.getState().getPath()).append(folderItem.getProperty().getLabel());

    }
}
