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
package org.talend.core.hadoop;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.talend.core.classloader.DynamicClassLoader;

/**
 * created by hcyi on Dec 3, 2019
 * Detailled comment
 *
 */
public class HadoopClassLoaderUtilTest {

    @Test
    public void testIsWebHDFS4NULL() {
        assertFalse(HadoopClassLoaderUtil.isWebHDFS(null));
    }

    @Test
    public void testIsWebHDFS4EmptyCharacter() {
        assertFalse(HadoopClassLoaderUtil.isWebHDFS(""));//$NON-NLS-1$
    }

    @Test
    public void testIsWebHDFSWithSSL() {
        String nameNodeURI = "swebhdfs://<HOST>:<HTTP_PORT>";//$NON-NLS-1$
        assertTrue(HadoopClassLoaderUtil.isWebHDFS(nameNodeURI));
    }

    @Test
    public void testIsWebHDFSWithoutSSL() {
        String nameNodeURI = "webhdfs://<HOST>:<HTTP_PORT>";//$NON-NLS-1$
        assertTrue(HadoopClassLoaderUtil.isWebHDFS(nameNodeURI));
    }

    @Test
    public void testAddExtraJars4ExcludedAllJars() {
        String nameNodeURI = "webhdfs://<HOST>:<HTTP_PORT>";//$NON-NLS-1$
        DynamicClassLoader classLoader = new DynamicClassLoader();
        List<String> excludedJars = Arrays.asList(HadoopClassLoaderUtil.webhdfsExtraJars);
        classLoader.addLibraries(excludedJars);
        assertEquals(classLoader, HadoopClassLoaderUtil.addExtraJars(classLoader, EHadoopCategory.HDFS, nameNodeURI));
    }
}