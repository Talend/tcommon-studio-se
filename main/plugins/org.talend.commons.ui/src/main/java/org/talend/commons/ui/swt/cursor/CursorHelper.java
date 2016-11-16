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
package org.talend.commons.ui.swt.cursor;

import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Shell;

public class CursorHelper {

    /**
     * Load the given cursor for the current active shell.
     */
    public static void changeCursor(Shell shell, int swtCursor) {
        Cursor cursor = shell.getDisplay().getSystemCursor(swtCursor);
        shell.setCursor(cursor);
    }

}
