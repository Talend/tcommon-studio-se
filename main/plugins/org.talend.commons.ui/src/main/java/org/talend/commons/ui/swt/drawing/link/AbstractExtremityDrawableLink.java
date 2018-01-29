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
package org.talend.commons.ui.swt.drawing.link;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class AbstractExtremityDrawableLink implements IExtremityDrawableLink {

    protected IStyleLink styleLink;

    protected int xOffset;

    protected int yOffset;

    /**
     * DOC amaumont AbstractTipLink constructor comment.
     * 
     * @param styleLink
     */
    public AbstractExtremityDrawableLink(IStyleLink styleLink) {
        this.styleLink = styleLink;
    }

    /**
     * DOC amaumont AbstractTipLink constructor comment.
     * 
     * @param styleLink
     * @param xOffset
     * @param yOffset
     */
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.drawing.link.ITipLink#draw(org.eclipse.swt.graphics.GC)
     */
    public abstract void draw(GC gc, Point point);

    /**
     * Getter for styleLink.
     * 
     * @return the styleLink
     */
    public IStyleLink getStyleLink() {
        return this.styleLink;
    }

    /**
     * Sets the styleLink.
     * 
     * @param styleLink the styleLink to set
     */
    public void setStyleLink(IStyleLink styleLink) {
        this.styleLink = styleLink;
    }

    /**
     * Getter for xOffset.
     * 
     * @return the xOffset
     */
    public int getXOffset() {
        return this.xOffset;
    }

    /**
     * Sets the xOffset.
     * 
     * @param offset the xOffset to set
     */
    public void setXOffset(int offset) {
        this.xOffset = offset;
    }

    /**
     * Getter for yOffset.
     * 
     * @return the yOffset
     */
    public int getYOffset() {
        return this.yOffset;
    }

    /**
     * Sets the yOffset.
     * 
     * @param offset the yOffset to set
     */
    public void setYOffset(int offset) {
        this.yOffset = offset;
    }

}
