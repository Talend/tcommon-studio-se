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
package org.talend.registration.license;

import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.BusinessException;
import org.talend.repository.ui.login.connections.ConnectionUserPerReader;

public class LicenseManagement {

    // LICENSE_VALIDATION_DONE = 1 : registration OK
    private static final double LICENSE_VALIDATION_DONE = 2;

    public static void acceptLicense() throws BusinessException {
        PlatformUI.getPreferenceStore().setValue("LICENSE_VALIDATION_DONE", 1); //$NON-NLS-1$
        ConnectionUserPerReader read = ConnectionUserPerReader.getInstance();
        read.saveLiscenseManagement();
    }

    public static boolean isLicenseValidated() {
        initPreferenceStore();
        ConnectionUserPerReader read = ConnectionUserPerReader.getInstance();
        if (!read.readLicenseManagement().equals("1")) { //$NON-NLS-1$
            return false;
        }
        return true;
    }

    private static void initPreferenceStore() {}

}
