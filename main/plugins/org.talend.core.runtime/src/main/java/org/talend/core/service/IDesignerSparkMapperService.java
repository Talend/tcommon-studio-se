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
 * 
 * created by hcyi on Aug 11, 2015 Detailled comment
 *
 */
public interface IDesignerSparkMapperService extends IService {

    public boolean isVirtualComponent(final INode node);

}
