package org.talend.presentation.onboarding.handlers;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroManager;
import org.eclipse.ui.intro.IIntroPart;
import org.talend.presentation.onboarding.ui.managers.OnBoardingManager;
import org.talend.presentation.onboarding.ui.runtimedata.OnBoardingPerspectiveBean;
import org.talend.presentation.onboarding.utils.ObjectBox;
import org.talend.presentation.onboarding.utils.OnBoardingConstants;
import org.talend.presentation.onboarding.utils.OnBoardingUtils;

public class OnBoardingStartup implements IStartup {

    @Override
    public void earlyStartup() {
        showOnboardingPresentationIfNeeded();
    }

    private void showOnboardingPresentationIfNeeded() {
        if (!OnBoardingUtils.isSupportBrowser()) {
            return;
        }
        boolean notShowOnBoarding = PlatformUI.getPreferenceStore().getBoolean(
                OnBoardingConstants.PREFERENCE_NOT_SHOW_ONBOARDING_AT_STARTUP);
        if (!notShowOnBoarding) {
            Display.getDefault().syncExec(new Runnable() {

                @Override
                public void run() {
                    IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                    IIntroManager introManager = workbenchWindow.getWorkbench().getIntroManager();
                    if (introManager.hasIntro()) {
                        IIntroPart introPart = introManager.getIntro();
                        if (introPart != null) {
                            introManager.closeIntro(introPart);
                        }
                    }
                }
            });

            Display.getDefault().asyncExec(new Runnable() {

                @Override
                public void run() {
                    ObjectBox<String> jsonDocBox = new ObjectBox<String>();
                    OnBoardingPerspectiveBean perspBean = OnBoardingUtils.getDefaultPerspectiveBean(jsonDocBox);
                    if (perspBean == null) {
                        return;
                    }
                    IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                    String curPerspId = OnBoardingUtils.getCurrentSelectedPerspectiveId(workbenchWindow);
                    String perspIdToShow = perspBean.getPerspId();
                    if (curPerspId == null || perspIdToShow == null) {
                        return;
                    }

                    if (!curPerspId.trim().equals(perspIdToShow)) {
                        return;
                    }
                    OnBoardingManager onBoardingManager = new OnBoardingManager(workbenchWindow.getShell());
                    onBoardingManager.createDefaultUIAndResourceManagers();
                    onBoardingManager.setDocId(jsonDocBox.value);
                    onBoardingManager.setPerspId(perspIdToShow);
                    onBoardingManager.reloadResource();
                    onBoardingManager.onBoarding(0);
                }
            });
            PlatformUI.getPreferenceStore().setValue(OnBoardingConstants.PREFERENCE_NOT_SHOW_ONBOARDING_AT_STARTUP, true);
        }
    }
}
