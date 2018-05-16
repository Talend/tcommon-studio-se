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
package org.talend.core.runtime.dynamic;

import java.util.List;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public interface IDynamicConfiguration extends IDynamicAttribute {

    public String toXmlString() throws Exception;

    public void setConfigurationName(String name);

    public void addChildConfiguration(IDynamicConfiguration configuration);

    public void removeChildConfiguration(IDynamicConfiguration configuration);

    public List<IDynamicConfiguration> getChildConfigurations();

}
