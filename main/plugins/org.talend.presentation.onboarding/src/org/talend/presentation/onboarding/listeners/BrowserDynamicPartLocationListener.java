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
package org.talend.presentation.onboarding.listeners;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.talend.presentation.onboarding.ui.html.DynamicHtmlURL;
import org.talend.presentation.onboarding.ui.html.DynamicURLParser;

/**
 * created by cmeng on Sep 18, 2015 Detailled comment
 *
 */
public class BrowserDynamicPartLocationListener implements LocationListener {

    @Override
    public void changing(LocationEvent event) {
        String url = event.location;
        if (url == null) {
            return;
        }

        DynamicURLParser parser = new DynamicURLParser(url);
        if (parser.hasIntroUrl()) {
            // stop URL first.
            event.doit = false;
            // execute the action embedded in the IntroURL
            DynamicHtmlURL introURL = parser.getIntroURL();
            introURL.execute();
        }

    }

    @Override
    public void changed(LocationEvent event) {
        String url = event.location;
        if (url == null) {
            return;
        }

        // guard against unnecessary History updates.
        Browser browser = (Browser) event.getSource();
        if (browser.getData("navigation") != null //$NON-NLS-1$
                && browser.getData("navigation").equals("true")) { //$NON-NLS-1$ //$NON-NLS-2$
            return;
        }

    }

}
