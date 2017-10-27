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
package org.talend.core.model.process;

import java.util.EventListener;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: IContextListener.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public interface IContextListener extends EventListener {

    /**
     * 
     * If any or all contexts have changed this event will be sent.
     */
    public void contextsChanged();
}
