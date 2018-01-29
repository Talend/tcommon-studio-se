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
package org.talend.repository.example.viewer.handler.demo;

import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.example.model.demo.DemoFactory;
import org.talend.repository.example.model.demo.DemoPackage;
import org.talend.repository.example.viewer.node.ExampleDemoRepositoryNodeType;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class ExtendedExampleDemoRepositoryHandler extends ExampleDemoRepositoryHandler {

    /**
     * DOC ggu ExampleDemoRepositoryHandler constructor comment.
     */
    public ExtendedExampleDemoRepositoryHandler() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#createNewItem(org.talend.core.model.repository.
     * ERepositoryObjectType)
     */
    @Override
    public Item createNewItem(ERepositoryObjectType type) {
        if (isRepObjType(type)) {
            return DemoFactory.eINSTANCE.createExtendedExampleDemoConnectionItem();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.repository.IRepositoryContentHandler#getRepositoryObjectType(org.talend.core.model.properties
     * .Item)
     */
    @Override
    public ERepositoryObjectType getRepositoryObjectType(Item item) {
        if (item.eClass() == DemoPackage.Literals.EXTENDED_EXAMPLE_DEMO_CONNECTION_ITEM) {
            return getHandleType();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryContentHandler#getHandleType()
     */
    @Override
    public ERepositoryObjectType getHandleType() {
        return ExampleDemoRepositoryNodeType.repositoryExtendedExampleDemoType;
    }

}
