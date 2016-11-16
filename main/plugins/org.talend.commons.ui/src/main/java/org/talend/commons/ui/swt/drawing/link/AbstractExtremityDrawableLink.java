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
package org.talend.commons.ui.swt.drawing.link;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

public abstract class AbstractExtremityDrawableLink implements IExtremityDrawableLink {

    protected IStyleLink styleLink;

    protected int xOffset;

    protected int yOffset;

    public AbstractExtremityDrawableLink(IStyleLink styleLink) {
        this.styleLink = styleLink;
    }

    public AbstractExtremityDrawableLink(IStyleLink styleLink, int xOffset, int yOffset) {
        super();
        this.styleLink = styleLink;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    /**
     * Return the current foregroundColor if defined , else return styleLink foreground if defined , else return gc
     * foreground.
     * 
     * @return the foregroundColor
     */
    public Color getForegroundColor(GC gc) {
        if (styleLink.getForegroundColor() != null) {
            return styleLink.getForegroundColor();
        } else {
            return gc.getForeground();
        }
    }

    public abstract void draw(GC gc, Point point);

    public IStyleLink getStyleLink() {
        return this.styleLink;
    }

    public void setStyleLink(IStyleLink styleLink) {
        this.styleLink = styleLink;
    }

    public int getXOffset() {
        return this.xOffset;
    }

    public void setXOffset(int offset) {
        this.xOffset = offset;
    }

    public int getYOffset() {
        return this.yOffset;
    }

    public void setYOffset(int offset) {
        this.yOffset = offset;
    }

}
