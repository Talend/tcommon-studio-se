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

/**
 * Creates and updates properties for the eclipse commandline in case of relaunch <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z sgandon $
 * 
 */
public class EclipseCommandLine {

    static final String PROP_VM = "eclipse.vm"; //$NON-NLS-1$

    static final String PROP_VMARGS = "eclipse.vmargs"; //$NON-NLS-1$

    static final String PROP_COMMANDS = "eclipse.commands"; //$NON-NLS-1$

    static final String CMD_VMARGS = "-vmargs"; //$NON-NLS-1$

    static final String NEW_LINE = "\n"; //$NON-NLS-1$

    static public final String CLEAN = "-clean"; //$NON-NLS-1$

    /**
     * Use it to specify the type of project that will be use to configure the studio when relaunched
     */
    static public final String TALEND_PROJECT_TYPE_COMMAND = "-talendProjectType"; //$NON-NLS-1$

    /**
     * for relaunch of the plugins when relaunching the Studio
     */
    static public final String TALEND_RELOAD_COMMAND = "-talendReload"; //$NON-NLS-1$    

    /**
     * for relaunch of the plugins when relaunching the Studio
     */
    static public final String TALEND_DISABLE_LOGINDIALOG_COMMAND = "--disableLoginDialog"; //$NON-NLS-1$   

    static public final String TALEND_DISABLE_UPDATE_DIALOG_COMMAND = "--disableUpdateDialog"; //$NON-NLS-1$

    static public final String TALEND_DISABLE_EXTERNAL_MODULE_INSTALL_DIALOG_COMMAND = "--disableExternalModuleInstallDialog"; //$NON-NLS-1$

    static public final String TALEND_NOSPLASH_COMMAND = "-nosplash"; //$NON-NLS-1$

    /**
     * for TUP-2218, enable to open the job auto, when open studio. the args should be name of job. if want to open
     * several jobs at same times. will split by comma "," or semicolon ";"
     */
    static public final String TALEND_SHOW_JOB_COMMAND = "--showJob"; //$NON-NLS-1$

    /**
     * By default, the type is PROCESS, but can be other type of job, like MR, Storm, Joblet etc, will implement it
     * later.
     */
    static public final String TALEND_SHOW_JOB_TYPE_COMMAND = "--showJobType"; //$NON-NLS-1$

    static public void updateOrCreateExitDataPropertyWithCommand(String command, String value, boolean delete) {
        updateOrCreateExitDataPropertyWithCommand(command, value, delete, false);
    }

