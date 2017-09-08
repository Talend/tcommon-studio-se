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
package org.talend.designer.maven.template;

import static org.junit.Assert.*;

import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.core.JavaCore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;

public class MavenTemplateManagerTest {

    private String originalCompliance;
    
    private ProjectPreferenceManager manager;

    @Before
    public void setUp() throws Exception {
        IEclipsePreferences eclipsePreferences = InstanceScope.INSTANCE.getNode(JavaCore.PLUGIN_ID);
        originalCompliance = eclipsePreferences.get(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
        manager = CoreRuntimePlugin.getInstance().getProjectPreferenceManager();
    }
    
    @Test
    public void testSetJavaVersionForModel() {
        manager.setValue(JavaUtils.PROJECT_JAVA_VERSION_KEY, JavaCore.VERSION_1_7);
        Model model = MavenTemplateManager.getCodeProjectTemplateModel();
        validateResult(model, JavaCore.VERSION_1_7);
        
        manager.setValue(JavaUtils.PROJECT_JAVA_VERSION_KEY, "");
        model = MavenTemplateManager.getCodeProjectTemplateModel();
        validateResult(model, JavaCore.VERSION_1_8);
    }

    private void validateResult(Model model, String expectedValue) {
        Plugin plugin = model.getBuild().getPluginManagement().getPluginsAsMap()
                .get("org.apache.maven.plugins:maven-compiler-plugin"); //$NON-NLS-1$
        Object object = plugin.getConfiguration();
        if (object instanceof Xpp3Dom) {
            Xpp3Dom configNode = (Xpp3Dom) object;
            Xpp3Dom sourceNode = configNode.getChild("source"); //$NON-NLS-1$
            Xpp3Dom targetNode = configNode.getChild("target"); //$NON-NLS-1$
            assertEquals(expectedValue, sourceNode.getValue());
            assertEquals(expectedValue, targetNode.getValue());
        }
    }
    
    @After
    public void tearDown() throws Exception {
        JavaUtils.updateProjectJavaVersion(originalCompliance);
    }

}
