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
package org.talend.commons.runtime.helper;

import java.util.Collection;
import java.util.Collections;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.runtime.service.ComponentsInstallComponent;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class LocalComponentInstallHelper {

    public static ComponentsInstallComponent getComponent() {
        BundleContext bc = FrameworkUtil.getBundle(LocalComponentInstallHelper.class).getBundleContext();
        Collection<ServiceReference<ComponentsInstallComponent>> patchComps = Collections.emptyList();
        try {
            patchComps = bc.getServiceReferences(ComponentsInstallComponent.class, null);
        } catch (InvalidSyntaxException e) {
            CommonExceptionHandler.process(e);
        }

        for (ServiceReference<ComponentsInstallComponent> sr : patchComps) {
            ComponentsInstallComponent pc = bc.getService(sr);
            return pc;
        }
        return null;
    }
}
