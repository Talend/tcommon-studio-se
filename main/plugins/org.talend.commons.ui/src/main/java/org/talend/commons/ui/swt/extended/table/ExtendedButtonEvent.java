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
package org.talend.commons.ui.swt.extended.table;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ExtendedButtonEvent {

    private boolean before;

    // private IExtendedControlEventType type;

    /**
     * DOC amaumont ExtendedModelEvent constructor comment.
     * 
     * @param b
     */
    public ExtendedButtonEvent(boolean before) {
        super();
        this.before = before;
        // this.type = type;
    }

    /**
     * Getter for before.
     * 
     * @return the before
     */
    public boolean isBefore() {
        return this.before;
    }

    // /**
    // * Getter for type.
    // *
    // * @return the type
    // */
    // public IExtendedControlEventType getType() {
    // return this.type;
    // }
    //
}
