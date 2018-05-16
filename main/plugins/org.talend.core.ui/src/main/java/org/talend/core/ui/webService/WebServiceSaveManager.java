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
package org.talend.core.ui.webService;

import java.util.Iterator;
import java.util.Vector;

import org.talend.core.service.IWebService;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class WebServiceSaveManager {

    public static WebServiceSaveManager instance;

    private Vector<IWebService> listener = new Vector<IWebService>();

    public static WebServiceSaveManager getInstance() {
        if (instance == null) {
            instance = new WebServiceSaveManager();
        }
        return instance;
    }

    public void addWebServiceSaveListener(IWebService webServicen) {
        if (!listener.contains(webServicen))
            listener.add(webServicen);
    }

    public void removeWebSeviceListenr(IWebService webServicen) {
        listener.remove(webServicen);
    }

    public void saveValue() {
        Iterator<IWebService> it = listener.iterator();
        while (it.hasNext()) {
            IWebService lis = (IWebService) it.next();
            lis.saveValue();
        }
    }
}
