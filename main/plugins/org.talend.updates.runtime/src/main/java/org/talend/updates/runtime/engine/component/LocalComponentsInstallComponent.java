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

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
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

    private File componentFolder = null;

    private List<File> failedComponents;

    public boolean needRelaunch() {
        return needRelaunch;
    }

    public List<File> getFailedComponents() {
        return failedComponents;
    }

    private void reset() {
        needRelaunch = false;
        failedComponents = new ArrayList<File>();
        installedMessage = null;
    }

    public void setComponentFolder(File componentFolder) {
        this.componentFolder = componentFolder;
    }

    protected File getInstallingComponentFolder() {
        if (componentFolder == null) {
            // use same folder of user component from preference setting.
            ScopedPreferenceStore preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE,
                    "org.talend.designer.codegen"); //$NON-NLS-1$
            // same key as IComponentPreferenceConstant.USER_COMPONENTS_FOLDER
            String userCompFolder = preferenceStore.getString("USER_COMPONENTS_FOLDER"); //$NON-NLS-1$
            if (!StringUtils.isEmpty(userCompFolder)) {
                File compFolder = new File(userCompFolder);
                if (compFolder.exists()) {
                    componentFolder = compFolder;
                }
            }

            if (componentFolder == null) {
                try {
                    componentFolder = new File(Platform.getInstallLocation().getDataArea(FOLDER_COMPS).getPath());
                } catch (IOException e) {
                    //
                }
            }
            if (componentFolder == null) {
                componentFolder = new File(System.getProperty("user.dir") + '/' + FOLDER_COMPS); //$NON-NLS-1$
            }
        }
        return componentFolder;
    }

    File getInstalledComponentFolder() {
        return new File(getInstallingComponentFolder(), FOLDER_INSTALLED);
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
            final File componentFolder = getInstallingComponentFolder();
            if (componentFolder == null || !componentFolder.exists()) {
                return false;
            }
            if (componentFolder.isDirectory()) {
                Map<File, Set<InstalledUnit>> successUnits = new HashMap<File, Set<InstalledUnit>>();

                final File[] updateFiles = componentFolder.listFiles(); // no children folders recursively.
                if (updateFiles != null && updateFiles.length > 0) {
                    for (File f : updateFiles) {
                        // must be file, and update site.
                        if (f.isFile() && UpdatesHelper.isComponentUpdateSite(f)) {

                            P2Installer installer = new P2Installer() {

                                @Override
                                public Set<InstalledUnit> installPatchFolder(File updatesiteFolder, boolean keepChangeConfigIni)
                                        throws IOException, ProvisionException {
                                    final Set<InstalledUnit> installed = super.installPatchFolder(updatesiteFolder,
                                            keepChangeConfigIni);
                                    if (!installed.isEmpty()) { // install success
                                        // sync the component libraries
                                        syncLibraries(updatesiteFolder);
                                    }
                                    return installed;
                                }

                            };

                            try {
                                final Set<InstalledUnit> installed = installer.installPatchFile(f, true);
                                if (installed != null && !installed.isEmpty()) { // install success
                                    successUnits.put(f, installed);

                                    afterInstall(f);
                                }
                            } catch (Exception e) { // sometime, if reinstall it, will got one exception also.
                                // won't block others to install.
                                if (!CommonsPlugin.isHeadless()) {
                                    ExceptionHandler.process(e);
                                }
                                failedComponents.add(f);
                            }
                        }
                    }
                }

                if (!successUnits.isEmpty()) { // have one component install ok.
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
                        messages.append("\n  file: " + f.getName());
                        messages.append('\n');
                        Set<InstalledUnit> set = successUnits.get(f);
                        for (InstalledUnit unit : set) {
                            messages.append("  > bundle:" + unit.getBundle() + " , version=" + unit.getVersion());
                            messages.append('\n');
                        }
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

    protected void afterInstall(File f) throws IOException {
        // try to move install success to installed folder
        final File installedComponentFolder = getInstalledComponentFolder();
        FilesUtils.copyFile(f, new File(installedComponentFolder, f.getName()));
        f.delete(); // delete original file.
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
