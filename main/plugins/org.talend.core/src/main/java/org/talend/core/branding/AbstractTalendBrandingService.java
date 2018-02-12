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
package org.talend.core.branding;

import org.talend.core.ui.branding.AbstractBrandingService;

/**
 * DOC xtan class global comment. Detailled comment
 */
public abstract class AbstractTalendBrandingService extends AbstractBrandingService {

    public String getFullProductName() {
        return getProductName() + " " + getOptionName(); //$NON-NLS-1$
    }

    public boolean isPoweredbyTalend() {
        return true;
    }

    public boolean isPoweredOnlyCamel() {
        return false;
    }
}
