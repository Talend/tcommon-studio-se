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
package org.talend.updates.runtime.model;

import org.eclipse.equinox.p2.core.ProvisionException;

public class P2ExtraFeatureException extends Exception {

    private static final long serialVersionUID = -1761956032920259160L;

    public P2ExtraFeatureException(ProvisionException e) {
        super(e);
    }

}
