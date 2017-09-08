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
package org.talend.commons.utils.data.reflection;

import org.talend.commons.i18n.internal.Messages;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ReflectionPropertyException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 3425844870424877957L;

    public ReflectionPropertyException(Class reflectedClass, String reflectedProperty, boolean isGetter, Throwable cause) {

        super(Messages.getString("ReflectionPropertyException.Access.ErrorMsg3", (isGetter ? "getter" : "setter"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                reflectedProperty, (reflectedClass != null ? reflectedClass.getName() : "class null")), cause); //$NON-NLS-1$
    }
}
