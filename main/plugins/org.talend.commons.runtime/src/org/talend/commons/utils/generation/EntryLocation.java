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
package org.talend.commons.utils.generation;

import org.talend.commons.i18n.internal.Messages;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class EntryLocation {

    public String tableName;

    public String columnName;

    public EntryLocation() {
        super();
    }

    /**
     * DOC amaumont Couple constructor comment.
     * 
     * @param tableName
     * @param columnName
     */
    public EntryLocation(String tableName, String columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
    }

    /**
     * DOC amaumont Couple constructor comment.
     * 
     * @param tableName
     * @param columnName
     */
    public EntryLocation(EntryLocation tableEntryLocation) {
        this.tableName = tableEntryLocation.tableName;
        this.columnName = tableEntryLocation.columnName;
    }

    public String toString() {
        return Messages.getString("EntryLocation.returnTableName", this.tableName, this.columnName); //$NON-NLS-1$
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.columnName == null) ? 0 : this.columnName.hashCode());
        result = prime * result + ((this.tableName == null) ? 0 : this.tableName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EntryLocation other = (EntryLocation) obj;
        if (this.columnName == null) {
            if (other.columnName != null) {
                return false;
            }
        } else if (!this.columnName.equals(other.columnName)) {
            return false;
        }
        if (this.tableName == null) {
            if (other.tableName != null) {
                return false;
            }
        } else if (!this.tableName.equals(other.tableName)) {
            return false;
        }
        return true;
    }

}
