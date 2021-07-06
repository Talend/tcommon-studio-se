// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.service;

import java.net.URI;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.commons.CommonsPlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IService;
import org.talend.core.model.general.Project;

/**
 * DOC cmeng class global comment. Detailled comment
 */
public interface IStudioLiteP2Service extends IService {

    public static final String PROP_USE_NEW_UPDATE_SYSTEM = "talend.studio.update.useNewUpdateSystem";

    public static final String CONFIG_STORAGE_FOLDER = "talend/studioLite/";

    public static final String BUNDLES_INFOS_STORAGE_FOLDER = CONFIG_STORAGE_FOLDER + "bundlesInfo/";

    public static final int RESULT_SKIP = 0;

    public static final int RESULT_DONE = 1;

    /**
     * cancel current action
     */
    public static final int RESULT_CANCEL = 2;

    void setProfileKey(String profKey) throws Exception;

    String getProfileKey() throws Exception;

    /**
     * Preload to improve performance
     */
    void preload(IProgressMonitor monitor) throws Exception;

    String getSettingsFilePath() throws Exception;

    UpdateSiteConfig getUpdateSiteConfig(IProgressMonitor monitor) throws Exception;;

    CheckUpdateHook checkForUpdate(IProgressMonitor monitor) throws Exception;

    boolean performUpdate(IProgressMonitor monitor, CheckUpdateHook hook) throws Exception;

    ValidateRequiredFeaturesHook validateRequiredFeatures(IProgressMonitor monitor, Project proj) throws Exception;

    /**
     * show required features, and choose what to do
     * 
     * @return {@link IStudioLiteP2Service#RESULT_DONE}<br/>
     * {@link IStudioLiteP2Service#RESULT_SKIP}<br/>
     * {@link IStudioLiteP2Service#RESULT_CANCEL}<br/>
     */
    int showInstallRequiredFeaturesWizard(ValidateRequiredFeaturesHook hook, Project proj) throws Exception;

    ValidatePotentialFeaturesHook validatePotentialFeatures(IProgressMonitor monitor, Project proj) throws Exception;

    /**
     * selected features will be write into the required feature list of project
     * 
     * @param hook
     * @param proj
     * @return {@link IStudioLiteP2Service#RESULT_UPDATED}<br/>
     * {@link IStudioLiteP2Service#RESULT_SKIP}<br/>
     * {@link IStudioLiteP2Service#RESULT_CANCEL}<br/>
     */
    int showUpdateProjectRequiredFeaturesWizard(ValidatePotentialFeaturesHook hook, Project proj) throws Exception;

    int adaptFeaturesForProject(IProgressMonitor monitor, Project proj) throws Exception;

    public static IStudioLiteP2Service get() {
        boolean forceLoad = Boolean.getBoolean("talend.studio.studiolite.p2.enable");
        if (!forceLoad) {
            if (CommonsPlugin.isHeadless() || CommonsPlugin.isJUnitTest() || CommonsPlugin.isTUJTest()
                    || CommonsPlugin.isScriptCmdlineMode()) {
                return null;
            }
        }
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IStudioLiteP2Service.class)) {
            return GlobalServiceRegister.getDefault().getService(IStudioLiteP2Service.class);
        }
        return null;
    }

    public static interface IInstallableUnitInfo {

        String getName();

        String getId();

        List<String> getRequired();

    }

    public static interface CheckUpdateHook {

        boolean hasUpdate();

        boolean isReCheckForUpdate();

        boolean isRestart();

        Collection<?> getUninstalledIUs();

        boolean performUpdate(IProgressMonitor monitor) throws Exception;

    }

    public static interface ValidatePotentialFeaturesHook {

        boolean hasPotentialFeatures();

        List<IInstallableUnitInfo> getPotentialFeatures();

    }

    public static interface ValidateRequiredFeaturesHook {

        boolean isMissingRequiredFeatures();

        List<IInstallableUnitInfo> getMissingRequiredFeatures();

    }

    public static interface UpdateSiteConfig {

        URI getRelease(IProgressMonitor monitor) throws Exception;

        void setRelease(IProgressMonitor monitor, URI uri) throws Exception;

        URI getUpdate(IProgressMonitor monitor) throws Exception;

        void setUpdate(IProgressMonitor monitor, URI uri) throws Exception;

    }

}
