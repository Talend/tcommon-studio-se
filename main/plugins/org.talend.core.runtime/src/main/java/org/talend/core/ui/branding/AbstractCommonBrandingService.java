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
package org.talend.core.ui.branding;

import java.net.URL;

import org.talend.core.runtime.i18n.Messages;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractCommonBrandingService implements IBrandingService {

    @Override
    public String getStartingBrowserId() {
        // default is StartingBrowser implement in org.talend.rcp
        return "org.talend.rcp.intro.starting.StartingBrowser"; //$NON-NLS-1$
    }

    @Override
    public URL getStartingHtmlURL() {
        // for example, if set the startingPage html in the service bundle in "content/startingPage.html"
        // return FrameworkUtil.getBundle(this.getClass()).getEntry("content/startingPage.html");
        return null;
    }

    @Override
    public void createStartingContent(String id, Element parent) {
        Document dom = parent.getOwnerDocument();
        if (StartingConstants.PRODUCT_NAME.equals(id)) {
            parent.appendChild(dom.createTextNode(getProductName()));
        } else if (StartingConstants.OPTION_NAME.equals(id)) {
            parent.appendChild(dom.createTextNode(getOptionName()));
        }
    }

    @Override
    public String getRoutineLicenseHeader(String version) {
        String contents = Messages.getString("AbstractBrandingService.routines_license_header_content2014", //$NON-NLS-1$
                this.getFullProductName(), version);
        return contents;
    }
}
