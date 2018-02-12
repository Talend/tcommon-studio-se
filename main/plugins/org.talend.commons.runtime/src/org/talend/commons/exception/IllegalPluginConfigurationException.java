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
package org.talend.commons.exception;

/**
 * Defines a fatal plug-in configuration. For exemple, an extension point defined to have one implementation has more or
 * less.<br/>
 * 
 * $Id$
 * 
 */
public class IllegalPluginConfigurationException extends FatalException {

    @SuppressWarnings("unused")//$NON-NLS-1$
    private static final long serialVersionUID = 1L;

    public IllegalPluginConfigurationException(String message) {
        super(message);
    }

    public IllegalPluginConfigurationException(Exception ex) {
        super(ex);
    }
}
