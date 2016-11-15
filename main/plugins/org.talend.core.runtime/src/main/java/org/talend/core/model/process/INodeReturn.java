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
package org.talend.core.model.process;

import java.util.List;

public interface INodeReturn {

    public abstract String getAvailability();

    public abstract void setAvailability(final String availability);

    public abstract String getVarName();

    public abstract void setVarName(final String varName);

    public abstract String getDisplayName();

    public abstract void setDisplayName(final String displayName);

    public abstract String getName();

    public abstract void setName(final String name);

    public abstract String getType();

    public abstract void setType(final String type);

    public abstract String getDisplayType();

    public abstract void setShowIf(final String showIf);

    public abstract boolean isShow(List<? extends IElementParameter> listParam);
}
