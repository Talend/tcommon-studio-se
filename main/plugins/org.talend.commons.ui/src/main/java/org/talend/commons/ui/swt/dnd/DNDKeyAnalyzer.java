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
package org.talend.commons.ui.swt.dnd;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: DNDKeyAnalyzer.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class DNDKeyAnalyzer {

    private boolean ctrlPressed;

    private boolean shiftPressed;

    public DNDKeyAnalyzer(DropTargetEvent event) {
        super();
        analyze(event);
    }

    /**
     * DOC amaumont Comment method "analyze".
     * 
     * @param event
     * @param mapperManager
     */
    private void analyze(DropTargetEvent event) {
        if ((event.detail & DND.DROP_COPY) != 0) { // Ctrl key
            // System.out.println("Ctrl key");
            ctrlPressed = true;
            shiftPressed = false;
        } else if ((event.detail & DND.DROP_LINK) != 0) { // Ctrl + Shift keys
            // System.out.println("Ctrl + Shift keys");
            ctrlPressed = true;
            shiftPressed = true;
        } else if ((event.detail & DND.DROP_MOVE) != 0) { // Shift key
            // System.out.println("Shift key");
            ctrlPressed = false;
            shiftPressed = true;
        } else if ((event.detail & DND.DROP_DEFAULT) != 0) { // no key
            // System.out.println("Default");
            ctrlPressed = false;
            shiftPressed = false;
        }

    }

    public boolean isCtrlPressed() {
        return this.ctrlPressed;
    }

    public boolean isShiftPressed() {
        return this.shiftPressed;
    }

}
