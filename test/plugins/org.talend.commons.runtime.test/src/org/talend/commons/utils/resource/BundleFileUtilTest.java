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
package org.talend.commons.utils.resource;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.Platform;
import org.junit.Assert;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class BundleFileUtilTest {

    @Test
    public void test_getBundleFile4Class() throws IOException {
        File bundleFile = BundleFileUtil.getBundleFile((Class) null, "");
        Assert.assertNull(bundleFile);

        bundleFile = BundleFileUtil.getBundleFile(this.getClass(), "data/TextFile.txt");
        Assert.assertTrue(bundleFile.exists());

        testPath(bundleFile);
    }

    private void testPath(File bundleFile) {
        final String path = bundleFile.getAbsolutePath().replace('\\', '/');
        if (Platform.inDevelopmentMode()) {
            Assert.assertTrue(path.endsWith("/org.talend.commons.runtime.test/data/TextFile.txt"));
        } else {
            // if jar
            if (path.contains("org.eclipse.osgi")) {
                Assert.assertTrue(path.contains("/configuration/org.eclipse.osgi/")); // in osgi
            } else {
                // if foler, becasue the folder contained version.
                Assert.assertTrue(path.contains("/plugins/org.talend.commons.runtime.test"));
            }
        }
    }

    @Test
    public void test_getBundleFile4Bundle() throws IOException {
        File bundleFile = BundleFileUtil.getBundleFile((Bundle) null, "");
        Assert.assertNull(bundleFile);

        bundleFile = BundleFileUtil.getBundleFile(FrameworkUtil.getBundle(this.getClass()), "data/TextFile.txt");
        Assert.assertTrue(bundleFile.exists());

        testPath(bundleFile);
    }
}
