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

import java.util.List;

import org.eclipse.emf.common.util.EList;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: IContextManager.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public interface IContextManager {

    public List<IContext> getListContext();

    public void setListContext(List<IContext> listContext);

    public IContext getDefaultContext();

    public IContext getContext(String name);

    public void setDefaultContext(IContext context);

    public void addContextListener(IContextListener listener);

    public void removeContextListener(IContextListener listener);

    public void fireContextsChangedEvent();

    public boolean checkValidParameterName(String oldParameterName, String newParameterName);

    public void saveToEmf(EList contextTypeList);

    public void loadFromEmf(EList contextTypeList, String defaultContextName);

    public boolean sameAs(IContextManager contextManager);
}
