// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.commons.utils.system;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import junit.framework.Assert;

/**
 * DOC sgandon class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class EclipseCommandLineTest {

    static final String TALEND_PROJECT_TYPE_COMMAND = "-talendProjectType"; //$NON-NLS-1$

    static final String TALEND_RESTART_COMMAND = "-talendRestart"; //$NON-NLS-1$        

    @Before
    public void resetProperties() {
        System.clearProperty(org.eclipse.equinox.app.IApplicationContext.EXIT_DATA_PROPERTY);
        System.setProperty(EclipseCommandLine.PROP_VM, "/System/Library/Frameworks/JavaVM.framework");
        System.setProperty(
                EclipseCommandLine.PROP_VMARGS,
                "-Xms64m\n-Xmx768m\n-Xdock:icon=../Resources/talend.icns\n-XstartOnFirstThread\n-Dorg.eclipse.swt.internal.carbon.smallFonts\n-Dosgi.instance.area.default=../../../workspace\n-Xdebug\n-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=1716\n-XX:MaxPermSize=256m\n-Djava.class.path=/Applications/java/talend/studio/Talend-All-r70207-V5.0.0NB/Talend-macosx-cocoa.app/Contents/MacOS/../../../plugins/org.eclipse.equinox.launcher_1.1.0.v20100507.jar\n");
        System.setProperty(
                EclipseCommandLine.PROP_COMMANDS,
                "-os\nmacosx\n-ws\ncocoa\n-arch\nx86_64\n-showsplash\n-launcher\n/Applications/java/talend/studio/Talend-All-r70207-V5.0.0NB/Talend-macosx-cocoa.app/Contents/MacOS/Talend-macosx-cocoa\n-name\nTalend-macosx-cocoa\n--launcher.library\n/Applications/java/talend/studio/Talend-All-r70207-V5.0.0NB/Talend-macosx-cocoa.app/Contents/MacOS/../../../plugins/org.eclipse.equinox.launcher.cocoa.macosx.x86_64_1.1.0.v20100503/eclipse_1307.so\n-startup\n/Applications/java/talend/studio/Talend-All-r70207-V5.0.0NB/Talend-macosx-cocoa.app/Contents/MacOS/../../../plugins/org.eclipse.equinox.launcher_1.1.0.v20100507.jar\n-console\n-keyring\n/Users/sgandon/.eclipse_keyring\n-showlocation\n-vm\n/System/Library/Frameworks/JavaVM.framework\n");

    }

    /**
     * Test method for
     * {@link org.talend.commons.utils.system.EclipseCommandLine#updateOrCreateExitDataProperty(java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testUpdateOrCreateExitDataPropertyToCreateRestartFalseCommandline() {
        EclipseCommandLine.updateOrCreateExitDataPropertyWithCommand(TALEND_RESTART_COMMAND, "false", false);
        EclipseCommandLine.updateOrCreateExitDataPropertyWithCommand(TALEND_PROJECT_TYPE_COMMAND, null, true);
        System.out.println(System.getProperty(org.eclipse.equinox.app.IApplicationContext.EXIT_DATA_PROPERTY));
        Assert.assertEquals(System.getProperty(org.eclipse.equinox.app.IApplicationContext.EXIT_DATA_PROPERTY),
                buildRestartFalseCommandLine());
    }

    /**
     * Test method for
     * {@link org.talend.commons.utils.system.EclipseCommandLine#updateOrCreateExitDataProperty(java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testUpdateOrCreateExitDataPropertyToRemoveProperty() {
        EclipseCommandLine.updateOrCreateExitDataPropertyWithCommand("dummy", "dummy", false);
        String initialProp = System.getProperty(org.eclipse.equinox.app.IApplicationContext.EXIT_DATA_PROPERTY);
        Assert.assertFalse(initialProp.contains(TALEND_RESTART_COMMAND));
        EclipseCommandLine.updateOrCreateExitDataPropertyWithCommand(TALEND_RESTART_COMMAND, "false", false);
        Assert.assertTrue(System.getProperty(org.eclipse.equinox.app.IApplicationContext.EXIT_DATA_PROPERTY).contains(
                TALEND_RESTART_COMMAND + "\nfalse"));
        EclipseCommandLine.updateOrCreateExitDataPropertyWithCommand(TALEND_RESTART_COMMAND, null, true);
        Assert.assertEquals(System.getProperty(org.eclipse.equinox.app.IApplicationContext.EXIT_DATA_PROPERTY), initialProp);
    }

    /**
     * Test method for
     * {@link org.talend.commons.utils.system.EclipseCommandLine#updateOrCreateExitDataProperty(java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testUpdateOrCreateExitDataPropertyToUpdateProperty() {
        EclipseCommandLine.updateOrCreateExitDataPropertyWithCommand(TALEND_RESTART_COMMAND, "false", false);
        Assert.assertFalse(System.getProperty(org.eclipse.equinox.app.IApplicationContext.EXIT_DATA_PROPERTY).contains(
                TALEND_RESTART_COMMAND + "\ntrue"));
        EclipseCommandLine.updateOrCreateExitDataPropertyWithCommand(TALEND_RESTART_COMMAND, "true", false);
        Assert.assertTrue(System.getProperty(org.eclipse.equinox.app.IApplicationContext.EXIT_DATA_PROPERTY).contains(
                TALEND_RESTART_COMMAND + "\ntrue"));
        Assert.assertFalse(System.getProperty(org.eclipse.equinox.app.IApplicationContext.EXIT_DATA_PROPERTY).contains(
                TALEND_RESTART_COMMAND + "\nfalse"));
    }

    @Test
    public void testArgumentWithWhitespace() {
        String studioInstallationFolder = "C:\\Folder with whitespace\\Studio";
        String expectFolder = "'C:\\\\Folder with whitespace\\\\Studio'";
        EclipseCommandLine.updateOrCreateExitDataPropertyWithCommand("-talend.licence.path", studioInstallationFolder, false);
        Assert.assertTrue(
                System.getProperty(org.eclipse.equinox.app.IApplicationContext.EXIT_DATA_PROPERTY).contains(expectFolder));

        studioInstallationFolder = "/user/home/talend/download/Folder with whitespace";
        expectFolder = "'" + studioInstallationFolder + "'";
        EclipseCommandLine.updateOrCreateExitDataPropertyWithCommand("-talend.licence.path", studioInstallationFolder, false);
        Assert.assertTrue(
                System.getProperty(org.eclipse.equinox.app.IApplicationContext.EXIT_DATA_PROPERTY).contains(expectFolder));
    }

    @Test
    public void testArgumentWithoutWhitespace() {
        String studioInstallationFolder = "C:\\FolderWithoutWhitespace\\Studio";
        String expectFolder = studioInstallationFolder;
        EclipseCommandLine.updateOrCreateExitDataPropertyWithCommand("-talend.licence.path", studioInstallationFolder, false);
        Assert.assertTrue(
                System.getProperty(org.eclipse.equinox.app.IApplicationContext.EXIT_DATA_PROPERTY).contains(expectFolder));

        studioInstallationFolder = "/user/home/talend/download/FolderWithWhitespace";
        expectFolder = studioInstallationFolder;
        EclipseCommandLine.updateOrCreateExitDataPropertyWithCommand("-talend.licence.path", studioInstallationFolder, false);
        Assert.assertTrue(
                System.getProperty(org.eclipse.equinox.app.IApplicationContext.EXIT_DATA_PROPERTY).contains(expectFolder));
    }

    @Test
    @Ignore("no able to create the same string with the same order.")
    public void testUpdateOrCreateExitDataPropertyBuildRestartWithProdcutType() {
        EclipseCommandLine.updateOrCreateExitDataPropertyWithCommand(TALEND_RESTART_COMMAND, "true", false);
        EclipseCommandLine.updateOrCreateExitDataPropertyWithCommand(TALEND_PROJECT_TYPE_COMMAND, "DI", false);
        Assert.assertEquals(System.getProperty(org.eclipse.equinox.app.IApplicationContext.EXIT_DATA_PROPERTY),
                buildRestartCommandLine("DI"));

    }

    /**
     * this recreates the Eclipse command line by add (or changing) the option -talendRestart false and removing the
     * -talendProjectType value
     * 
     * @return
     */
    String buildRestartFalseCommandLine() {

        String falseValue = "false";
        // define the java process tio launch
        String property = System.getProperty(EclipseCommandLine.PROP_VM);
        StringBuffer result = new StringBuffer(512);
        result.append(property);
        result.append(EclipseCommandLine.NEW_LINE);

        // add the java argument for the jvm
        // append the vmargs and commands. Assume that these already end in \n
        String vmargs = System.getProperty(EclipseCommandLine.PROP_VMARGS);
        if (vmargs != null) {
            result.append(vmargs);
        }

        // append the rest of the args, replacing or adding -data as required
        property = System.getProperty(EclipseCommandLine.PROP_COMMANDS);
        if (property == null) {
            result.append(TALEND_RESTART_COMMAND);
            result.append(EclipseCommandLine.NEW_LINE);
            result.append(falseValue); //$NON-NLS-1$
            result.append(EclipseCommandLine.NEW_LINE);
        } else {
            // try to find existing commands to update them
            // find the index of the arg to replace its value
            Pattern restartPattern = Pattern.compile(TALEND_RESTART_COMMAND + "\\s+.+\\s");//$NON-NLS-1$ -talendRestart\s+.+\s
            Matcher restartMatcher = restartPattern.matcher(property);
            if (restartMatcher.find()) {
                property = restartMatcher.replaceAll(TALEND_RESTART_COMMAND + EclipseCommandLine.NEW_LINE + falseValue
                        + EclipseCommandLine.NEW_LINE); //$NON-NLS-1$
            } else {
                result.append(TALEND_RESTART_COMMAND);
                result.append(EclipseCommandLine.NEW_LINE);
                result.append(falseValue);
                result.append(EclipseCommandLine.NEW_LINE);
            }
            Pattern projTypePattern = Pattern.compile(TALEND_PROJECT_TYPE_COMMAND + "\\s+.+\\s");//$NON-NLS-1$ -talendProjectType\s+.+\s
            Matcher projTypeMatcher = projTypePattern.matcher(property);
            if (projTypeMatcher.find()) {
                property = projTypeMatcher.replaceAll(EclipseCommandLine.NEW_LINE);
            } // not here already to do nothing

            result.append(property);
        }

        // put the vmargs back at the very end so that the Main.java can know the vm args and set them in the system
        // property eclipse.vmargs
        // (the previously set eclipse.commands property already contains the -vm arg)
        if (vmargs != null) {
            result.append(EclipseCommandLine.CMD_VMARGS);
            result.append(EclipseCommandLine.NEW_LINE);
            result.append(vmargs);
        }

        return result.toString();
    }

    String buildRestartCommandLine(String projectType) {
        // define the java process tio launch
        String property = System.getProperty(EclipseCommandLine.PROP_VM);
        StringBuffer result = new StringBuffer(512);
        result.append(property);
        result.append(EclipseCommandLine.NEW_LINE);

        // add the java argument for the jvm
        // append the vmargs and commands. Assume that these already end in \n
        String vmargs = System.getProperty(EclipseCommandLine.PROP_VMARGS);
        if (vmargs != null) {
            result.append(vmargs);
        }

        // append the rest of the args, replacing or adding -data as required
        property = System.getProperty(EclipseCommandLine.PROP_COMMANDS);
        if (property == null) {
            result.append(TALEND_RESTART_COMMAND);
            result.append(EclipseCommandLine.NEW_LINE);
            result.append("true"); //$NON-NLS-1$
            result.append(EclipseCommandLine.NEW_LINE);
            result.append(TALEND_PROJECT_TYPE_COMMAND);
            result.append(EclipseCommandLine.NEW_LINE);
            result.append(projectType);
            result.append(EclipseCommandLine.NEW_LINE);
        } else {
            // try to find existing commands to update them
            // find the index of the arg to replace its value
            Pattern restartPattern = Pattern.compile(TALEND_RESTART_COMMAND + "\\s+.+\\s");//$NON-NLS-1$ -talendRestart\s+.+\s
            Matcher restartMatcher = restartPattern.matcher(property);
            if (restartMatcher.find()) {
                property = restartMatcher.replaceAll(TALEND_RESTART_COMMAND + EclipseCommandLine.NEW_LINE
                        + "true" + EclipseCommandLine.NEW_LINE); //$NON-NLS-1$
            } else {
                result.append(TALEND_RESTART_COMMAND);
                result.append(EclipseCommandLine.NEW_LINE);
                result.append("true");
                result.append(EclipseCommandLine.NEW_LINE);
            }
            Pattern projTypePattern = Pattern.compile(TALEND_PROJECT_TYPE_COMMAND + "\\s+.+\\s");//$NON-NLS-1$ -talendProjectType\s+.+\s
            Matcher projTypeMatcher = projTypePattern.matcher(property);
            if (projTypeMatcher.find()) {
                property = projTypeMatcher.replaceAll(TALEND_PROJECT_TYPE_COMMAND + EclipseCommandLine.NEW_LINE + projectType
                        + EclipseCommandLine.NEW_LINE);
            } else {
                result.append(TALEND_PROJECT_TYPE_COMMAND);
                result.append(EclipseCommandLine.NEW_LINE);
                result.append(projectType);
                result.append(EclipseCommandLine.NEW_LINE);
            }
            result.append(property);
        }

        // put the vmargs back at the very end so that the Main.java can know the vm args and set them in the system
        // property eclipse.vmargs
        // (the previously set eclipse.commands property already contains the -vm arg)
        if (vmargs != null) {
            result.append(EclipseCommandLine.CMD_VMARGS);
            result.append(EclipseCommandLine.NEW_LINE);
            result.append(vmargs);
        }

        return result.toString();
    }

}
