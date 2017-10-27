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
package org.talend.designer.rowgenerator.data;

/**
 * class global comment. Detailled comment <br/> $Id: ListParameter.java,v 1.5 2007/02/02 03:04:21 pub Exp $
 */
public class ListParameter extends Parameter {

    private String[] values;

    /**
     * Sets the values.
     * 
     * @param values the values to set
     */
    public void setValues(String[] values) {
        this.values = values;
    }

    /**
     * Getter for values.
     * 
     * @return the values
     */
    public String[] getValues() {
        return this.values;
    }

    /**
     * Sets the properties from the input String.
     */
    public void parseProperties(String str) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.rowgenerator.data.Parameter#setValue(java.lang.String)
     */
    @Override
    public void setValue(String value) {
        super.setValue(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.rowgenerator.data.Parameter#sameParameterAs(org.talend.designer.rowgenerator.data.Parameter)
     */
    @Override
    public boolean sameParameterAs(Parameter obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ListParameter p = (ListParameter) obj;
        if (this.name == null) {
            if (p.name != null) {
                return false;
            }
        } else if (!this.name.equals(p.name)) {
            return false;
        }

        if (this.comment == null) {
            if (p.comment != null) {
                return false;
            }
        } else if (!this.comment.equals(p.comment)) {
            return false;
        }

        if (this.type == null) {
            if (p.type != null) {
                return false;
            }
        } else if (!this.type.equals(p.type)) {
            return false;
        }

        if (this.value == null) {
            if (p.value != null) {
                return false;
            }
        } else if (!this.value.equals(p.value)) {
            return false;
        }

        if (this.values == null) {
            if (p.values != null) {
                return false;
            }
        } else if (!this.values.equals(p.values)) {
            return false;
        }

        return true;
    }

}
