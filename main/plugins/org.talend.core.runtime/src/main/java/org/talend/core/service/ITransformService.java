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
package org.talend.core.service;

import org.eclipse.core.resources.IResource;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.talend.core.IService;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;

/**
 * created by wchen on 2013-8-16 Detailled comment
 * 
 */
public interface ITransformService extends IService {

    public boolean isTransformItem(Item item);

    public boolean isTransformType(ERepositoryObjectType type);

    public boolean isTransformResource(IResource resource);

    public boolean isSampleFileResource(IResource resource);

    public boolean performDrop(DropTargetEvent event, IResource targetNode, int operation, Object data);

}
