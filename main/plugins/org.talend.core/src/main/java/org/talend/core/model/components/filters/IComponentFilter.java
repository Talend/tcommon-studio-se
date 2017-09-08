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
package org.talend.core.model.components.filters;

import org.talend.designer.core.model.utils.emf.talendfile.NodeType;

/**
 * Use to filter components. Used in :
 * <ul>
 * <li>RenameComponentAction: to know on wich components apply conversion</li>
 * </ul>
 * 
 * $Id$
 * 
 */
public interface IComponentFilter {

    boolean accept(NodeType node);
}
