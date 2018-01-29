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
package org.talend.rcp.intro.starting;

import java.util.Properties;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroSite;
import org.eclipse.ui.intro.config.IIntroAction;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * 
 * DOC wchen class global comment. Detailled comment
 */
public class ShowViewsAction implements IIntroAction {

    public static final String SHOW_JOB_DESIGNER = "SHOW_JOB_DESIGNER";

    public static final String SHOW_PALETTE = "SHOW_PALETTE";

    public static final String SHOW_COMPONENT = "SHOW_COMPONENT";

    public static final String SHOW_RUN_JOB = "SHOW_RUN_JOB";

    public static final String KEY_TYPE = "type";

    private static String componentViewId = "org.talend.designer.core.ui.views.properties.ComponentSettingsView";

    private static String gefViewId = "org.eclipse.gef.ui.palette_view";

    private static String repositoryViewId = IRepositoryView.VIEW_ID;

    private static String processViewId = "org.talend.designer.runprocess.ui.views.processview";

    public void run(IIntroSite site, Properties params) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (page != null) {
            if (SHOW_JOB_DESIGNER.equals(params.get(KEY_TYPE))) {
                showView(page, repositoryViewId);
            } else if (SHOW_PALETTE.equals(params.get(KEY_TYPE))) {
                showView(page, gefViewId);
            } else if (SHOW_COMPONENT.equals(params.get(KEY_TYPE))) {
                showView(page, componentViewId);
            } else if (SHOW_RUN_JOB.equals(params.get(KEY_TYPE))) {
                showView(page, processViewId);
            }
        }
    }

    private void showView(IWorkbenchPage page, String id) {
        IViewPart view = page.findView(id);
        try {
            view = page.showView(id);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        page.findView(id).setFocus();
    }

}
