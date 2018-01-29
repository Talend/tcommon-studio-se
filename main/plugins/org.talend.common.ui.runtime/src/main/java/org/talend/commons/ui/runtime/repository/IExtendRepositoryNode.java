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
package org.talend.commons.ui.runtime.repository;

import org.talend.commons.ui.runtime.image.IImage;

/**
 * DOC hywang class global comment. Detailled comment
 */
public interface IExtendRepositoryNode {

    public IImage getNodeImage();

    public int getOrdinal();

    // *the element should implement the IRepositoryNode
    public Object[] getChildren();

}
