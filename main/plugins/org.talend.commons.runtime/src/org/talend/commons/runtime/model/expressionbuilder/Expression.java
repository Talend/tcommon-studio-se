// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.commons.runtime.model.expressionbuilder;

import java.util.ArrayList;
import java.util.List;

public class Expression {

    private String expression;

    private final List<Variable> variables;

    /**
     * yzhang Expression constructor comment.
     */
    public Expression() {
        expression = null;
        variables = new ArrayList<Variable>();
    }

    /**
     * yzhang Expression constructor comment.
     * 
     * @param expression
     * @param vList
     */
    public Expression(String expression, List<Variable> vList) {
        this.expression = expression;
        this.variables = vList;
    }

    /**
     * Getter for expression.
     * 
     * @return the expression
     */
    public String getExpression() {
        return this.expression;
    }

    /**
     * Sets the expression.
     * 
     * @param expression the expression to set
     */
    public void setExpression(String expression) {
        this.expression = expression;
    }

    /**
     * Getter for variables.
     * 
     * @return the variables
     */
    public List<Variable> getVariables() {
        return this.variables;
    }

}
