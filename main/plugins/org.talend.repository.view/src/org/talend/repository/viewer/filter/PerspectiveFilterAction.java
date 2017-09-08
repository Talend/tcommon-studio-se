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
package org.talend.repository.viewer.filter;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

public class PerspectiveFilterAction extends Action {

    private final PerspectiveFilterActionProvider actionProvider;

    public PerspectiveFilterAction(PerspectiveFilterActionProvider perspectiveFilterActionProvider, String label) {
        super(label, IAction.AS_CHECK_BOX);
        this.actionProvider = perspectiveFilterActionProvider;
    }

    @Override
    public void run() {
        super.run();
        actionProvider.setFiltering(isChecked(), false);
    }

}