    /**
     * this creates or updates the org.eclipse.equinox.app.IApplicationContext.EXIT_DATA_PROPERTY by adding or changing
     * the command with value, except if value is null then the command shall be removed.
     * 
     * @param command the command to add or update or remove (if value is null) (usually starts with a -)
     * @param value the value of the command,if the value is null,will only update the commmand
     * @param delete the flag used to trigger delete or insert/update the command
     * @param isOption this flag used to trigger for the option command without any arguments.
     */
    static public void updateOrCreateExitDataPropertyWithCommand(String command, String value, boolean delete, boolean isOption) {
        boolean isValueNull = false;
        if (value == null || "".equals(value)) { //$NON-NLS-1$
            isValueNull = true;
        } else {
            if (1 < value.length() && value.startsWith("'") && value.endsWith("'")) { //$NON-NLS-1$ //$NON-NLS-2$
                // nothing to do
            } else {
                if (value.contains(" ")) { //$NON-NLS-1$
                    value = value.replaceAll("\\\\", "\\\\\\\\"); //$NON-NLS-1$ //$NON-NLS-2$
                    value = "'" + value + "'"; //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
        }
        StringBuffer result = new StringBuffer(512);

        String currentProperty = System.getProperty(org.eclipse.equinox.app.IApplicationContext.EXIT_DATA_PROPERTY);
        String patternStr = "\\s+.+\\s"; //$NON-NLS-1$
        // if the command is only one option. should only process the command without arguments.
        if (isOption) {
            patternStr = "\\s+"; //$NON-NLS-1$
        }

        String commandRegx = command.replaceAll("\\.", "\\\\\\."); //$NON-NLS-1$//$NON-NLS-2$
        String valueRegx = value;
        if (value != null) {
            valueRegx = value.replaceAll("\\\\", "\\\\\\\\"); //$NON-NLS-1$//$NON-NLS-2$
        }
        if (currentProperty != null) {// update the property
            Pattern commandPattern = Pattern.compile(commandRegx + patternStr);// -talendRestart\s+.+\s
            Matcher restartMatcher = commandPattern.matcher(currentProperty);

            if (delete) {// if delete,no matter the value is null or not,remove the command directly
                if (restartMatcher.find()) {// match found so remove it
                    currentProperty = restartMatcher.replaceAll(""); //$NON-NLS-1$
                } // else no match so do nothing
            } else {// else add or update the command
                    // try to find existing commands to update them
                    // find the index of the arg to replace its value
                // if value is null,only add or update the command
                if (restartMatcher.find()) {// match found so update the command
                    if (isOption) {
                        // because no arguments, if have been existed, so ignore.
                    } else {
                        currentProperty = restartMatcher.replaceAll(commandRegx + EclipseCommandLine.NEW_LINE
                                + (isValueNull ? "" : valueRegx + EclipseCommandLine.NEW_LINE));
                    }
                } else {// no match so insert it before the CMD_VMARGS
                    int indexOfVmArgs = currentProperty.indexOf(CMD_VMARGS);
                    if (indexOfVmArgs >= 0) {// found it so insert command before
                        currentProperty = currentProperty.substring(0, indexOfVmArgs) + command + EclipseCommandLine.NEW_LINE
                                + (isValueNull ? "" : value + EclipseCommandLine.NEW_LINE)
                                + currentProperty.substring(indexOfVmArgs);
                    } else {// vmargs command not found so don't know where to set it to throw Exception
                        throw new IllegalArgumentException("the property :"
                                + org.eclipse.equinox.app.IApplicationContext.EXIT_DATA_PROPERTY + "must constain "
                                + EclipseCommandLine.CMD_VMARGS);
                    }
                }
            }

            result.append(currentProperty);

        } else {// creates a new string
                // define the java process to launch
            String property = System.getProperty(EclipseCommandLine.PROP_VM);
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
                if (value != null) {// command to be set
                    result.append(command);
                    result.append(EclipseCommandLine.NEW_LINE);
                    if (!isValueNull) {
                        result.append(value);
                        result.append(EclipseCommandLine.NEW_LINE);
                    }
                }// else command shall be removed,but it does not exists so ignor it
            } else {
                Pattern commandPattern = Pattern.compile(commandRegx + patternStr);// -talendRestart\s+.+\s
                Matcher restartMatcher = commandPattern.matcher(property);

                if (delete) {// if delete,no matter the value is null or not,remove the command dirctly
                    if (restartMatcher.find()) {// match found so remove it
                        property = restartMatcher.replaceAll(EclipseCommandLine.NEW_LINE);
                    }
                } else {// else need add or update the
                    if (restartMatcher.find()) {// match found so update the command
                        if (isOption) {
                            // because no arguments, if have been existed, so ignore.
                        } else {
                            property = restartMatcher.replaceAll(commandRegx + EclipseCommandLine.NEW_LINE
                                    + (isValueNull ? "" : valueRegx + EclipseCommandLine.NEW_LINE));
                        }
                    } else {// no match so add it
                        result.append(command);
                        result.append(EclipseCommandLine.NEW_LINE);
                        if (!isValueNull) {// won't add value if value is null
                            result.append(value);
                            result.append(EclipseCommandLine.NEW_LINE);
                        }
                    }
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
        }
        System.setProperty(org.eclipse.equinox.app.IApplicationContext.EXIT_DATA_PROPERTY, result.toString());
    }
}
