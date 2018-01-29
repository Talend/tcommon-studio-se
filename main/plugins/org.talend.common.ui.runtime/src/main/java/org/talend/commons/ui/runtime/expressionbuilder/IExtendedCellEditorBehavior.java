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
package org.talend.commons.ui.runtime.expressionbuilder;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: IExtendedCellEditorBehavior.java 上午09:52:19 2007-8-16 +0000 (2007-8-16) yzhang $
 * 
 */
public interface IExtendedCellEditorBehavior {

    public Control createBehaviorControls(Composite parent);

}
