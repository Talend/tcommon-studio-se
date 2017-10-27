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
 * $Id: IExpressionConsumer.java 下午04:21:19 2007-8-1 +0000 (2007-8-1) yzhang $
 * 
 */
public interface IExpressionDataBean {

    public String getExpression();

    public List<Variable> getVariables();

    public String getOwnerId();

    public void setConsumerExpression(String expression);

    // java Only
    public String getExpressionType();

    public void setExpressionType(String expressionType);

}
