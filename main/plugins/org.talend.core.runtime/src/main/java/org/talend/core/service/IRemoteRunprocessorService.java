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
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.Property;
import org.talend.designer.runprocess.IProcessor;

/**
 * DOC guanglong.du class global comment. Detailled comment
 */
public interface IRemoteRunprocessorService extends IService {

    public IProcessor createRemotePerlProcessor(IProcess process, Property property, boolean filenameFromLabel);

}
