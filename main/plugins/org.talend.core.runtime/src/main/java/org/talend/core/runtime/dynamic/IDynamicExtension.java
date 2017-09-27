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
package org.talend.core.runtime.dynamic;

import java.util.List;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public interface IDynamicExtension extends IDynamicAttribute {

    public static final String ATTR_EXTENSION_POINT = "point"; //$NON-NLS-1$

    public static final String ATTR_EXTENSION_ID = "id"; //$NON-NLS-1$

    public static final String TAG_NAME = "extension"; //$NON-NLS-1$

    public String toXmlString() throws Exception;

    public List<IDynamicConfiguration> getConfigurations();

    public void addConfiguration(IDynamicConfiguration config);

    public void removeConfiguration(IDynamicConfiguration config);

    public IDynamicConfiguration createEmptyConfiguration();

    public void setExtensionPoint(String extensionPoint);

    public String getExtensionPoint();

    public void setExtensionId(String extensionId);

    public String getExtensionId();

}
