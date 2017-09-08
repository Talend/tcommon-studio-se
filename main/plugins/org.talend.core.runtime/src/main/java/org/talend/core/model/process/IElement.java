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
 * $Id: IElement.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public interface IElement {

    public IElementParameter getElementParameter(String name);

    public List<? extends IElementParameter> getElementParameters();

    public List<? extends IElementParameter> getElementParametersWithChildrens();

    public void setElementParameters(List<? extends IElementParameter> elementsParameters);

    public boolean isReadOnly();

    public void setReadOnly(boolean readOnly);

    public boolean isForceReadOnly();

    public void setForceReadOnly(boolean readOnly);

    public String getElementName();

    public Object getPropertyValue(String name);

    public void setPropertyValue(String name, Object value);

    public IElementParameter getElementParameterFromField(EParameterFieldType dbtable);

    public IElementParameter getElementParameterFromField(EParameterFieldType propertyType, EComponentCategory category);

    public Object getPropertyValue(String name, String paramName);

}
