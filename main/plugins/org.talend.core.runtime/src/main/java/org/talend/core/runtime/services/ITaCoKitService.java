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

import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.core.IService;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public interface ITaCoKitService extends IService {

    void checkMigration(final IProgressMonitor monitor) throws Exception;

}
