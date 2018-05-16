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
package org.talend.core.ui;

import java.util.List;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

/**
 * is notified on lifecycle step of the RCP E4 application This call is reference in the
 * org.eclipse.core.runtime.products extension point with a property
 * 
 * <pre>
 * {@code
 *    <property
 *             name="lifeCycleURI"
 *             value="bundleclass://org.talend.core.ui/org.talend.core.ui.TalendStudioLifeCycleManager">
 *    </property>
 * }
 * </pre>
 */
public class TalendStudioLifeCycleManager {

    /**
     * change all toolbar visible attribute to true. This is to fix the bug TDI-32030
     *
     * @param fModelService, service used to find app elements
     * @param mApp, application model to look for toolbars
     */
    @ProcessAdditions
    // parameters are injected
    // TODO remove this when switching to Eclipse Mars
    public void processAddition(EModelService fModelService, MApplication mApp) {
        List<MToolBar> allToolBars = fModelService.findElements(mApp, null, MToolBar.class, null);
        for (MToolBar toolbar : allToolBars) {
            toolbar.setVisible(true);
        }
    }
}
