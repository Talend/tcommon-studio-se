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
package org.talend.presentation.onboarding.ui.html.actions;

import java.util.Properties;

import org.eclipse.ui.intro.IIntroSite;
import org.eclipse.ui.intro.config.IIntroAction;
import org.talend.presentation.onboarding.exceptions.OnBoardingExceptionHandler;
import org.talend.presentation.onboarding.i18n.Messages;
import org.talend.presentation.onboarding.ui.managers.OnBoardingManager;

/**
 * created by cmeng on Sep 16, 2015 Detailled comment
 *
 */
public class OnBoardingHtmlAction implements IIntroAction {

    public static final String ACTION_TYPE = "TYPE"; //$NON-NLS-1$

    public static final String ACTION_SHOW_PAGE = "SHOW_PAGE"; //$NON-NLS-1$

    public static final String ACTION_SKIP = "SKIP"; //$NON-NLS-1$

    public static final String ACTION_BACK = "BACK"; //$NON-NLS-1$

    public static final String ACTION_NEXT = "NEXT"; //$NON-NLS-1$

    public static final String PAGE_INDEX = "PAGE_INDEX"; //$NON-NLS-1$

    public static final String MANAGER_ID = "MANAGER_ID"; //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.intro.config.IIntroAction#run(org.eclipse.ui.intro.IIntroSite, java.util.Properties)
     */
    @Override
    public void run(IIntroSite site, Properties params) {
        OnBoardingManager obManager = OnBoardingManager.getRegistedManager(params.getProperty(MANAGER_ID));
        if (obManager == null) {
            OnBoardingExceptionHandler.process(new Throwable(Messages.getString("OnBoardingHtmlAction.OnBoardingManager.null"))); //$NON-NLS-1$
            return;
        }
        if (ACTION_SHOW_PAGE.equals(params.get(ACTION_TYPE))) {
            String pageIndex = params.getProperty(PAGE_INDEX);
            if (pageIndex != null && !pageIndex.trim().isEmpty()) {
                try {
                    int index = Integer.valueOf(pageIndex);
                    obManager.onBoarding(index);
                } catch (Throwable e) {
                    OnBoardingExceptionHandler.process(e);
                }
            }
        } else if (ACTION_SKIP.equals(params.get(ACTION_TYPE))) {
            obManager.close();
        } else if (ACTION_NEXT.equals(params.get(ACTION_TYPE))) {
            obManager.onBoarding(obManager.getCurrentSelectedPresentationDataIndex() + 1);
        } else if (ACTION_BACK.equals(params.get(ACTION_TYPE))) {
            obManager.onBoarding(obManager.getCurrentSelectedPresentationDataIndex() - 1);
        }

    }
}
