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
 * DOC amaumont TypesManager class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class MappingType {

    private String dbType;

    private String talendType;

    private Boolean defaultSelected;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.dbType == null) ? 0 : this.dbType.hashCode());
        result = prime * result + ((this.defaultSelected == null) ? 0 : this.defaultSelected.hashCode());
        result = prime * result + ((this.talendType == null) ? 0 : this.talendType.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final MappingType other = (MappingType) obj;

        if (this.dbType != null && !this.dbType.equals(other.dbType))
            return false;

        if (this.defaultSelected != null && !this.defaultSelected.equals(other.defaultSelected))
            return false;

        if (this.talendType != null && !this.talendType.equals(other.talendType))
            return false;
        return true;
    }

    /**
     * Getter for dbmsType.
     * 
     * @return the dbmsType
     */
    public String getDbType() {
        return this.dbType;
    }

    /**
     * Sets the dbmsType.
     * 
     * @param dbmsType the dbmsType to set
     */
    public void setDbType(String dbmsType) {
        this.dbType = dbmsType;
    }

    /**
     * Getter for defaultSelected.
     * 
     * @return the defaultSelected
     */
    public Boolean getDefaultSelected() {
        return this.defaultSelected;
    }

    /**
     * Sets the defaultSelected.
     * 
     * @param defaultSelected the defaultSelected to set
     */
    public void setDefaultSelected(Boolean defaultSelected) {
        this.defaultSelected = defaultSelected;
    }

    /**
     * Getter for languageType.
     * 
     * @return the languageType
     */
    public String getTalendType() {
        return this.talendType;
    }

    /**
     * Sets the languageType.
     * 
     * @param talendTypeName the languageType to set
     */
    public void setTalendType(String talendTypeName) {
        this.talendType = talendTypeName;
    }

    /**
         * toString method: creates a String representation of the object
         * @return the String representation
         * @author 
         */
        public String toString() {
            StringBuffer buffer = new StringBuffer();
            buffer.append("MappingType["); //$NON-NLS-1$
            buffer.append("dbmsType = ").append(dbType); //$NON-NLS-1$
            buffer.append(", talendType = ").append(talendType); //$NON-NLS-1$
            buffer.append(", defaultSelected = ").append(defaultSelected); //$NON-NLS-1$
            buffer.append("]"); //$NON-NLS-1$
            return buffer.toString();
        }

    
    
}

