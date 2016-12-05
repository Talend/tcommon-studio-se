// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.updates.runtime.engine.component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.runtime.service.ComponentsInstallComponent;
import org.talend.commons.utils.resource.UpdatesHelper;
import org.talend.librariesmanager.prefs.LibrariesManagerUtils;
import org.talend.updates.runtime.engine.InstalledUnit;
import org.talend.updates.runtime.engine.P2Installer;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class LocalComponentsInstallComponent implements ComponentsInstallComponent {

    private boolean needRelaunch;

    private String installedMessage;

    public boolean needRelaunch() {
        return needRelaunch;
    }

    private void reset() {
        needRelaunch = false;
        installedMessage = null;
    }

    File getInstallingComponentFolder() {
        try {
            return new File(Platform.getInstallLocation().getDataArea(FOLDER_COMPS).getPath());
        } catch (IOException e) {
            //
        }
        return new File(System.getProperty("user.dir") + '/' + FOLDER_COMPS); //$NON-NLS-1$
    }

    /**
     * If have patches to be installed, will ask restart.
     */
    public boolean install() {
        if (Platform.inDevelopmentMode()) { // for dev, no need install patches.
            return false;
        }
        reset();

        try {
            final File file = getInstallingComponentFolder();
            if (file == null) {
                return false;
            }
            if (file.exists() && file.isDirectory()) {
                boolean success = false;
                Map<File, Set<InstalledUnit>> successUnits = new HashMap<File, Set<InstalledUnit>>();
                List<File> failedUnits = new ArrayList<File>();

                final File[] updateFiles = UpdatesHelper.findUpdateFiles(file);
                if (updateFiles != null && updateFiles.length > 0) {
                    for (File f : updateFiles) {
                        if (f.isFile()) {
                            P2Installer installer = new P2Installer() {

                                @Override
                                public Set<InstalledUnit> installPatchFolder(File updatesiteFolder, boolean keepChangeConfigIni)
                                        throws IOException, ProvisionException {
                                    final Set<InstalledUnit> installed = super.installPatchFolder(updatesiteFolder,
                                            keepChangeConfigIni);
                                    if (!installed.isEmpty()) {
                                        // sync the component libraries
                                        syncLibraries(updatesiteFolder);
                                    }
                                    return installed;
                                }

                            };
                            try {
                                final Set<InstalledUnit> installed = installer.installPatchFile(f, true);
                                if (installed != null && !installed.isEmpty()) {
                                    success = true;
                                    successUnits.put(f, installed);
                                }
                            } catch (Exception e) { // sometime, if reinstall it, will got one exception also.
                                // won't block others to install.
                                if (!CommonsPlugin.isHeadless()) {
                                    ExceptionHandler.process(e);
                                }
                                failedUnits.add(f);
                            }
                        }
                    }
                }
                if (success) { // have one component install ok.
                    needRelaunch = true;
                }

                // messages
                StringBuffer messages = new StringBuffer(100);
                if (successUnits.isEmpty()) {
                    installedMessage = null; // no message
                } else {
                    messages.append("Installed success components:\n");
                    List<File> succussFiles = new ArrayList(successUnits.keySet());
                    Collections.sort(succussFiles);

                    for (File f : succussFiles) {
                        messages.append("  file: " + f.getName());
                        Set<InstalledUnit> set = successUnits.get(f);
                        for (InstalledUnit unit : set) {
                            messages.append("  b=" + unit.getBundle() + " , v=" + unit.getVersion());
                        }
                        messages.append('\n');
                    }

                    installedMessage = messages.toString();
                }
                return !successUnits.isEmpty();
            }
        } catch (Exception e) {
            if (!CommonsPlugin.isHeadless()) {
                // make sure to popup error dialog for studio
                ExceptionHandler.process(e);
            }
        }
        return false;
    }

    void syncLibraries(File updatesiteFolder) throws IOException {
        // sync to product lib/java
        final File productLibFolder = new File(LibrariesManagerUtils.getLibrariesPath());
        File updatesiteLibFolder = new File(updatesiteFolder, LibrariesManagerUtils.LIB_JAVA_SUB_FOLDER);
        if (updatesiteLibFolder.exists()) {
            final File[] listFiles = updatesiteLibFolder.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                if (!productLibFolder.exists()) {
                    productLibFolder.mkdirs();
                }
                FilesUtils.copyFolder(updatesiteLibFolder, productLibFolder, false, null, null, false);
            }
        }
    }

    @Override
    public String getInstalledMessages() {
        return installedMessage;
    }

}
