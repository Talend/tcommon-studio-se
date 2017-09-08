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
package org.talend.commons.ui.utils;

import org.eclipse.swt.SWT;
import org.talend.commons.ui.runtime.i18n.Messages;

/**
 * Utility class for event handling.
 */
public final class EventUtil {

    /**
     * Default Constructor. Must not be used.
     */
    private EventUtil() {
    }

    public static String getEventNameFromType(int eventType) {

        switch (eventType) {
        case SWT.None:
            return Messages.getString("EventUtil.typeNull"); //$NON-NLS-1$
        case SWT.Activate:
            return Messages.getString("EventUtil.activate"); //$NON-NLS-1$
        case SWT.Arm:
            return Messages.getString("EventUtil.arm"); //$NON-NLS-1$
        case SWT.Close:
            return Messages.getString("EventUtil.close"); //$NON-NLS-1$
        case SWT.Collapse:
            return Messages.getString("EventUtil.collapse"); //$NON-NLS-1$
        case SWT.Deactivate:
            return Messages.getString("EventUtil.deactivate"); //$NON-NLS-1$
        case SWT.Deiconify:
            return Messages.getString("EventUtil.deiconify"); //$NON-NLS-1$
        case SWT.DefaultSelection:
            return Messages.getString("EventUtil.defaultSelection"); //$NON-NLS-1$
        case SWT.Dispose:
            return Messages.getString("EventUtil.dispose"); //$NON-NLS-1$
        case SWT.DragDetect:
            return Messages.getString("EventUtil.dragDetect"); //$NON-NLS-1$
        case SWT.Expand:
            return Messages.getString("EventUtil.expand"); //$NON-NLS-1$
        case SWT.FocusIn:
            return Messages.getString("EventUtil.focusIn"); //$NON-NLS-1$
        case SWT.FocusOut:
            return Messages.getString("EventUtil.focusOut"); //$NON-NLS-1$
        case SWT.HardKeyDown:
            return Messages.getString("EventUtil.hardKeyDown"); //$NON-NLS-1$
        case SWT.HardKeyUp:
            return Messages.getString("EventUtil.hardKeyUp"); //$NON-NLS-1$
        case SWT.Help:
            return Messages.getString("EventUtil.help"); //$NON-NLS-1$
        case SWT.Hide:
            return Messages.getString("EventUtil.hide"); //$NON-NLS-1$
        case SWT.Iconify:
            return Messages.getString("EventUtil.iconify"); //$NON-NLS-1$
        case SWT.KeyDown:
            return Messages.getString("EventUtil.keyDown"); //$NON-NLS-1$
        case SWT.KeyUp:
            return Messages.getString("EventUtil.keyUp"); //$NON-NLS-1$
        case SWT.Modify:
            return Messages.getString("EventUtil.modify"); //$NON-NLS-1$
        case SWT.MenuDetect:
            return Messages.getString("EventUtil.menuDetect"); //$NON-NLS-1$
        case SWT.MouseDoubleClick:
            return Messages.getString("EventUtil.mouseDoubleClick"); //$NON-NLS-1$
        case SWT.MouseDown:
            return Messages.getString("EventUtil.mouseDown"); //$NON-NLS-1$
        case SWT.MouseEnter:
            return Messages.getString("EventUtil.mouseEnter"); //$NON-NLS-1$
        case SWT.MouseExit:
            return Messages.getString("EventUtil.mouseExit"); //$NON-NLS-1$
        case SWT.MouseHover:
            return Messages.getString("EventUtil.mouseHover"); //$NON-NLS-1$
        case SWT.MouseMove:
            return Messages.getString("EventUtil.mouseMove"); //$NON-NLS-1$
        case SWT.MouseUp:
            return Messages.getString("EventUtil.mouseUp"); //$NON-NLS-1$
        case SWT.Move:
            return Messages.getString("EventUtil.move"); //$NON-NLS-1$
        case SWT.Paint:
            return Messages.getString("EventUtil.paint"); //$NON-NLS-1$
        case SWT.Resize:
            return Messages.getString("EventUtil.resize"); //$NON-NLS-1$
        case SWT.Selection:
            return Messages.getString("EventUtil.selection"); //$NON-NLS-1$
        case SWT.Show:
            return Messages.getString("EventUtil.show"); //$NON-NLS-1$
        case SWT.Traverse:
            return Messages.getString("EventUtil.traverse"); //$NON-NLS-1$
        case SWT.Verify:
            return Messages.getString("EventUtil.verify"); //$NON-NLS-1$
        default:
            return Messages.getString("EventUtil.UnknowType") + eventType; //$NON-NLS-1$
        }
    }

    public static String getEventNameFromDetail(int eventDetail) {
        switch (eventDetail) {
        case SWT.None:
            return Messages.getString("EventUtil.detailNull"); //$NON-NLS-1$
        case SWT.SELECTED:
            return Messages.getString("EventUtil.selectedEqual") + SWT.SELECTED; //$NON-NLS-1$
        default:
            return Messages.getString("EventUtil.UnknowDetail") + eventDetail; //$NON-NLS-1$
        }
    }

    public static void main(String[] args) {
    }
}
