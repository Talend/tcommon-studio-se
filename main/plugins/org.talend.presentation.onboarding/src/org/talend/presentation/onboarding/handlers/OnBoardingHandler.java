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
package org.talend.presentation.onboarding.handlers;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.talend.presentation.onboarding.exceptions.OnBoardingExceptionHandler;
import org.talend.presentation.onboarding.i18n.Messages;
import org.talend.presentation.onboarding.ui.managers.OnBoardingManager;
import org.talend.presentation.onboarding.ui.runtimedata.OnBoardingPageBean;
import org.talend.presentation.onboarding.ui.runtimedata.OnBoardingPerspectiveBean;
import org.talend.presentation.onboarding.utils.ObjectBox;
import org.talend.presentation.onboarding.utils.OnBoardingUtils;

/**
 * created by cmeng on Sep 18, 2015 Detailled comment
 *
 */
public class OnBoardingHandler extends AbstractHandler {

    private String jsonDocId = null;

    private String perspId = null;

    @Override
    public boolean isEnabled() {
        if (!OnBoardingUtils.isSupportBrowser()) {
            return false;
        }

        ObjectBox<String> jsonDocIdBox = new ObjectBox<String>();
        OnBoardingPerspectiveBean perspBean = OnBoardingUtils.getDefaultPerspectiveBean(jsonDocIdBox);
        if (perspBean == null) {
            return false;
        }

        jsonDocId = jsonDocIdBox.value;
        perspId = perspBean.getPerspId();

        List<OnBoardingPageBean> pages = perspBean.getPages();
        if (pages == null || pages.isEmpty()) {
            return false;
        }
        return super.isEnabled();
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        boolean isAgree = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(),
                Messages.getString("OnBoardingHandler.confirm.title"), //$NON-NLS-1$
                Messages.getString("OnBoardingHandler.confirm.msg")); //$NON-NLS-1$
        if (!isAgree) {
            return null;
        }
        IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        String currentPerspectiveId = OnBoardingUtils.getCurrentSelectedPerspectiveId(workbenchWindow);
        try {
            if (!perspId.equals(currentPerspectiveId)) {
                workbenchWindow.getWorkbench().showPerspective(perspId, workbenchWindow);
            }
            workbenchWindow.getActivePage().resetPerspective();
            OnBoardingManager manager = new OnBoardingManager(workbenchWindow.getShell());
            manager.createDefaultUIAndResourceManagers();
            manager.setDocId(jsonDocId);
            manager.setPerspId(perspId);
            manager.reloadResource();
            manager.onBoarding(0);
        } catch (Throwable e) {
            OnBoardingExceptionHandler.process(e);
        }
        return null;
    }

}
