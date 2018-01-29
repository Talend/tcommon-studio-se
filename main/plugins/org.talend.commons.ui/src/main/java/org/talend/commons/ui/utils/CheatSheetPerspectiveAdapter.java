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
package org.talend.commons.ui.utils;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PerspectiveAdapter;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.cheatsheets.views.CheatSheetView;
import org.eclipse.ui.internal.cheatsheets.views.ViewUtilities;

/**
 * DOC yyi class global comment. Detailled comment
 */
public class CheatSheetPerspectiveAdapter extends PerspectiveAdapter {

    /**
     * CheatSheetPerspectiveAdapter constructor.
     * 
     */
    public CheatSheetPerspectiveAdapter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.PerspectiveAdapter#perspectivePreDeactivate(org.eclipse.ui.IWorkbenchPage,
     * org.eclipse.ui.IPerspectiveDescriptor)
     */
    @Override
    public void perspectivePreDeactivate(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
        super.perspectivePreDeactivate(page, perspective);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.PerspectiveAdapter#perspectiveActivated(org.eclipse.ui.IWorkbenchPage,
     * org.eclipse.ui.IPerspectiveDescriptor)
     */
    @Override
    public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
        // 1.if it is not DQ perspective, hide cheat sheet view.
        // 2.if it is DQ perspective and the last statu of cheet sheet view is opened, restore cheat sheet.
        if (!perspective.getId().equals(CheatSheetUtils.DQ_PERSPECTIVE_ID)) {
            CheatSheetView cheetSheet = CheatSheetUtils.getInstance().findCheetSheet(
                    "org.talend.datacleansing.core.ui.dqcheatsheet"); //$NON-NLS-1$
            if (null != cheetSheet) {
                // Always hide cheatsheet first on switching perspective
                CheatSheetUtils.getInstance().hideCheetSheet(cheetSheet);
            }
        } else if (CheatSheetUtils.getInstance().isOpenedCheatSheet()) {
            restoreCheetSheet();
        }

        super.perspectiveActivated(page, perspective);
    }

    /**
     * show the cheet sheet view.
     */
    private void restoreCheetSheet() {
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                CheatSheetView view = ViewUtilities.showCheatSheetView();

                // MOD klliu bug TDQ-4130 if studio is started by other user(DI/MDM),the cheetSheetID will not be
                // initalized.
                // Then it is changed to DQ,cheetSheetID will be null,but CheatSheetView is not null,so that will show
                // an empty view.
                // Therefore DQ_CHEATSHEET_START_ID will be used to fill CheatSheetView.
                IWorkbenchPart activePart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
                if (null != view) {
                    view.setInput("org.talend.datacleansing.core.ui.dqcheatsheet");//$NON-NLS-1$
                    if (CheatSheetUtils.getInstance().isFirstTime()) {
                        CheatSheetUtils.getInstance().maxDisplayCheatSheetView(view);
                    }
                }
                if (null != activePart) {
                    activePart.setFocus();
                }
            }

        });
    }

}
