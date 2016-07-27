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
package org.talend.core.ui;

import org.talend.core.IService;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.process.INode;

/**
 * DOC hwang  class global comment. Detailled comment
 */
public interface ISparkJobletProviderService extends IService{

    public boolean isSparkJobletComponent(INode node);
    
    public IComponent instanceSparkJobletComponent(Object execObj);
    
    public void clearSparkJobletComponent();
    
}
