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
package org.talend.core.model.general;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
import org.junit.Assert;
import org.junit.Test;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;

/**
 * created by wchen on Aug 21, 2017 Detailled comment
 *
 */
public class ModuleNeededTest {

    @Test
    public void testEquals() {
        Set<ModuleNeeded> modules = new HashSet<ModuleNeeded>();
        ModuleNeeded module1 = new ModuleNeeded("tMysqlInput", "test1.jar", "description", false, null, null,
                "mvn:org.talend.libraries/test1/6.0.0");
        ModuleNeeded module2 = new ModuleNeeded("tMysqlConnection", "test1.jar", "description", false, null, null,
                "mvn:org.talend.libraries/test1/6.0.0");
        Assert.assertFalse(module1.equals(module2));
        modules.add(module1);
        modules.add(module2);
        Assert.assertEquals(modules.size(), 2);
        ModuleNeeded module3 = new ModuleNeeded("tMysqlInput", "test2.jar", "description", false, null, null,
                "mvn:org.talend.libraries/test2/6.0.0");
        Assert.assertFalse(module1.equals(module3));
        modules.add(module3);
        Assert.assertEquals(modules.size(), 3);
        ModuleNeeded module4 = new ModuleNeeded("tMysqlInput", "test1.jar", "description", false, null, null,
                "mvn:org.talend.libraries/test1/6.1.0");
        Assert.assertFalse(module1.equals(module4));
        modules.add(module4);
        Assert.assertEquals(modules.size(), 4);

        ModuleNeeded module5 = new ModuleNeeded("tMysqlInput", "test1.jar", "description 1", true, null, "version = 'MYSQL_5'",
                "mvn:org.talend.libraries/test1/6.0.0");
        Assert.assertTrue(module1.equals(module5));
        modules.add(module5);
        Assert.assertEquals(modules.size(), 4);

    }

    @Test
    public void getGetMavenUri() {
        ModuleNeeded module1 = new ModuleNeeded("tMysqlInput", "test1.exe", "description", false, null, null,
                "mvn:org.talend.libraries/test1/6.1.0");
        Assert.assertEquals(module1.getMavenURIFromConfiguration(), "mvn:org.talend.libraries/test1/6.1.0/exe");
        Assert.assertEquals(module1.getMavenUri(), "mvn:org.talend.libraries/test1/6.1.0/exe");

        ModuleNeeded module2 = new ModuleNeeded("tMysqlInput", "mysql-connector-java-5.1.30-bin.jar", null, false);
        Assert.assertEquals(module2.getMavenURIFromConfiguration(), null);
        Assert.assertEquals(module2.getMavenUri(), "mvn:org.talend.libraries/mysql-connector-java-5.1.30-bin/6.0.0/jar");

        // jar not in configuration
        ModuleNeeded module3 = new ModuleNeeded("tMysqlInput", "ModuleNeededTest.jar", null, false);
        Assert.assertEquals(module3.getMavenURIFromConfiguration(), null);
        Assert.assertEquals(module3.getMavenUri(), "mvn:org.talend.libraries/ModuleNeededTest/6.0.0-SNAPSHOT/jar");

        // test custom mvn uri
        String customMvnURI = "mvn:org.talend.libraries/ModuleNeededTest/6.1.0-SNAPSHOT/jar";
        module3.setCustomMavenUri(customMvnURI);
        Assert.assertEquals(module3.getMavenUri(), customMvnURI);
        customMvnURI = "mvn:org.talend.libraries/ModuleNeededTest/6.2.0-SNAPSHOT/jar";
        module3.setCustomMavenUri(customMvnURI);
        Assert.assertEquals(module3.getMavenUri(), customMvnURI);

    }

