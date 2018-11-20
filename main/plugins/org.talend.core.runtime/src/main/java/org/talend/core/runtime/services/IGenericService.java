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
package org.talend.core.runtime.services;

import java.util.List;
import java.util.Map;

import org.talend.core.IService;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;

/**
 * created by ycbai on 2016年3月24日 Detailled comment
 *
 */
public interface IGenericService extends IService {

    /**
     * Call method <code>callBeforeActivate()</code> of <code>parameter</code>.
     * 
     * 
     * @param parameter
     * @return
     */
    public boolean callBeforeActivate(IElementParameter parameter);
    
    /**
     * Get all installed generic components information.
     * 
     */
    public List<Map<String, String>> getAllGenericComponentsInfo();
    
    public void resetReferenceValue(INode curNode, String oldConnectionName, String newConnectionName);

}
