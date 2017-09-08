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

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public interface IDrawableLink {

    public abstract void draw(GC gc);

    /**
     * Getter for point1.
     * 
     * @return the point1
     */
    public Point getPoint1();

    /**
     * Sets the point1.
     * 
     * @param point1 the point1 to set
     */
    public void setPoint1(Point point1);

    /**
     * Getter for point2.
     * 
     * @return the point2
     */
    public Point getPoint2();

    /**
     * Sets the point2.
     * 
     * @param point2 the point2 to set
     */
    public void setPoint2(Point point2);

    /**
     * Getter for calculateBounds.
     * 
     * @return the calculateBounds
     */
    public Rectangle getBoundsOfCalculate();

    /**
     * Sets the calculateBounds.
     * 
     * @param calculateBounds the calculateBounds to set
     */
    public void setBoundsOfCalculate(Rectangle calculateBounds);

    /**
     * Getter for connectorWidth.
     * 
     * @return the connectorWidth
     */
    public Integer getConnectorWidth();

    /**
     * Sets the connectorWidth.
     * 
     * @param connectorWidth the connectorWidth to set
     */
    public void setConnectorWidth(Integer connectorWidth);

    /**
     * Getter for style.
     * 
     * @return the style
     */
    public IStyleLink getStyle();

    /**
     * Sets the style.
     * 
     * @param style the style to set
     */
    public void setStyle(IStyleLink style);

}