    @Test
    public void testGetStatus() throws URISyntaxException, IOException {
        // test jar not installed in platform
        URI testFileToDeploy = FileLocator.toFileURL(this.getClass().getClassLoader().getResource("resources/tRowGenerator.xml"))
                .toURI();
        ModuleNeeded module1 = new ModuleNeeded("tMysqlInput", "tRowGenerator.xml", "description", false, null, null,
                "mvn:org.talend.libraries/tRowGenerator/6.0.0");
        ILibraryManagerService libService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                ILibraryManagerService.class);
        libService.deploy(testFileToDeploy, module1.getMavenUri());
        Assert.assertEquals(module1.getStatus(), ELibraryInstallStatus.INSTALLED);
        Assert.assertEquals(module1.getDeployStatus(), ELibraryInstallStatus.DEPLOYED);

        module1.setCustomMavenUri("mvn:org.talend.libraries/tRowGenerator/6.1.0");

        Assert.assertEquals(module1.getStatus(), ELibraryInstallStatus.NOT_INSTALLED);
        Assert.assertEquals(module1.getDeployStatus(), ELibraryInstallStatus.NOT_DEPLOYED);

        // change back the status
        module1.setCustomMavenUri(null);
        String jarPathFromMaven = libService.getJarPathFromMaven("mvn:org.talend.libraries/tRowGenerator/6.0.0/xml");
        File file = new File(jarPathFromMaven);
        if (file.exists()) {
            FilesUtils.deleteFile(file.getParentFile().getParentFile(), true);
        }

    }

    @Test
    public void testGetStatus1() throws URISyntaxException, IOException {
        // test jar installed in platform
        String mavenURI = "mvn:org.talend.libraries/commons-logging-1.1.1-emr-2.4.0/6.0.0/jar";
        String customURI = "mvn:org.talend.libraries/commons-logging-emr/6.0.0/jar";
        String platformURL = "platform:/plugin/org.talend.libraries.hadoop/lib/commons-logging-1.1.1-emr-2.4.0.jar";
        ILibraryManagerService libService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                ILibraryManagerService.class);
        ModuleNeeded module1 = new ModuleNeeded("", "commons-logging-1.1.1-emr-2.4.0.jar", "description", false, null, null,
                mavenURI);
        module1.setModuleLocaion(platformURL);
        if (ELibraryInstallStatus.NOT_INSTALLED == module1.getStatus() || !libService.checkJarInstalledFromPlatform(platformURL)) {
            fail("Please change an other jar installed in platform for test");
        }
        module1.setCustomMavenUri(customURI);
        Assert.assertEquals(ELibraryInstallStatus.NOT_INSTALLED, module1.getStatus());
        Assert.assertEquals(ELibraryInstallStatus.NOT_DEPLOYED, module1.getDeployStatus());
        URI testFileToDeploy = FileLocator.toFileURL(this.getClass().getClassLoader().getResource("resources/tRowGenerator.xml"))
                .toURI();
        libService.deploy(testFileToDeploy, module1.getMavenUri());

        Assert.assertEquals(module1.getStatus(), ELibraryInstallStatus.INSTALLED);
        Assert.assertEquals(module1.getDeployStatus(), ELibraryInstallStatus.DEPLOYED);

        // change back the status
        module1.setCustomMavenUri(null);
        String jarPathFromMaven = libService.getJarPathFromMaven(customURI);
        File file = new File(jarPathFromMaven);
        if (file.exists()) {
            FilesUtils.deleteFile(file.getParentFile().getParentFile(), true);
        }

    }
    
    @Test
    public void testSetModuleName() {
        String moduleValue = "mysql-connector-java-5.1.30-bin";
        ModuleNeeded moduleNeeded = new ModuleNeeded(null, moduleValue, null, true);
        moduleNeeded.setModuleName(moduleValue);
        Assert.assertEquals(moduleValue, moduleNeeded.getModuleName());

        
        moduleValue = "mysql-connector-java-5.1.30-bin.jar";
        moduleNeeded.setModuleName(moduleValue);
        Assert.assertEquals("mysql-connector-java-5.1.30-bin.jar", moduleNeeded.getModuleName());
    }
    
}
