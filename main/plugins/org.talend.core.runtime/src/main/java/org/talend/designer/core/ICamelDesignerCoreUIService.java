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
package org.talend.designer.core;

import org.talend.commons.exception.PersistenceException;
import org.talend.core.IService;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.IRepositoryEditorInput;

/**
 * ESB service for UI part; ICamelDesignerCoreService should also be usable for commandline, should not add UI related
 * codes.
 */
public interface ICamelDesignerCoreUIService extends IService {

    public boolean isInstanceofCamelRoutes(Item item);

    public IRepositoryEditorInput getRouteEditorInput(ProcessItem processItem, boolean load, Boolean lastVersion)
            throws PersistenceException;

    public String getRouteEditorId(ProcessItem processItem);
}
