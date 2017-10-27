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
package org.talend.presentation.onboarding.ui.composites;

import java.io.IOException;
import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.talend.presentation.onboarding.exceptions.OnBoardingExceptionHandler;
import org.talend.presentation.onboarding.listeners.BrowserDynamicPartLocationListener;
import org.talend.presentation.onboarding.ui.html.HtmlContentHelper;
import org.talend.presentation.onboarding.ui.managers.OnBoardingManager;
import org.talend.presentation.onboarding.ui.runtimedata.OnBoardingPresentationData;
import org.talend.presentation.onboarding.utils.OnBoardingConstants;
import org.talend.presentation.onboarding.utils.OnBoardingUtils;

/**
 * created by cmeng on Sep 14, 2015 Detailled comment
 *
 */
public class OnBoardingContentComposite extends Composite {

    private OnBoardingManager onBoardingManager;

    private Browser browser;

    private OnBoardingPresentationData currentPresentationData;

    private HtmlContentHelper contentHelper;

    private URL htmlPath;

    public OnBoardingContentComposite(Composite parent, int style, OnBoardingManager obManager) {
        super(parent, style);
        this.onBoardingManager = obManager;
        contentHelper = new HtmlContentHelper(onBoardingManager);
        this.setLayout(new FormLayout());
        createControl(this);
    }

    public void refreshDocIfNeeded() {
        if (currentPresentationData != onBoardingManager.getCurrentSelectedPresentationData()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    final String content = getHtmlContent();
                    Display.getDefault().asyncExec(new Runnable() {

                        @Override
                        public void run() {
                            browser.setText(content);
                        }
                    });
                }

            }).start();
        }
    }

    private Composite createControl(Composite parent) {
        Composite contentControl = new Composite(parent, SWT.NONE);
        FormData formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.top = new FormAttachment(0, 0);
        formData.bottom = new FormAttachment(100, 0);
        formData.right = new FormAttachment(100, 0);
        contentControl.setLayoutData(formData);
        contentControl.setLayout(new FillLayout());

        browser = new Browser(contentControl, SWT.NONE);
        browser.addLocationListener(new BrowserDynamicPartLocationListener());
        loadBrowserHtmlContent();

        return contentControl;
    }

    private String getHtmlContent() {
        String content = null;
        currentPresentationData = onBoardingManager.getCurrentSelectedPresentationData();
        if (htmlPath == null) {
            htmlPath = OnBoardingUtils.getResourceLocalURL(OnBoardingConstants.ON_BOARDING_VIEW_HTML_PATH);
        }
        try {
            content = contentHelper.getHtmlContent(htmlPath);
        } catch (IOException e1) {
            OnBoardingExceptionHandler.process(e1);
        }
        return content;
    }

    private void loadBrowserHtmlContent() {
        String content = getHtmlContent();
        browser.setText(content);
    }
}
