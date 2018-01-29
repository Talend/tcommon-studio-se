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
package org.talend.commons.ui.swt.cursor;

import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Shell;

/**
 * DOC amaumont class global comment. Detailled comment
 */
public class CursorHelper {

    /**
     * Looad the given cursor for the current active shell.
     * 
     * @param display
     * @return
     */
    public static void changeCursor(Shell shell, int swtCursor) {
        Cursor cursor = shell.getDisplay().getSystemCursor(swtCursor);
        shell.setCursor(cursor);
    }

}
