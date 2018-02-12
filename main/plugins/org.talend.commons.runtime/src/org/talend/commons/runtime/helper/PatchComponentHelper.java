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
package org.talend.commons.runtime.helper;

import java.util.Collection;
import java.util.Collections;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.runtime.service.PatchComponent;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class PatchComponentHelper {

    public static PatchComponent getPatchComponent() {
        BundleContext bc = FrameworkUtil.getBundle(PatchComponentHelper.class).getBundleContext();
        Collection<ServiceReference<PatchComponent>> patchComps = Collections.emptyList();
        try {
            patchComps = bc.getServiceReferences(PatchComponent.class, null);
        } catch (InvalidSyntaxException e) {
            CommonExceptionHandler.process(e);
        }

        for (ServiceReference<PatchComponent> sr : patchComps) {
            PatchComponent pc = bc.getService(sr);
            return pc;
        }
        return null;
    }
}
