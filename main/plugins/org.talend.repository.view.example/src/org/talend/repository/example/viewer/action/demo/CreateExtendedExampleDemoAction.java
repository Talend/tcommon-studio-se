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
package org.talend.repository.example.viewer.action.demo;

import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.example.model.demo.DemoFactory;
import org.talend.repository.example.model.demo.ExampleDemoConnectionItem;
import org.talend.repository.example.model.demo.ExtendedExampleDemoConnectionItem;
import org.talend.repository.example.viewer.node.ExampleDemoRepositoryNodeType;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 * NOTE: this class is not finished, because need add some wizards or wizard pages also.
 */
@SuppressWarnings("nls")
public class CreateExtendedExampleDemoAction extends CreateExampleDemoAction {

    public CreateExtendedExampleDemoAction() {
        super();
        EDIT_LABEL = "Edit Extended Example Demo";
        CREATE_LABEL = "Create Extended Example Demo";
        OPEN_LABEL = "Open Extended Example Demo";
        this.setText(CREATE_LABEL);
        this.setToolTipText(CREATE_LABEL);
    }

    @Override
    protected ERepositoryObjectType getRepoObjectType() {
        return ExampleDemoRepositoryNodeType.repositoryExtendedExampleDemoType;
    }

    @Override
    protected ExampleDemoConnectionItem createDemoItem() {
        return DemoFactory.eINSTANCE.createExtendedExampleDemoConnectionItem();
    }

    @Override
    protected String getBaseLabel() {
        return "Extended" + super.getBaseLabel();
    }

    @Override
    public Class getClassForDoubleClick() {
        return ExtendedExampleDemoConnectionItem.class;
    }
}
