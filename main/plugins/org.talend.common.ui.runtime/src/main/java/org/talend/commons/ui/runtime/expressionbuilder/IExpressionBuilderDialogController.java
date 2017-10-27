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
package org.talend.commons.ui.runtime.expressionbuilder;

import java.util.List;

import org.talend.commons.runtime.model.expressionbuilder.Variable;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: IExpressionBuilderDialogController.java 下午04:43:22 2007-8-1 +0000 (2007-8-1) yzhang $
 * 
 */
public interface IExpressionBuilderDialogController extends ICellEditorDialog {

    public void setDefaultExpression(String expression);

    public void addVariables(List<Variable> variables);

    public String getExpressionForTable();

}
