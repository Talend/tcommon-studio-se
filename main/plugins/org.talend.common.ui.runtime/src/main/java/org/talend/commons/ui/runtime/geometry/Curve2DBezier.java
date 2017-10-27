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
// Curve2DBezier.java
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

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.swt.graphics.GC;

/**
 * 
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: Curve2DBezier.java 7189 2007-11-23 17:06:19Z amaumont $
 * 
 */
public class Curve2DBezier extends Curve2D {

    double[] tmp;

    double time = 1;

    int size;

    int n;

    private int[] points;

    private int yMaxVisiblePoints;

    private int yMinVisiblePoints;

    // constructor
    public Curve2DBezier() {
        super();
    }

    public Curve2DBezier(Point2DList pl) {
        super(pl);
    }

    public Curve2DBezier(Point2DList pl, double t) {
        super(pl);
        time = t;
    }

    // getTime
    public double getTime() {
        return time;
    }

    // setTime
    public void setTime(double t) {
        time = Math.min(Math.max(t, 0), 1);
    }

    // computeSegments
    @Override
    void computeSegments() {
        Point2DList pl = (Point2DList) plist;

        n = pl.size() - 1;

        size = (int) (subdiv * n * time);

        if (size > getMaximumSegements()) {
            size = getMaximumSegements();
        }

        if (size < getMinimumSegements()) {
            size = getMinimumSegements();
        }

        tmp = new double[size + 1];
        for (int i = 0; i <= size; i++) {
            tmp[i] = time * i / size;
        }

        double bt;

        int[] pointsTmp = new int[2 * (size + 1)];
        double x, y;
        int indexTStart = -1;
        int indexTEnd = -1;
        boolean firstInvisiblePoint = true;
        for (int t = 0; t <= size; t++) {
            x = 0;
            y = 0;
            for (int i = 0; i <= n; i++) {
                bt = Math.comb(n, i) * Math.pow(1 - tmp[t], n - i) * Math.pow(tmp[t], i);
                x += pl.get(i).getX() * bt;
                y += pl.get(i).getY() * bt;
            }

            if (y >= yMinVisiblePoints && y <= yMaxVisiblePoints || firstInvisiblePoint || pointsTmp.length > 4) {
                if (indexTStart == -1) {
                    indexTStart = t;
                }
                pointsTmp[2 * t] = (int) java.lang.Math.round(x);
                pointsTmp[2 * t + 1] = (int) java.lang.Math.round(y);
                if (firstInvisiblePoint && y < yMinVisiblePoints && y < yMaxVisiblePoints) {
                    firstInvisiblePoint = false;
                }
            } else if (indexTStart != -1) {
                indexTEnd = t;
                break;
            }

        }
        if (indexTEnd == -1 && indexTStart != -1) {
            indexTEnd = size + 1;
        }
        // System.out.println("size="+size + " indexTStart=" + indexTStart + " indexTEnd="+indexTEnd);
        if (indexTEnd == -1) {
            this.points = new int[0];
        } else {
            this.points = ArrayUtils.subarray(pointsTmp, 2 * indexTStart, 2 * indexTEnd);
            // System.out.println("pointsTmp=");
            // for (int i = pointsTmp.length - 4; i < pointsTmp.length; i++) {
            // System.out.println(pointsTmp[i]);
            // }
            //
            //
            // System.out.println("this.points=");
            // for (int i = points.length - 4; i < points.length; i++) {
            // System.out.println(points[i]);
            // }
        }
    }

    // double BezierTerm(int i, int t) {
    // return (double) (Math.comb(n, i) * Math.pow(1 - tmp[t], n - i) * Math.pow(tmp[t], i));
    // }

    public void draw(GC gc, Point2DList current, Point2DList active, int yMinVisiblePoints, int yMaxVisiblePoints) {
        if (plist == null) {
            return;
        }

        Point2DList pl = (Point2DList) plist;

        if (!visible || pl.size() <= (nbMin - 1)) {
            return;
        }
        // g.setStroke(new BasicStroke(THICKNESS));

        if (pl == current || pl == active) {
            this.yMinVisiblePoints = yMinVisiblePoints;
            this.yMaxVisiblePoints = yMaxVisiblePoints;
            computeSegments();
        }
        gc.drawPolyline(points);
        // System.out.println("lastPoint=" + points[points.length - 2] + "," + points[points.length - 1]);
    }

}
