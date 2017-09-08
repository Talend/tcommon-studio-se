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
package org.talend.core.ui;

import org.eclipse.jface.action.IAction;
import org.talend.core.IService;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.RepositoryNode;

/**
 * created by cmeng on Nov 23, 2015 Detailled comment
 *
 */
public interface IRouteletService extends IService {

    public String getRouteletEditorId();

    public IAction getEditProcessAction(RepositoryNode result, ERepositoryObjectType type);
}
