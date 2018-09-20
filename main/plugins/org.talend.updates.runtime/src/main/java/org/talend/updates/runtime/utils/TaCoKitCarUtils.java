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
package org.talend.updates.runtime.utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.service.ITaCoKitUpdateService;
import org.talend.updates.runtime.service.ITaCoKitUpdateService.ICarInstallationResult;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class TaCoKitCarUtils {

    public static ICarInstallationResult installCars(File carFolder, IProgressMonitor progress) throws Exception {
        if (carFolder.exists()) {
            File[] files = carFolder.listFiles();
            if (files != null && 0 < files.length) {
                ITaCoKitUpdateService tckUpdateService = ITaCoKitUpdateService.getInstance();
                if (tckUpdateService == null) {
                    throw new Exception(Messages.getString("ITaCoKitUpdateService.exception.notFound", //$NON-NLS-1$
                            ITaCoKitUpdateService.class.getSimpleName()));
                }
                List<File> fileList = Arrays.asList(files);
                return tckUpdateService.installCars(fileList, false, progress);
            }
        }
        return null;
    }

}
