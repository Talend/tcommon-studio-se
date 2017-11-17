// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.librariesmanager.utils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeoutException;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.gmf.util.DisplayUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.core.model.general.ModuleStatusProvider;
import org.talend.core.nexus.NexusServerBean;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.librariesmanager.ui.LibManagerUiPlugin;
import org.talend.librariesmanager.ui.i18n.Messages;

/**
 * created by wchen on Sep 25, 2017 Detailled comment
 *
 */
public class ModuleMavenURIUtils {

    private static PatternMatcherInput patternMatcherInput;

    private static Perl5Matcher matcher = new Perl5Matcher();

    private static Perl5Compiler compiler = new Perl5Compiler();

    private static Pattern pattern;

    // match mvn:group-id/artifact-id/version/type/classifier
    public static final String expression1 = "(mvn:(\\w+.*/)(\\w+.*/)([0-9]+(\\.[0-9])+(-SNAPSHOT){0,1}/)(\\w+/)(\\w+))";//$NON-NLS-1$

    // match mvn:group-id/artifact-id/version/type
    public static final String expression2 = "(mvn:(\\w+.*/)(\\w+.*/)([0-9]+(\\.[0-9])+(-SNAPSHOT){0,1}/)\\w+)";//$NON-NLS-1$

    // match mvn:group-id/artifact-id/version
    public static final String expression3 = "(mvn:(\\w+.*/)(\\w+.*/)([0-9]+(\\.[0-9])+(-SNAPSHOT){0,1}))";//$NON-NLS-1$

    public static final String MVNURI_TEMPLET = "mvn:<groupid>/<artifactId>/<version>/<type>";

    public static String validateCustomMvnURI(String originalText, String customText) {
        if (customText.equals(originalText)) {
            return Messages.getString("InstallModuleDialog.error.sameCustomURI");
        }
        if (!validateMvnURI(customText)) {
            return Messages.getString("InstallModuleDialog.error.customURI");
        }
        return null;
    }

    public static boolean validateMvnURI(String mvnURI) {
        if (pattern == null) {
            try {
                pattern = compiler.compile(expression1 + "|" + expression2 + "|" + expression3);
            } catch (MalformedPatternException e) {
                ExceptionHandler.process(e);
            }
        }
        patternMatcherInput = new PatternMatcherInput(mvnURI);
        matcher.setMultiline(false);
        boolean isMatch = matcher.matches(patternMatcherInput, pattern);
        return isMatch;
    }

    public static boolean checkInstalledStatus(String uri) {
        final String mvnURI = uri;
        ILibraryManagerService libManagerService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                ILibraryManagerService.class);
        String jarPathFromMaven = libManagerService.getJarPathFromMaven(mvnURI);
        final boolean[] deployStatus = new boolean[] { false };
        if (jarPathFromMaven != null) {
            deployStatus[0] = true;
        } else {
            final IRunnableWithProgress acceptOursProgress = new IRunnableWithProgress() {

                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    NexusServerBean customNexusServer = TalendLibsServerManager.getInstance().getCustomNexusServer();
                    if (customNexusServer != null) {
                        File resolveJar = null;
                        try {
                            resolveJar = libManagerService.resolveJar(customNexusServer, mvnURI);
                        } catch (Exception e) {
                            deployStatus[0] = false;
                        }
                        if (resolveJar != null) {
                            deployStatus[0] = true;
                            DisplayUtils.getDisplay().syncExec(new Runnable() {

                                @Override
                                public void run() {
                                    LibManagerUiPlugin.getDefault().getLibrariesService().checkLibraries();
                                }
                            });
                        }
                    }
                }
            };

            ProgressMonitorDialog dialog = new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                    .getShell());
            try {
                dialog.run(true, true, acceptOursProgress);
            } catch (Throwable e) {
                if (!(e instanceof TimeoutException)) {
                    ExceptionHandler.process(e);
                }
            }

        }

        if (!deployStatus[0]) {
            ModuleStatusProvider.putDeployStatus(mvnURI, ELibraryInstallStatus.NOT_DEPLOYED);
            // ModuleStatusProvider.putStatus(mvnURI, ELibraryInstallStatus.NOT_INSTALLED);
        }

        return deployStatus[0];
    }

    public static void copyDefaultMavenURI(String text) {
        Clipboard clipBoard = new Clipboard(Display.getCurrent());
        TextTransfer textTransfer = TextTransfer.getInstance();
        clipBoard.setContents(new Object[] { text }, new Transfer[] { textTransfer });
    }
}
