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
package org.talend.updates.runtime.service;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IService;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.model.interfaces.ITaCoKitCarFeature;
import org.talend.updates.runtime.nexus.component.ComponentIndexBean;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public interface ITaCoKitUpdateService extends IService {

    public static final String FOLDER_CAR = "car"; //$NON-NLS-1$

    ITaCoKitCarFeature generateExtraFeature(File file, IProgressMonitor monitor) throws Exception;

    ITaCoKitCarFeature generateExtraFeature(ComponentIndexBean indexBean, IProgressMonitor monitor) throws Exception;

    ICarInstallationResult installCars(Collection<File> files, boolean share, IProgressMonitor monitor) throws Exception;

    ICarInstallationResult installCarFeatures(Collection<ITaCoKitCarFeature> features, boolean share, IProgressMonitor monitor)
            throws Exception;

    Collection<ExtraFeature> filterUpdatableFeatures(Collection<ExtraFeature> features, IProgressMonitor monitor)
            throws Exception;

    boolean isCar(File file, IProgressMonitor monitor) throws Exception;

    public static ITaCoKitUpdateService getInstance() throws Exception {
        GlobalServiceRegister register = GlobalServiceRegister.getDefault();
        if (register.isServiceRegistered(ITaCoKitUpdateService.class)) {
            return (ITaCoKitUpdateService) register.getService(ITaCoKitUpdateService.class);
        }
        return null;
    }

    static interface ICarInstallationResult {

        boolean needRestart();

        Map<File, IStatus> getInstalledStatus();

        List<File> getFailedFile();

    }

}
