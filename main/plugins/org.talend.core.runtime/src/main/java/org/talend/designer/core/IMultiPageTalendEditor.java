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
package org.talend.designer.core;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IEditorPart;
import org.talend.core.model.process.IProcess2;

/**
 * ggu class global comment. Detailled comment
 */
public interface IMultiPageTalendEditor extends IEditorPart, IAdaptable {

    public IProcess2 getProcess();

    public ITalendEditor getTalendEditor();

    public void changePaletteComponentHandler();
}
