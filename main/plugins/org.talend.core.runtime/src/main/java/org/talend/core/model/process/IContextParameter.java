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
package org.talend.core.model.process;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: IContextParameter.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public interface IContextParameter {

    public static final String BUILT_IN = "built-in"; //$NON-NLS-1$

    public String[] getValueList();

    public void setValueList(String[] list);

    public IContext getContext();

    public void setContext(IContext context);

    public String getName();

    public void setName(final String name);

    public String getPrompt();

    public void setPrompt(final String prompt);

    public String getType();

    public void setType(final String type);

    public String getValue();

    public String getDisplayValue();

    public void setValue(final String value);

    public void setInternalValue(String value);

    public String getComment();

    public void setComment(final String comment);

    public boolean isPromptNeeded();

    public void setPromptNeeded(boolean promptNeeded);

    public void setScriptCode(final String scriptCode);

    public String getScriptCode();

    public IContextParameter clone();

    public boolean sameAs(IContextParameter contextParam);

    public String getSource();

    public void setSource(final String sourceName);

    public boolean isBuiltIn();
}
