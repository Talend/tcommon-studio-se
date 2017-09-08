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
package org.talend.core.ui.webService;

import java.util.Iterator;
import java.util.Vector;

import org.talend.core.service.IWebServiceTos;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class WebServiceTosSaveManager {

    public static WebServiceTosSaveManager instance;

    private Vector<IWebServiceTos> listener = new Vector<IWebServiceTos>();

    public static WebServiceTosSaveManager getInstance() {
        if (instance == null) {
            instance = new WebServiceTosSaveManager();
        }
        return instance;
    }

    public void addWebServiceSaveListener(IWebServiceTos webServicen) {
        if (!listener.contains(webServicen))
            listener.add(webServicen);
    }

    public void removeWebSeviceListenr(IWebServiceTos webServicen) {
        listener.remove(webServicen);
    }

    public void saveValue() {
        Iterator<IWebServiceTos> it = listener.iterator();
        while (it.hasNext()) {
            IWebServiceTos lis = (IWebServiceTos) it.next();
            lis.saveValue();
        }
    }
}
