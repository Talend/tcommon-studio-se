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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class StyleLink implements IStyleLink {

    protected Color foregroundColor;

    protected Color backgroundColor;

    protected IExtremityDrawableLink extremity1;

    protected IExtremityDrawableLink extremity2;

    protected IDrawableLink drawableLink;

    protected int lineWidth = 1;

    protected int lineStyle = SWT.LINE_SOLID;

    protected int lineJoin = SWT.JOIN_MITER;

    protected int lineCap = SWT.CAP_FLAT;

    protected int[] lineDash;

    /**
     * 
     * Indicate direction of link. <br/>
     * 
     * $Id$
     * 
     */
    public enum EDirection {
        ONE_TO_TWO,
        TWO_TO_ONE,
        NONE,
        BOTH,
    }

    /**
     * default value : EDirection.ONE_TO_TWO.
     */
    EDirection direction = EDirection.ONE_TO_TWO;

    /**
     * Getter for direction.
     * 
     * @return the direction
     */
    public EDirection getDirection() {
        return this.direction;
    }

    /**
     * Sets the direction.
     * 
     * @param direction the direction to set
     */
    public void setDirection(EDirection direction) {
        this.direction = direction;
    }

    /**
     * Getter for backgroundColor.
     * 
     * @return the backgroundColor
     */
    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    /**
     * Sets the backgroundColor.
     * 
     * @param backgroundColor the backgroundColor to set
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Getter for foregroundColor.
     * 
     * @return the foregroundColor
     */
    public Color getForegroundColor() {
        return this.foregroundColor;
    }

    /**
     * Sets the foregroundColor.
     * 
     * @param foregroundColor the foregroundColor to set
     */
    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    /**
     * Getter for extremity1.
     * 
     * @return the extremity1
     */
    public IExtremityDrawableLink getExtremity1() {
        return this.extremity1;
    }

    /**
     * Sets the extremity1.
     * 
     * @param extremity1 the extremity1 to set
     */
    public void setExtremity1(IExtremityDrawableLink extremity1) {
        this.extremity1 = extremity1;
    }

    /**
     * Getter for extremity2.
     * 
     * @return the extremity2
     */
    public IExtremityDrawableLink getExtremity2() {
        return this.extremity2;
    }

    /**
     * Sets the extremity2.
     * 
     * @param extremity2 the extremity2 to set
     */
    public void setExtremity2(IExtremityDrawableLink extremity2) {
        this.extremity2 = extremity2;
    }

    /**
     * Getter for drawableLink.
     * 
     * @return the drawableLink
     */
    public IDrawableLink getDrawableLink() {
        return this.drawableLink;
    }

    /**
     * Sets the drawableLink.
     * 
     * @param drawableLink the drawableLink to set
     */
    public void setDrawableLink(IDrawableLink drawableLink) {
        this.drawableLink = drawableLink;
    }

    /**
     * Getter for lineCap.
     * 
     * @return the lineCap
     * @see GC#getLineCap()
     */
    public int getLineCap() {
        return this.lineCap;
    }

    /**
     * Sets the lineCap.
     * 
     * @param lineCap the lineCap to set
     * @see GC#setLineCap(int)
     */
    public void setLineCap(int lineCap) {
        this.lineCap = lineCap;
    }

    /**
     * Getter for lineDash.
     * 
     * @return the lineDash
     * @see GC#getLineDash()
     */
    public int[] getLineDash() {
        return this.lineDash;
    }

    /**
     * Sets the lineDash.
     * 
     * @param lineDash the lineDash to set
     * @see GC#setLineDash(int[])
     */
    public void setLineDash(int[] lineDash) {
        this.lineDash = lineDash;
    }

    /**
     * Getter for lineJoin. Note: this is only visible when using GC#drawPolygon(int[]) !
     * 
     * @return the lineJoin
     * @see GC#getLineJoin()
     */
    public int getLineJoin() {
        return this.lineJoin;
    }

    /**
     * Sets the lineJoin. Note: this is only visible when using GC#drawPolygon(int[]) !
     * 
     * @param lineJoin the lineJoin to set
     * @see GC#setLineJoin(int)
     */
    public void setLineJoin(int lineJoin) {
        this.lineJoin = lineJoin;
    }

    /**
     * Getter for lineStyle.
     * 
     * @return the lineStyle
     * @see GC#getLineStyle()
     */
    public int getLineStyle() {
        return this.lineStyle;
    }

    /**
     * Sets the lineStyle.
     * 
     * @param lineStyle the lineStyle to set
     * @see GC#setLineStyle(int)
     */
    public void setLineStyle(int lineStyle) {
        this.lineStyle = lineStyle;
    }

    /**
     * Getter for lineWidth.
     * 
     * @return the lineWidth
     * @see GC#getLineWidth()
     */
    public int getLineWidth() {
        return this.lineWidth;
    }

    /**
     * Sets the lineWidth.
     * 
     * @param lineWidth the lineWidth to set
     * @see GC#setLineWidth(int)
     */
    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.drawing.link.IStyleLink#apply(org.eclipse.swt.graphics.GC)
     */
    public void apply(GC gc) {
        if (getForegroundColor() != null) {
            gc.setForeground(getForegroundColor());
        }
        if (getBackgroundColor() != null) {
            gc.setBackground(getBackgroundColor());
        }
        gc.setLineCap(getLineCap());
        gc.setLineWidth(getLineWidth());
        gc.setLineStyle(getLineStyle());
        gc.setLineJoin(getLineJoin());
        gc.setLineDash(getLineDash());
    }

}
