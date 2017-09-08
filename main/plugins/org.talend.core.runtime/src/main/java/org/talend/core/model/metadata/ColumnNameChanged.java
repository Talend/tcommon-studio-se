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
package org.talend.core.model.metadata;

/**
 * 
 * DOC smallet IODataComponentContainer class global comment. Detailled comment <br/>
 * 
 * $Id: IODataComponent.java 961 2006-12-12 04:42:46 +0000 (mar., 12 d茅c. 2006) nrousseau $
 * 
 */
public class ColumnNameChanged {

    private String oldName;

    private String newName;

    /**
     * DOC smallet ColumnNameChanged constructor comment.
     * 
     * @param oldName
     * @param newName
     */
    public ColumnNameChanged(String oldName, String newName) {
        super();
        this.oldName = oldName;
        this.newName = newName;
    }

    /**
     * Getter for newName.
     * 
     * @return the newName
     */
    public String getNewName() {
        return this.newName;
    }

    /**
     * Getter for oldName.
     * 
     * @return the oldName
     */
    public String getOldName() {
        return this.oldName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Column changed : " + oldName + "->" + newName; //$NON-NLS-1$//$NON-NLS-2$
    }

}
