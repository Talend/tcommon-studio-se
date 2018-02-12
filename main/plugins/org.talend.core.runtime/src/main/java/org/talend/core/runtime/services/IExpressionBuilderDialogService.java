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
package org.talend.core.runtime.services;

import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.talend.commons.runtime.model.expressionbuilder.Variable;
import org.talend.commons.ui.runtime.expressionbuilder.IExpressionBuilderDialogController;
import org.talend.commons.ui.runtime.expressionbuilder.IExpressionDataBean;
import org.talend.core.IService;
import org.talend.core.model.process.INode;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: IExpressionBuilderDialogService.java 下午04:26:06 2007-8-1 +0000 (2007-8-1) yzhang $
 * 
 */
public interface IExpressionBuilderDialogService extends IService {

    public IExpressionBuilderDialogController getExpressionBuilderInstance(Composite parent, IExpressionDataBean dataBean,
            INode component);

    public IExpressionBuilderDialogController getExpressionBuilderInstance(Composite parent, IExpressionDataBean dataBean,
            INode component, List<Variable> vars, boolean hasPigDataFuCategory);

    public IExpressionBuilderDialogController getExpressionBuilderInstance(Composite parent, IExpressionDataBean dataBean,
            INode component, boolean isBatch);
}
