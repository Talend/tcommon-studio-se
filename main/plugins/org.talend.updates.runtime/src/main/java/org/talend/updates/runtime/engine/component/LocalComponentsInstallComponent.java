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
package org.talend.updates.runtime.engine.component;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.runtime.service.ComponentsInstallComponent;
import org.talend.commons.utils.resource.UpdatesHelper;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.nexus.component.ComponentIndexBean;
import org.talend.updates.runtime.nexus.component.ComponentIndexManager;
import org.talend.updates.runtime.service.ITaCoKitUpdateService;
import org.talend.updates.runtime.service.ITaCoKitUpdateService.ICarInstallationResult;
import org.talend.updates.runtime.utils.PathUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class LocalComponentsInstallComponent implements ComponentsInstallComponent {

    protected InstallComponentMessages messages = new InstallComponentMessages();

    private File userComponentFolder = null;

    private List<File> failedComponents;

    private boolean isLogin = false;

    private File tmpM2RepoFolder;

    @Override
    public void setLogin(boolean login) {
        this.isLogin = login;
    }

    @Override
    public boolean needRelaunch() {
        return messages.isNeedRestart();
    }

    @Override
    public List<File> getFailedComponents() {
        if (failedComponents == null) {
            failedComponents = new ArrayList<>();
        }
        return failedComponents;
    }

    private void reset() {
        if (failedComponents != null) {
            failedComponents.clear();
        }
        failedComponents = new ArrayList<>();
        messages.reset();
    }

    @Override
    public void setComponentFolder(File componentFolder) {
        this.userComponentFolder = componentFolder;
    }

    protected File getUserComponentFolder() {
        if (userComponentFolder == null) {
            // use same folder of user component from preference setting.
            ScopedPreferenceStore preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE,
                    "org.talend.designer.codegen"); //$NON-NLS-1$
            // same key as IComponentPreferenceConstant.USER_COMPONENTS_FOLDER
            String userCompFolder = preferenceStore.getString("USER_COMPONENTS_FOLDER"); //$NON-NLS-1$
            if (!StringUtils.isEmpty(userCompFolder)) {
                File compFolder = new File(userCompFolder);
                if (compFolder.exists()) {
                    userComponentFolder = compFolder;
                }
            }

            if (userComponentFolder == null) {
                try {
                    userComponentFolder = new File(Platform.getInstallLocation().getDataArea(FOLDER_COMPS).getPath());
                } catch (IOException e) {
                    //
                }
            }
            if (userComponentFolder == null) {
                userComponentFolder = new File(System.getProperty("user.dir") + '/' + FOLDER_COMPS); //$NON-NLS-1$
            }
        }
        return userComponentFolder;
    }

    /**
     * same as PatchLocalInstallerManager.getInstallingPatchesFolder
     */
    protected File getPatchesFolder() {
        return PathUtils.getPatchesFolder();
    }

    /**
     * If have patches to be installed, will ask restart.
     */
    @Override
    public boolean install() {
        if (Platform.inDevelopmentMode()) { // for dev, no need install patches.
            return false;
        }
        return doInstall();
    }

    protected boolean doInstall() {
        reset();

        try {
            installFromFolder(getUserComponentFolder());
            if (isLogin) { // try to install the components from patches folder.
                // because in patches folder, will do after install user components.
                installFromFolder(getPatchesFolder());
            }
            return getFailureMessage() == null && getInstalledMessages() != null;
        } catch (Exception e) {
            if (!CommonsPlugin.isHeadless()) {
                // make sure to popup error dialog for studio
                ExceptionHandler.process(e);
            }
        }
        return false;
    }

    protected void installFromFolder(final File componentBaseFolder) {
        if (componentBaseFolder == null || !componentBaseFolder.exists() || !componentBaseFolder.isDirectory()) {
            return;
        }
        final ComponentIndexManager indexManager = new ComponentIndexManager();

        final File[] updateFiles = componentBaseFolder.listFiles(); // no children folders recursively.
        if (updateFiles != null && updateFiles.length > 0) {
            List<File> carFiles = new LinkedList<>();
            for (File f : updateFiles) {
                // must be file, and update site.
                if (f.isFile()) {
                    ExtraFeature feature = null;
                    try {
                        if (UpdatesHelper.isComponentUpdateSite(f)) {
                            // get ComponentP2ExtraFeature from update site file
                            ComponentIndexBean indexBean = indexManager.create(f);
                            if (indexBean == null) {
                                getFailedComponents().add(f);
                                continue;
                            }
                            ComponentP2ExtraFeature componentfeature = createComponentFeature(f);
                            componentfeature.setLogin(isLogin);
                            feature = componentfeature;
                        } else if (isTaCoKitCar(f)) {
                            carFiles.add(f);
                            continue;
                        } else {
                            continue;
                        }
                        NullProgressMonitor progressMonitor = new NullProgressMonitor();
                        // boolean installedBefore = feature.isInstalled(progressMonitor);
                        if (feature.canBeInstalled(progressMonitor)) {
                            List<URI> repoUris = new ArrayList<>(1);
                            repoUris.add(PathUtils.getP2RepURIFromCompFile(f));
                            messages.analyzeStatus(feature.install(progressMonitor, repoUris));
                            messages.setNeedRestart(feature.needRestart());
                        }
                    } catch (Exception e) { // sometime, if reinstall it, will got one exception also.
                        // won't block others to install.
                        if (!CommonsPlugin.isHeadless()) {
                            ExceptionHandler.process(e);
                        }
                        getFailedComponents().add(f);
                    }
                }
            }
            installTaCoKitCars(messages, carFiles, getFailedComponents());
        }
    }

    private void installTaCoKitCars(InstallComponentMessages msg, List<File> carFiles, List<File> failedComponents) {
        if (carFiles != null && !carFiles.isEmpty()) {
            ITaCoKitUpdateService tckUpdateService = null;
            try {
                tckUpdateService = ITaCoKitUpdateService.getInstance();
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
            if (tckUpdateService == null) {
                ExceptionHandler.process(new Exception(
                        Messages.getString("ITaCoKitService.exception.notFound", ITaCoKitUpdateService.class.getSimpleName()))); //$NON-NLS-1$
                return;
            }
            try {
                ICarInstallationResult instResult = tckUpdateService.installCars(carFiles, true, new NullProgressMonitor());
                msg.setNeedRestart(instResult.needRestart());
                Map<File, IStatus> installedStatus = instResult.getInstalledStatus();
                if (installedStatus != null && !installedStatus.isEmpty()) {
                    for (IStatus status : installedStatus.values()) {
                        msg.analyzeStatus(status);
                    }
                }
                failedComponents.addAll(instResult.getFailedFile());
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
    }

    private boolean isTaCoKitCar(File f) {
        boolean isTaCoKitCar = false;
        try {
            ITaCoKitUpdateService tckUpdateService = ITaCoKitUpdateService.getInstance();
            if (tckUpdateService != null) {
                isTaCoKitCar = tckUpdateService.isCar(f, new NullProgressMonitor());
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return isTaCoKitCar;
    }

    protected ComponentP2ExtraFeature createComponentFeature(File f) {
        return new ComponentLocalP2ExtraFeature(f);
    }

    @Override
    public String getInstalledMessages() {
        return messages.getInstalledMessage();
    }

    @Override
    public String getFailureMessage() {
        return messages.getFailureMessage();
    }

}
