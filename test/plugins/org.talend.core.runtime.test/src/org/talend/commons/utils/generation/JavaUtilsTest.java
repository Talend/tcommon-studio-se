// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.commons.utils.generation;

import static org.junit.Assert.*;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.core.JavaCore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;

public class JavaUtilsTest {

    private String originalCompliance;

    private IEclipsePreferences eclipsePreferences;

    @Before
    public void setUp() throws Exception {
        eclipsePreferences = InstanceScope.INSTANCE.getNode(JavaCore.PLUGIN_ID);
        originalCompliance = eclipsePreferences.get(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
    }
    
    @Test
    public void testUpdateProjectJavaVersion() {
        JavaUtils.updateProjectJavaVersion(JavaCore.VERSION_1_6);
        
        assertEquals(eclipsePreferences.get(JavaCore.COMPILER_COMPLIANCE, null), JavaCore.VERSION_1_6);
        assertEquals(JavaCore.getOption(JavaCore.COMPILER_COMPLIANCE), JavaCore.VERSION_1_6);
        
        assertEquals(JavaCore.VERSION_1_6,
                CoreRuntimePlugin.getInstance().getProjectPreferenceManager().getValue("talend.project.java.version"));
        assertEquals(JavaCore.VERSION_1_6, JavaUtils.getProjectJavaVersion());
    }

    @After
    public void tearDown() throws Exception {
        JavaUtils.updateProjectJavaVersion(originalCompliance);
    }

}
