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
package org.talend.core.utils;

import org.talend.commons.runtime.extension.ExtensionRegistryReader;

/**
 * Template implementation of a registry reader that creates objects representing registry contents. Typically, an
 * extension contains one element, but this reader handles multiple elements per extension.
 * 
 * To start reading the extensions from the registry for an extension point, call the method <code>readRegistry</code>.
 * 
 * To read children of an IConfigurationElement, call the method <code>readElementChildren</code> from your
 * implementation of the method <code>readElement</code>, as it will not be done by default.
 * 
 * @since 3.2
 */
public abstract class RegistryReader extends ExtensionRegistryReader {

    protected RegistryReader(String aPluginId, String anExtensionPoint) {
        super(aPluginId, anExtensionPoint);
    }

}
