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
public interface IDynamicPlugin extends IDynamicAttribute {

    public static final String TAG_NAME = "plugin"; //$NON-NLS-1$

    public IDynamicExtension addExtension(IDynamicExtension extension);

    public IDynamicExtension addExtension(int index, IDynamicExtension extension);

    /**
     * may have extensions with different id while same extension point
     * 
     * @param extensionPoint
     * @return
     */
    public List<IDynamicExtension> removeExtensions(String extensionPoint);

    public IDynamicExtension getExtension(String extensionPoint, String extensionId, boolean createIfNotExist);

    public List<IDynamicExtension> getAllExtensions();

    public String toXmlString() throws Exception;

    public IDynamicPluginConfiguration getPluginConfiguration();

    public void setPluginConfiguration(IDynamicPluginConfiguration pluginConfiguration);

}
