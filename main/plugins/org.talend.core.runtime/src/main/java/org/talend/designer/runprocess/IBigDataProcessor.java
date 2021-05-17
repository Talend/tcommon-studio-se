// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.runprocess;

import java.util.Set;

import org.talend.core.model.general.ModuleNeeded;

/**
 * created by nrousseau on Mar 24, 2018
 * Detailled comment
 *
 */
public interface IBigDataProcessor extends IProcessor {

    public boolean needsShade();

    public Set<ModuleNeeded> getShadedModulesExclude();
}
