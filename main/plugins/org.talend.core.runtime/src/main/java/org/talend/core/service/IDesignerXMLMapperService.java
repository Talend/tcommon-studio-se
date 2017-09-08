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

import org.talend.core.IService;
import org.talend.core.model.process.INode;

/**
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 */
public interface IDesignerXMLMapperService extends IService {

    /**
     * 
     * DOC YeXiaowei Comment method "isVirtualComponent".
     * 
     * @param node
     * @return
     */
    public boolean isVirtualComponent(final INode node);

}
