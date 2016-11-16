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
package org.talend.commons.ui.runtime.swt.proposal;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

public class TextCellEditorContentAdapterExtended extends TextContentAdapterExtended {

    @Override
    public Rectangle getInsertionBounds(Control control) {
        Rectangle bounds = super.getInsertionBounds(control);
        bounds.y += 10;
        return bounds;
    }

}
