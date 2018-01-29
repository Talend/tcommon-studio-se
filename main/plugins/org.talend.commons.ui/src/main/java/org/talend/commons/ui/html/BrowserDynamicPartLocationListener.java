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
package org.talend.commons.ui.html;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;

/**
 * DOC talend class global comment. Detailled comment
 */
public class BrowserDynamicPartLocationListener implements LocationListener {

    public void changing(LocationEvent event) {
        String url = event.location;
        if (url == null)
            return;

        DynamicURLParser parser = new DynamicURLParser(url);
        if (parser.hasIntroUrl()) {
            // stop URL first.
            event.doit = false;
            // execute the action embedded in the IntroURL
            DynamicHtmlURL introURL = parser.getIntroURL();
            introURL.execute();
        }

    }

    public void changed(LocationEvent event) {
        String url = event.location;
        if (url == null)
            return;

        // guard against unnecessary History updates.
        Browser browser = (Browser) event.getSource();
        if (browser.getData("navigation") != null //$NON-NLS-1$
                && browser.getData("navigation").equals("true")) //$NON-NLS-1$ //$NON-NLS-2$
            return;

        DynamicURLParser parser = new DynamicURLParser(url);
        if (!parser.hasProtocol() || parser.getHost() == null || parser.getHost().equals("")) //$NON-NLS-1$
            // This will filter out two navigation events fired by the browser
            // on a setText. (about:blank and
            // res://C:\WINDOWS\System32\shdoclc.dll/navcancl.htm on windows,
            // and file:/// on Linux)
            return;

    }

}
