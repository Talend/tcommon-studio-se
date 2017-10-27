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
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: BooleanParameter.java 下午02:45:32 2007-8-7 +0000 (2007-8-7) yzhang $
 * 
 */
public class BooleanParameter extends Parameter {

    private boolean booleanValue;

    /**
     * yzhang Comment method "setValue".
     * 
     * @param value
     */
    public void setValue(boolean value) {
        this.booleanValue = value;
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
        final BooleanParameter p = (BooleanParameter) obj;
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
        if (this.booleanValue != p.booleanValue) {
            return false;
        }
        return true;
    }

}
