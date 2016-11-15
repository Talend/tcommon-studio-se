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
package org.talend.metadata.managment.ui.model.positional;

import org.eclipse.swt.graphics.Point;

public class ResizeHelper {

    public enum RESIZE_MODE {
        HORIZONTAL,
        VERTICAL,
        BOTH,
        NONE,
    }

    private Point lastDragPoint;

    private RESIZE_MODE currentMode;

    public void startDrag(Point point) {
        lastDragPoint = point;
    }

    public void stopDrag() {
        lastDragPoint = null;
    }

    public boolean isDragging() {
        return lastDragPoint != null;
    }

    public Point getLastDragPoint() {
        return lastDragPoint;
    }

    public void setLastDragPoint(Point lastDragPoint) {
        this.lastDragPoint = lastDragPoint;
    }

    public RESIZE_MODE getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(RESIZE_MODE currentMode) {
        this.currentMode = currentMode;
    }

}
