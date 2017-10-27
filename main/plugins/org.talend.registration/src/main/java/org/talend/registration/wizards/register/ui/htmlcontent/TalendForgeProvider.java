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
package org.talend.registration.wizards.register.ui.htmlcontent;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Priority;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.intro.config.IIntroContentProviderSite;
import org.eclipse.ui.intro.config.IIntroXHTMLContentProvider;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.ui.html.TalendHtmlModelUtil;
import org.talend.core.prefs.IIntroHTMLConstants;
import org.talend.registration.RegistrationPlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * created by kongxiaohan on Mar 24, 2015 Detailled comment
 *
 */
public class TalendForgeProvider implements IIntroXHTMLContentProvider {

    private String oldCountry;

    private int countryToSelect = 0;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.intro.config.IIntroContentProvider#init(org.eclipse.ui.intro.config.IIntroContentProviderSite)
     */
    @Override
    public void init(IIntroContentProviderSite site) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.intro.config.IIntroContentProvider#createContent(java.lang.String, java.io.PrintWriter)
     */
    @Override
    public void createContent(String id, PrintWriter out) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.intro.config.IIntroContentProvider#createContent(java.lang.String,
     * org.eclipse.swt.widgets.Composite, org.eclipse.ui.forms.widgets.FormToolkit)
     */
    @Override
    public void createContent(String id, Composite parent, FormToolkit toolkit) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.intro.config.IIntroContentProvider#dispose()
     */
    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.intro.config.IIntroXHTMLContentProvider#createContent(java.lang.String, org.w3c.dom.Element)
     */
    @Override
    public void createContent(String id, Element parent) {
        if (ContentConstants.INSERT_DATA.equals(id)) {
            String countryList[] = initiateCountryList();
            Document dom = parent.getOwnerDocument();
            for (int i = 0; i < countryList.length; i++) {

                Properties att = new Properties();
                if (i == countryToSelect) {
                    att.setProperty(IIntroHTMLConstants.ATTRIBUTE_SELECTED, "true");
                }
                Element option = TalendHtmlModelUtil.createElement(dom, TalendHtmlModelUtil.TAG_OPTION, att);
                option.setTextContent(countryList[i]);
                parent.appendChild(option);
            }
        } else if (ContentConstants.TALEND_FORGE_IMAGE.equals(id)) {
            URL imgURL = RegistrationPlugin.getDefault().getBundle().getEntry("content/img/talendforge.png"); //$NON-NLS-1$
            if (imgURL != null) {
                try {
                    imgURL = FileLocator.toFileURL(imgURL);
                } catch (IOException e) {
                    CommonExceptionHandler.process(e, Priority.WARN);
                }
                String result = imgURL.toExternalForm();
                if (result.startsWith("file:/")) { //$NON-NLS-1$
                    if (result.startsWith("file:///") == false) { //$NON-NLS-1$
                        result = "file:///" + result.substring(6); //$NON-NLS-1$
                    }
                }
                Document dom = parent.getOwnerDocument();
                Properties att = new Properties();
                att.setProperty(IIntroHTMLConstants.ATTRIBUTE_SRC, result);
                Element img = TalendHtmlModelUtil.createElement(dom, TalendHtmlModelUtil.TAG_IMG, att);
                parent.appendChild(img);
            }
        } else if (ContentConstants.OK_HINT_IMAGE.equals(id)) {
            URL imgURL = RegistrationPlugin.getDefault().getBundle().getEntry("content/img/ok.png"); //$NON-NLS-1$
            if (imgURL != null) {
                try {
                    imgURL = FileLocator.toFileURL(imgURL);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String result = imgURL.toExternalForm();
                if (result.startsWith("file:/")) { //$NON-NLS-1$
                    if (result.startsWith("file:///") == false) { //$NON-NLS-1$
                        result = "file:///" + result.substring(6); //$NON-NLS-1$
                    }
                }
                Document dom = parent.getOwnerDocument();
                Properties att = new Properties();
                att.setProperty(IIntroHTMLConstants.ATTRIBUTE_SRC, result);
                Element img = TalendHtmlModelUtil.createElement(dom, TalendHtmlModelUtil.TAG_IMG, att);
                parent.appendChild(img);
            }
        }
    }

    private String[] initiateCountryList() {
        SortedSet<String> countryList = new TreeSet<String>();
        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getDisplayCountry().compareTo("") != 0) { //$NON-NLS-1$
                countryList.add(locale.getDisplayCountry());
            }
        }

        String defaultCountry = Locale.getDefault().getDisplayCountry();
        int i = 0;

        if (oldCountry != null) {
            for (String country : countryList) {

                if (country.equals(oldCountry)) {
                    countryToSelect = i;
                    break;
                }
                i++;
            }
        } else {
            for (String country : countryList) {
                if (country.equals(defaultCountry)) {
                    countryToSelect = i;
                    break;
                }
                i++;
            }
        }
        return countryList.toArray(new String[] {});
    }
}
