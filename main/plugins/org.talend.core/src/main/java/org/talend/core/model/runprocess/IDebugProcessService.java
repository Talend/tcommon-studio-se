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
package org.talend.core.model.runprocess;

import org.talend.core.IService;

/**
 * DOC talend class global comment. Detailled comment
 */
public interface IDebugProcessService extends IService {

    public void debugProcess();

    public void debugKill();
}
