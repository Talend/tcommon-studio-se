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

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: IContext.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public interface IContext {

    String DEFAULT = "Default"; //$NON-NLS-1$

    public void setName(String name);

    public String getName();

    public boolean isConfirmationNeeded();

    public void setConfirmationNeeded(boolean confirmationNeeded);

    public List<IContextParameter> getContextParameterList();

    public void setContextParameterList(List<IContextParameter> contextParameterList);

    public IContextParameter getContextParameter(String parameterName);

    /**
     * Added by Marvin Wang on Mar.8, 2012, using {@link IContextParameter#getSource()} +
     * {@link IContextParameter#getName()} can only identify an unique {@link IContextParameter}. That indicates one
     * source can has many varibles, but the variable name must be unique. For different source, the variable names can
     * be same.
     * 
     * @param sourceId
     * @param paraName
     * @return
     */
    public IContextParameter getContextParameter(String sourceId, String paraName);

    /**
     * Gets the <code>IContextParameter</code>s by given <code>parameterName</code>, caz name is not the unique
     * attribute for the object which implements <code>IContextParameter</code>.
     * 
     * @param parameterName
     * @return
     */
    // public List<IContextParameter> getContextParameters(String parameterName);

    public IContext clone();

    public boolean sameAs(IContext context);
}
