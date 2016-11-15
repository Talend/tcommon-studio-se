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
package org.talend.commons.ui.swt.advanced.dataeditor.control;

import org.eclipse.swt.widgets.Button;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedControlViewer;

public interface IExtendedPushButton {

    public Button getButton();

    public AbstractExtendedControlViewer getExtendedControlViewer();

    public boolean getEnabledState();

}
