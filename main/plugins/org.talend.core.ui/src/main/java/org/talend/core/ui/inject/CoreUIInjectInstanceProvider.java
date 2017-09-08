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
package org.talend.core.ui.inject;

import javax.inject.Inject;

import org.apache.log4j.Priority;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.services.IStylingEngine;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;

/**
 * created by cmeng on Jan 20, 2015 Detailled comment
 *
 */
public class CoreUIInjectInstanceProvider {

    @Inject
    private IStylingEngine cssStylingEngine;

    private static CoreUIInjectInstanceProvider coreUIInjectInstanceProvider;

    public static CoreUIInjectInstanceProvider getInstance() {
        if (coreUIInjectInstanceProvider == null) {
            try {
                coreUIInjectInstanceProvider = new CoreUIInjectInstanceProvider();
                IWorkbench workbench = PlatformUI.getWorkbench();
                IEclipseContext activeContext = ((IEclipseContext) workbench.getService(IEclipseContext.class)).getActiveLeaf();

                ContextInjectionFactory.inject(coreUIInjectInstanceProvider, activeContext);
            } catch (Exception e) {
                ExceptionHandler.process(e, Priority.WARN);
                coreUIInjectInstanceProvider = null;
            }
        }
        return coreUIInjectInstanceProvider;
    }

    public IStylingEngine getCSSStylingEngine() {
        return cssStylingEngine;
    }
}
