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
package org.talend.repository.example.viewer.node;

import org.talend.commons.ui.runtime.image.IImage;
import org.talend.commons.ui.runtime.repository.IExtendRepositoryNode;
import org.talend.repository.example.image.EExampleDemoImage;
import org.talend.repository.model.RepositoryNode;

public class ExampleDemoRepositoryNode implements IExtendRepositoryNode {

    public ExampleDemoRepositoryNode() {
    }

    @Override
    public IImage getNodeImage() {
        return EExampleDemoImage.DEMO_ICON;
    }

    @Override
    public int getOrdinal() {
        return 0;
    }

    @Override
    public Object[] getChildren() {
        return new RepositoryNode[0];
    }

}
