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
package org.talend.core.model.repository;

import org.talend.core.model.metadata.builder.connection.AbstractMetadataObject;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public interface ISubRepositoryObject extends IRepositoryViewObject {

    public AbstractMetadataObject getAbstractMetadataObject();

    public void removeFromParent();

    public Property getProperty();

    public ModelElement getModelElement();

}
