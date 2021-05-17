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
package org.talend.updates.runtime.utils;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.service.ITaCoKitUpdateService;
import org.talend.updates.runtime.service.ITaCoKitUpdateService.ICarInstallationResult;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class TaCoKitCarUtils {

    public static ICarInstallationResult installCars(File carFolder, final IProgressMonitor monitor) throws Exception {
        return installCars(carFolder, monitor, true);
    }

    public static ICarInstallationResult installCars(File carFolder, final IProgressMonitor monitor, boolean cancellable)
            throws Exception {
        if (carFolder.exists()) {
            File[] files = carFolder.listFiles();
            if (files != null && 0 < files.length) {
                ITaCoKitUpdateService tckUpdateService = ITaCoKitUpdateService.getInstance();
                if (tckUpdateService == null) {
                    throw new Exception(Messages.getString("ITaCoKitUpdateService.exception.notFound", //$NON-NLS-1$
                            ITaCoKitUpdateService.class.getSimpleName()));
                }
                List<File> fileList = Arrays.asList(files);

                IProgressMonitor proxyMonitor = monitor;
                if (monitor != null && !cancellable) {
                    proxyMonitor = (IProgressMonitor) Proxy.newProxyInstance(monitor.getClass().getClassLoader(),
                            new Class[] { IProgressMonitor.class }, new InvocationHandler() {

                                @Override
                                public Object invoke(Object obj, Method method, Object[] args) throws Throwable {
                                    if (method == null) {
                                        return null;
                                    }
                                    if (StringUtils.equals(method.getName(), "isCanceled")) { //$NON-NLS-1$
                                        return Boolean.FALSE;
                                    }
                                    return method.invoke(monitor, args);
                                }
                            });
                }
                if (proxyMonitor == null) {
                    proxyMonitor = new NullProgressMonitor();
                }

                return tckUpdateService.installCars(fileList, false, proxyMonitor);
            }
        }
        return null;
    }
    
    public static ICarInstallationResult deployCars(File carFolder, final IProgressMonitor monitor, boolean cancellable)
            throws Exception {
        if (carFolder.exists()) {
            File[] files = carFolder.listFiles();
            if (files != null && 0 < files.length) {
                ITaCoKitUpdateService tckUpdateService = ITaCoKitUpdateService.getInstance();
                if (tckUpdateService == null) {
                    throw new Exception(Messages.getString("ITaCoKitUpdateService.exception.notFound", //$NON-NLS-1$
                            ITaCoKitUpdateService.class.getSimpleName()));
                }
                List<File> fileList = Arrays.asList(files);

                IProgressMonitor proxyMonitor = monitor;
                if (monitor != null && !cancellable) {
                    proxyMonitor = (IProgressMonitor) Proxy.newProxyInstance(monitor.getClass().getClassLoader(),
                            new Class[] { IProgressMonitor.class }, new InvocationHandler() {

                                @Override
                                public Object invoke(Object obj, Method method, Object[] args) throws Throwable {
                                    if (method == null) {
                                        return null;
                                    }
                                    if (StringUtils.equals(method.getName(), "isCanceled")) { //$NON-NLS-1$
                                        return Boolean.FALSE;
                                    }
                                    return method.invoke(monitor, args);
                                }
                            });
                }
                if (proxyMonitor == null) {
                    proxyMonitor = new NullProgressMonitor();
                }

                return tckUpdateService.deployCars(fileList, false, proxyMonitor);
            }
        }
        return null;
    }

}
