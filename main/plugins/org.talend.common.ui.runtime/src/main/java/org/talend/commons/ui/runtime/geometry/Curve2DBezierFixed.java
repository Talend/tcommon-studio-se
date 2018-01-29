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
// Curve2DBezierFixed.java
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

/**
 * 
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: Curve2DBezierFixed.java 7048 2007-11-16 02:36:17Z nrousseau $
 * 
 */
public class Curve2DBezierFixed extends Curve2D {

    double[] tmp;

    int size;

    int n = 1;

    // constructor
    public Curve2DBezierFixed() {
        super();
    }

    public Curve2DBezierFixed(int degree) {
        super();
        n = Math.max(degree, 1);
    }

    public Curve2DBezierFixed(Point2DList pl) {
        super(pl);
    }

    public Curve2DBezierFixed(Point2DList pl, int degree) {
        super(pl);
        n = Math.max(degree, 1);
    }

    // computeSegments
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    void computeSegments() {
        Point2DList pl = (Point2DList) plist;

        size = subdiv * n;
        tmp = new double[size + 1];
        for (int i = 0; i <= size; i++)
            tmp[i] = (double) i / size;

        PointDouble p;
        double x, y, bt;

        for (int j = 0; j < pl.size() - 1; j += n) {
            int d = Math.min(n, pl.size() - j - 1);

            for (int t = 0; t <= size; t++) {
                x = 0;
                y = 0;
                for (int i = 0; i <= d; i++) {
                    bt = bezierTerm(d, i, t);
                    x += ((Point2D) pl.get(i + j)).getX() * bt;
                    y += ((Point2D) pl.get(i + j)).getY() * bt;
                }
                p = new PointDouble(x, y);
                list.add(p);
            }
        }
    }

    /**
     * 
     * DOC amaumont Comment method "BezierTerm".
     * 
     * @param d
     * @param i
     * @param t
     * @return
     */
    double bezierTerm(int d, int i, int t) {
        return (double) (Math.pow(tmp[t], i) * Math.pow(1 - tmp[t], d - i) * Math.comb(d, i));
    }

}
