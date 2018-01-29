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
package org.talend.core.model.routines;

import java.util.Set;
import java.util.Vector;

import org.talend.core.IService;
import org.talend.core.model.general.ModuleNeeded;

/**
 * 
 * qli class global comment. Detailled comment
 */
public interface IRoutinesService extends IService {

    public Vector getAccents();

    Set<ModuleNeeded> getRunningModules();
}
