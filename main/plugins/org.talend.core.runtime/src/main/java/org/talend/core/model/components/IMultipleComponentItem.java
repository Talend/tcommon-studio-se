// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

import java.util.List;

public interface IMultipleComponentItem {

    public List<IMultipleComponentConnection> getOutputConnections();

    public List<IMultipleComponentConnection> getInputConnections();

    public String getName();

    public void setName(String name);

    public String getComponent();

    public void setComponent(String component);
}
