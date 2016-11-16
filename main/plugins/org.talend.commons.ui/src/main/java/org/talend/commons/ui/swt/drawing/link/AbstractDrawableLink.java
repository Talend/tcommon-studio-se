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

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public abstract class AbstractDrawableLink implements IDrawableLink {

    protected Point point1;

    protected Point point2;

    protected Rectangle boundsOfCalculate;

    protected Integer connectorWidth;

    protected IStyleLink style;

    public AbstractDrawableLink(IStyleLink style) {
        super();
        this.style = style;
    }

    public void draw(GC gc) {
        drawBody(gc);
        drawExtremities(gc);
    }

    protected void drawExtremities(GC gc) {
        if (style.getExtremity1() != null) {
            style.getExtremity1().draw(gc, point1);
        }
        if (style.getExtremity2() != null) {
            style.getExtremity2().draw(gc, point2);
        }
    }

    protected abstract void drawBody(GC gc);

    public Point getPoint1() {
        return this.point1;
    }

    public void setPoint1(Point point1) {
        this.point1 = point1;
    }

    public Point getPoint2() {
        return this.point2;
    }

    public void setPoint2(Point point2) {
        this.point2 = point2;
    }

    public Rectangle getBoundsOfCalculate() {
        return this.boundsOfCalculate;
    }

    public void setBoundsOfCalculate(Rectangle calculateBounds) {
        this.boundsOfCalculate = calculateBounds;
    }

    public Integer getConnectorWidth() {
        return this.connectorWidth;
    }

    public void setConnectorWidth(Integer connectorWidth) {
        this.connectorWidth = connectorWidth;
    }

    public IStyleLink getStyle() {
        return this.style;
    }

    public void setStyle(IStyleLink style) {
        this.style = style;
    }

}
