// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.ui.branding;

import org.talend.core.runtime.i18n.Messages;

/**
 * wzhang class global comment. Detailled comment
 */
public abstract class AbstractBrandingService extends AbstractCommonBrandingService {

    public String getJobLicenseHeader(String version) {

        String contents = Messages.getString("AbstractBrandingService_job_license_header_content", //$NON-NLS-1$
                this.getFullProductName(), version);
        return contents;
    }

    // public String getRoutineLicenseHeader(String version) {
    //        String contents = Messages.getString("AbstractBrandingService.routines_license_header_content1", //$NON-NLS-1$
    // this.getFullProductName(), version);
    // return contents;
    // }

    @Override
    public boolean isPoweredbyTalend() {
        return false;
    }

    @Override
    public boolean isPoweredOnlyCamel() {
        return false;
    }

    @Override
    public String getUserManuals() {
        return "DI";
    }
}
