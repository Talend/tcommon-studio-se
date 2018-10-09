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
package org.talend.commons.runtime.service;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.IProgressMonitor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.i18n.internal.Messages;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public interface ITaCoKitService {

    String reload(IProgressMonitor monitor) throws Exception;

    boolean isTaCoKitCar(File file, IProgressMonitor monitor) throws Exception;

    void checkMigration(final IProgressMonitor monitor) throws Exception;

    public static ITaCoKitService getInstance() throws Exception {
        BundleContext bc = FrameworkUtil.getBundle(ITaCoKitService.class).getBundleContext();
        Collection<ServiceReference<ITaCoKitService>> tacokitServices = Collections.emptyList();
        try {
            tacokitServices = bc.getServiceReferences(ITaCoKitService.class, null);
        } catch (InvalidSyntaxException e) {
            CommonExceptionHandler.process(e);
        }

        if (tacokitServices != null) {
            if (1 < tacokitServices.size()) {
                ExceptionHandler.process(new Exception(
                        Messages.getString("ITaCoKitService.exception.multipleInstance", ITaCoKitService.class.getName()))); //$NON-NLS-1$
            }
            for (ServiceReference<ITaCoKitService> sr : tacokitServices) {
                ITaCoKitService tacokitService = bc.getService(sr);
                if (tacokitService != null) {
                    return tacokitService;
                }
            }
        }
        return null;
    }

}
