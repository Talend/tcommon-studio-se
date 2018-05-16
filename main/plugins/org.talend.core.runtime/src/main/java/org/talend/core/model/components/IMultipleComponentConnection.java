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
package org.talend.core.model.components;


/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public interface IMultipleComponentConnection {

    public String getConnectionType();

    public void setConnectionType(String connectionType);

    public String getNameTarget();

    public void setNameTarget(String nameTarget);

    public IMultipleComponentItem getSource();

    public void setSource(IMultipleComponentItem source);

    public IMultipleComponentItem getTarget();

    public void setTarget(IMultipleComponentItem target);

}
