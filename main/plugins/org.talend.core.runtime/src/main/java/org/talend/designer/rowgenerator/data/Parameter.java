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
package org.talend.designer.rowgenerator.data;

import org.talend.core.runtime.i18n.Messages;

/**
 * class global comment. Detailled comment <br/>
 * $Id: Parameter.java,v 1.4 2007/02/02 03:04:21 pub Exp $
 */
public abstract class Parameter {

    protected String type;

    /**
     * @uml.property name="name"
     */
    protected String name = ""; //$NON-NLS-1$

    /**
     * Getter of the property <tt>name</tt>.
     * 
     * @return Returns the name.
     * @uml.property name="name"
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter of the property <tt>name</tt>.
     * 
     * @param name The name to set.
     * @uml.property name="name"
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @uml.property name="value"
     */
    protected String value = ""; //$NON-NLS-1$

    /**
     * Getter of the property <tt>value</tt>.
     * 
     * @return Returns the value.
     * @uml.property name="value"
     */
    public String getValue() {
        int index = this.value.indexOf("{variables}"); //$NON-NLS-1$
        if (index != -1) {
            return this.value.substring(0, index);
        } else {
            return this.value;
        }
    }

    /**
     * Setter of the property <tt>value</tt>.
     * 
     * @param value The value to set.
     * @uml.property name="value"
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @uml.property name="comment"
     */
    protected String comment = ""; //$NON-NLS-1$

    /**
     * Getter of the property <tt>comment</tt>.
     * 
     * @return Returns the comment.
     * @uml.property name="comment"
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * Setter of the property <tt>comment</tt>.
     * 
     * @param comment The comment to set.
     * @uml.property name="comment"
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Sets the properties from the input String.
     */
    public void parseProperties(String str) {

    }

    /**
     * qzhang Comment method "sameParameterAs".
     * 
     * @param obj
     * @return
     */
    public abstract boolean sameParameterAs(Parameter obj);

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Messages.getString("Parameter.Name") + name).//$NON-NLS-1$
                append("      ").append( //$NON-NLS-1$
                        Messages.getString("Parameter.Value") + this.getValue()).//$NON-NLS-1$
                append("      ").append( //$NON-NLS-1$
                        Messages.getString("Parameter.Comment") + this.getComment())//$NON-NLS-1$
                .append("      ").append( //$NON-NLS-1$
                        Messages.getString("Parameter.Type") + this.getType()); //$NON-NLS-1$

        return sb.toString();
    }

    /**
     * Getter for type.
     * 
     * @return the type
     * @see #setType(String);
     */
    public String getType() {
        return this.type;
    }

    /**
     * Sets the type.
     * 
     * @param the type value: {@link ParameterFactory#PARAMETER_TYPE_STRING} or
     * {@link ParameterFactory#PARAMETER_TYPE_INT} or {@link ParameterFactory#PARAMETER_TYPE_LIST} or
     * {@link ParameterFactory#PARAMETER_TYPE_DOUBLE} or {@link ParameterFactory#PARAMETER_TYPE_OBJECT} or
     * {@link ParameterFactory#PARAMETER_TYPE_BOOLEAN} or {@link ParameterFactory#PARAMETER_TYPE_LONG} or
     * {@link ParameterFactory#PARAMETER_TYPE_DATE} or {@link ParameterFactory#PARAMETER_TYPE_CHAR}
     */
    public void setType(String type) {
        this.type = type;
    }

}
