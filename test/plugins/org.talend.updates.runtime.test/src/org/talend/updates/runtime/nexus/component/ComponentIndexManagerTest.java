// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.updates.runtime.nexus.component;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.commons.utils.resource.BundleFileUtil;
import org.talend.updates.runtime.engine.P2InstallerTest;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ComponentIndexManagerTest {

    public static final String PATH_640_INDEX_FILE = "resources/components/index-6.4.0.xml"; //$NON-NLS-1$

    File tmpFolder = null;

    @Before
    public void prepare() throws Exception {
        tmpFolder = org.talend.utils.files.FileUtils.createTmpFolder("test", "index"); //$NON-NLS-1$  //$NON-NLS-2$
    }

    @After
    public void clean() {
        if (tmpFolder != null) {
            FilesUtils.deleteFolder(tmpFolder, true);
        }
    }

    @Test
    public void test_create_null() {
        final ComponentIndexBean indexBean = new ComponentIndexManager().create(null);
        Assert.assertNull(indexBean);
    }

    @Test
    public void test_create_notExisted() {
        final ComponentIndexBean indexBean = new ComponentIndexManager().create(new File("aaaaa"));
        Assert.assertNull(indexBean);
    }

    @Test
    public void test_create_dir() {
        final ComponentIndexBean indexBean = new ComponentIndexManager().create(tmpFolder);
        Assert.assertNull(indexBean);
    }

    @Test
    public void test_create_notZip() throws IOException {
        File txtFile = new File(tmpFolder, "abc.txt");
        txtFile.createNewFile();
        final ComponentIndexBean indexBean = new ComponentIndexManager().create(txtFile);
        Assert.assertNull(indexBean);
    }

    @Test
    public void test_create_wrongZip() throws IOException {
        final File compFile = BundleFileUtil.getBundleFile(this.getClass(), "resources/content.zip");
        Assert.assertNotNull(compFile);
        Assert.assertTrue(compFile.exists());

        final ComponentIndexBean indexBean = new ComponentIndexManager().create(compFile);
        Assert.assertNull(indexBean);
    }

    @Test
    public void test_create() throws IOException {
        final File compFile = BundleFileUtil.getBundleFile(this.getClass(), P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(compFile);
        Assert.assertTrue(compFile.exists());

        final ComponentIndexBean indexBean = new ComponentIndexManager().create(compFile);
        Assert.assertNotNull(indexBean);
        Assert.assertEquals("myJira", indexBean.getName());
        Assert.assertEquals("org.talend.components.myjira", indexBean.getBundleId());
        Assert.assertEquals("0.16.0.SNAPSHOT", indexBean.getVersion());
        Assert.assertEquals("mvn:org.talend.components/components-myjira/0.16.0.SNAPSHOT/zip", indexBean.getMvnURI());
    }
}
