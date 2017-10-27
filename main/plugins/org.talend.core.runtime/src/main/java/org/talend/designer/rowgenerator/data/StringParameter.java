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

import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;

/**
 * class global comment. Detailled comment <br/>
 * $Id: StringParameter.java,v 1.3 2007/02/02 03:04:20 pub Exp $
 */
public class StringParameter extends Parameter {

    /**
     * Sets the properties from the input String.
     */
    @Override
    public void parseProperties(String str) {
        str = str.replaceFirst(ParameterFactory.PARAMETER_TYPE_STRING, ""); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.designer.rowgenerator.data.Parameter#sameParameterAs(org.talend.designer.rowgenerator.data.Parameter)
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
        final StringParameter p = (StringParameter) obj;
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
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.rowgenerator.data.Parameter#setType(java.lang.String)
     */
    @Override
    public void setType(String type) {
        if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.JAVA)) {
            super.setType("String"); //$NON-NLS-1$
        } else {
            super.setType(type);
        }
    }

}
