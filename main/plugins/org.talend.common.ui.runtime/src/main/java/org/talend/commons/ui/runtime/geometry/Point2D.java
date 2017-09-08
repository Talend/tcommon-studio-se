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

// Java & B�zier
// (C) 2002 Emmanuel Turquin
//
// Point2D.java
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

package org.talend.commons.ui.runtime.geometry;

import org.eclipse.swt.graphics.Rectangle;

/**
 * 
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: Point2D.java 7048 2007-11-16 02:36:17Z nrousseau $
 * 
 */
public class Point2D {

    public static final int WIDTH = 8;

    public static final int HEIGHT = 8;

    PointDouble coord;

    Rectangle rect;

    private boolean visible;

    // constructor
    public Point2D() {
        coord = new PointDouble();
        rect = new Rectangle(0, 0, WIDTH, HEIGHT);
    }

    public Point2D(Point2D p) {
        coord = new PointDouble(p.getX(), p.getY());
        rect = new Rectangle((int) p.getX() - 4, (int) p.getY() - 4, WIDTH, HEIGHT);
    }

    public Point2D(double x, double y) {
        coord = new PointDouble(x, y);
        rect = new Rectangle((int) x - 4, (int) y - 4, WIDTH, HEIGHT);
    }

    // getX
    public double getX() {
        return coord.getX();
    }

    // getY
    public double getY() {
        return coord.getY();
    }

    // contains
    public boolean contains(double x, double y) {
        if (x > getX() - WIDTH / 2 && x < getX() + WIDTH / 2)
            if (y > getY() - HEIGHT / 2 && y < getY() + HEIGHT / 2)
                return true;
        return false;
    }

    // setLocation
    public void setLocation(double x, double y) {
        coord.setLocation(x, y);
        rect.x = (int) x - 4;
        rect.y = (int) y - 4;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}
