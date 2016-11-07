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
package org.talend.core.model.repository.documentation;

import org.talend.core.IService;
import org.talend.core.model.properties.Item;

/**
 * tang class global comment. Detailed comment
 */
public interface IDocumentationService extends IService {

    public boolean saveDocumentNode(Item item) throws Exception;

    public boolean createNodeDocumentationItemFromItem(Item item) throws Exception;

}
