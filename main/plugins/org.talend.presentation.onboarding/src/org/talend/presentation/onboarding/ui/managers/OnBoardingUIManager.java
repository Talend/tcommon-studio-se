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
package org.talend.presentation.onboarding.ui.managers;

import java.util.Collection;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.talend.presentation.onboarding.ui.runtimedata.OnBoardingCommandBean;
import org.talend.presentation.onboarding.ui.runtimedata.OnBoardingPageBean;
import org.talend.presentation.onboarding.ui.runtimedata.OnBoardingPresentationData;
import org.talend.presentation.onboarding.ui.shells.HighlightShell;
import org.talend.presentation.onboarding.ui.shells.OnBoardingShell;
import org.talend.presentation.onboarding.utils.WidgetFinder;

/**
 * created by cmeng on Sep 11, 2015 Detailled comment
 *
 */
public class OnBoardingUIManager {

    private HighlightShell highlightShell;

    private OnBoardingShell onBoardingShell;

    private OnBoardingManager onBoardingManager;

    private Shell shells[];

    private volatile boolean isOpened = false;

    public OnBoardingUIManager(OnBoardingManager onBoardingManager) {
        this.onBoardingManager = onBoardingManager;
        onBoardingManager.setUiManager(this);
        highlightShell = new HighlightShell(onBoardingManager.getParentShell(), onBoardingManager);
        onBoardingShell = new OnBoardingShell(highlightShell.getHighlightShell(), onBoardingManager);
        shells = new Shell[] { this.onBoardingManager.getParentShell() };
    }

    public void refreshOnBoarding() {
        if (!isOpened) {
            isOpened = true;
            highlightShell.open();
        }
        OnBoardingPresentationData presData = onBoardingManager.getCurrentSelectedPresentationData();
        if (presData == null) {
            return;
        }
        Widget widget = null;
        OnBoardingPageBean docBean = presData.getPageBean();
        String[] cssIds = docBean.getCssIds();
        widget = getWidget(cssIds);

        boolean isOnBoardingShellOpened = onBoardingShell.isOpened();
        if (isOnBoardingShellOpened) {
            onBoardingShell.setVisible(false);
        }

        onBoardingShell.setFocusedWidget(widget);
        onBoardingShell.setPresentationData(presData);
        onBoardingShell.refreshInUIThread();

        highlightShell.focusOnWidgetInUIThread(widget);

        // if (isOpened) {
        // onBoardingShell.setVisible(true);
        // }
    }

    public void refreshShellsBound() {
        highlightShell.refreshHighlightShell();
        onBoardingShell.refresh();
    }

    public void onHighlightShellMoveCompleted() {
        if (!onBoardingShell.getOnBoardingShell().isDisposed()) {
            onBoardingShell.setVisible(true);
        }
    }

    public void onShowAnimationDone() {
        OnBoardingPresentationData presData = onBoardingManager.getCurrentSelectedPresentationData();
        OnBoardingCommandBean onShowAnimationDoneCmd = presData.getPageBean().getOnShowAnimationDone();
        onBoardingManager.executeCommand(onShowAnimationDoneCmd);
    }

    private Widget getWidget(String[] cssIds) {
        Widget widget = null;
        if (cssIds != null && 0 < cssIds.length) {
            for (String cssId : cssIds) {
                if (cssId == null) {
                    continue;
                }
                Collection<Widget> widgets = WidgetFinder.findWidgetsByCSSInUIThread(cssId.trim(), shells);
                if (widgets != null && !widgets.isEmpty()) {
                    widget = widgets.iterator().next();
                    break;
                }
            }
        }
        return widget;
    }

    public void close() {
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                highlightShell.close();

                if (onBoardingShell.getParentShell() != highlightShell.getHighlightShell()) {
                    onBoardingShell.close();
                }
            }
        });
        isOpened = false;

    }

    public Shell[] getShells() {
        return this.shells;
    }

    public void setShells(Shell[] shells) {
        this.shells = shells;
    }

}
