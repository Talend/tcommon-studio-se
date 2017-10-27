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
package org.talend.core.model.repository.extension;

import org.talend.commons.utils.workbench.extensions.ExtensionPointLimiterImpl;
import org.talend.commons.utils.workbench.extensions.IExtensionPointLimiter;

/**
 * Provides extension points for this plug-in.<br/>
 * 
 * $Id: ExtensionPointFactory.java 911 2006-12-08 06:50:50 +0000 (星期五, 08 十二月 2006) bqian $
 * 
 */
public class ExtensionPointFactory {

    public static final IExtensionPointLimiter COMPONENTS_PROVIDER = new ExtensionPointLimiterImpl(
            "org.talend.core.components_provider", "ComponentsFactory", 1, 1); //$NON-NLS-1$ //$NON-NLS-2$

    public static final IExtensionPointLimiter EXTERNAL_COMPONENT = new ExtensionPointLimiterImpl(
            "org.talend.core.external_component", "ExternalComponent", 1, 1); //$NON-NLS-1$ //$NON-NLS-2$

    public static final IExtensionPointLimiter ACTIONS_GROUPS = new ExtensionPointLimiterImpl(
            "org.talend.core.actions", "Group"); //$NON-NLS-1$ //$NON-NLS-2$

}
