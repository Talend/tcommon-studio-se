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
package org.talend.core.repository.model;

import org.eclipse.core.resources.IProject;
import org.talend.commons.exception.PersistenceException;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public interface ILocalRepositoryFactory extends IRepositoryFactory {

    public void synchronizeRoutines(IProject prj) throws PersistenceException;
}
