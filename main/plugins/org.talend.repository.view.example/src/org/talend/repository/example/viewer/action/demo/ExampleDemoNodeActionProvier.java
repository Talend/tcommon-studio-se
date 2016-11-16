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
package org.talend.repository.example.viewer.action.demo;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.talend.repository.view.di.metadata.action.MetedataNodeActionProvier;

public class ExampleDemoNodeActionProvier extends MetedataNodeActionProvier {

    public ExampleDemoNodeActionProvier() {
        super();
    }

    @Override
    public void fillContextMenu(IMenuManager manager) {
        super.fillContextMenu(manager);
    }

    @Override
    public void fillActionBars(IActionBars actionBars) {
        super.fillActionBars(actionBars);
    }

}
