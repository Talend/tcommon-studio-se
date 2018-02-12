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
package org.talend.core.classloader;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * created by hcyi on May 19, 2017 Detailled comment
 *
 */
public class DynamicClassLoaderTest {

    @Test
    public void testUpdateBaseLoader() throws MalformedURLException {
        String[] addedJars = new String[] { "commons-lang-2.6.jar" };
        DynamicClassLoader baseLoader = new DynamicClassLoader();
        List<String> libList = new ArrayList<String>();
        libList.add("avro-1.7.5.jar");
        baseLoader.addLibraries(libList);
        assertEquals(null, baseLoader.getLibStorePath());
        assertEquals(1, baseLoader.getURLs().length);
        baseLoader.updateBaseLoader(baseLoader, addedJars);
        assertEquals(2, baseLoader.getURLs().length);
    }
}
