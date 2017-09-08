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
package org.talend.core.repository.model.provider;

import java.util.List;
import java.util.Set;

import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ItemReferenceBean;
import org.talend.core.repository.ui.actions.DeleteActionCache;
import org.talend.repository.model.IRepositoryNode;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public interface ICheckDeleteItemReference {

    public Set<ItemReferenceBean> getItemReferenceBeans(List<? extends IRepositoryNode> deleteNodes,
            DeleteActionCache deleteActionCache);
    
    public Set<ItemReferenceBean> getItemReferenceBeans(IRepositoryViewObject convertNode,
            DeleteActionCache deleteActionCache);

}
