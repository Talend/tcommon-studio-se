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
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IService;
import org.talend.core.model.general.Project;

/**
 * DOC cmeng class global comment. Detailled comment
 */
public interface IStudioLiteP2Service extends IService {

    public static final String PROP_USE_NEW_UPDATE_SYSTEM = "talend.studio.update.useNewUpdateSystem";

    /**
     * Preload to improve performance
     */
    void preload();

    CheckUpdateHook checkForUpdate(IProgressMonitor monitor) throws Exception;

    ValidateRequiredFeaturesHook validateRequiredFeatures(IProgressMonitor monitor, Project proj) throws Exception;

    UpdateSiteConfig getUpdateSiteConfig();

    public static IStudioLiteP2Service get() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IStudioLiteP2Service.class)) {
            return GlobalServiceRegister.getDefault().getService(IStudioLiteP2Service.class);
        }
        return null;
    }

    public static interface CheckUpdateHook {

        boolean hasUpdate();

        boolean performUpdate(IProgressMonitor monitor) throws Exception;

    }

    public static interface ValidateRequiredFeaturesHook {

        boolean isMissingRequiredFeatures();

        List<String> getMissingRequiredFeatures();

        boolean installMissingRequiredFeatures(IProgressMonitor monitor) throws Exception;

    }

    public static interface UpdateSiteConfig {

        URI getRelease() throws Exception;

        void setRelease(URI uri) throws Exception;

        URI getUpdate() throws Exception;

        void setUpdate(URI uri) throws Exception;

    }

}
