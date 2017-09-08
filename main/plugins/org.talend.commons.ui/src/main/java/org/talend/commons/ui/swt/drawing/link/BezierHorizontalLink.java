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
import org.talend.commons.ui.runtime.geometry.Curve2DBezier;
import org.talend.commons.ui.runtime.geometry.Point2D;
import org.talend.commons.ui.runtime.geometry.Point2DList;

/**
 * Draw an horizontal Bezier link. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class BezierHorizontalLink extends AbstractDrawableLink {

    private Curve2DBezier curve;

    private static final int CONNECTOR_WIDTH_DEFAULT = 70;

    private static final int DISTANCE_REFERENCE = 20000;

    /**
     * DOC amaumont BezierLink constructor comment.
     */
    public BezierHorizontalLink(StyleLink style) {
        super(style);
        connectorWidth = CONNECTOR_WIDTH_DEFAULT;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.drawing.link.ICAbstractGraphicalLink#draw(org.eclipse.swt.graphics.GC)
     */
    @Override
    public void drawBody(GC gc) {

        Point2DList pl = null;

        double distance;
        // int spaceBetweenPoints = 1;

        distance = new java.awt.Point(point1.x, point1.y).distance(point2.x, point2.y);

        if (curve == null) {
            curve = new Curve2DBezier();
            pl = new Point2DList();
            curve.setPointList(pl);
            for (int i = 0; i < 5; i++) {
                pl.add(new Point2D());
            }
        } else {
            pl = (Point2DList) curve.getPointList();
        }

        int subdiv = (int) (DISTANCE_REFERENCE / distance);
        curve.setSubdiv(subdiv > 0 ? subdiv : 2);
        double coefDistConnectorWidth = distance / 1500 * connectorWidth;

        int point1xHalfTableWidth1ConnWidth = point1.x + connectorWidth;
        int point2xHalfTableWidth2ConnWidth = point2.x - connectorWidth;

        ((Point2D) pl.get(0)).setLocation(point1.x, point1.y);
        ((Point2D) pl.get(1)).setLocation(point1xHalfTableWidth1ConnWidth + coefDistConnectorWidth, point1.y);

        ((Point2D) pl.get(2))
                .setLocation(
                        (point1xHalfTableWidth1ConnWidth + coefDistConnectorWidth + (point2xHalfTableWidth2ConnWidth - coefDistConnectorWidth)) / 2,
                        (point1.y + (point2.y)) / 2);

        ((Point2D) pl.get(3)).setLocation(point2xHalfTableWidth2ConnWidth - coefDistConnectorWidth, point2.y);
        ((Point2D) pl.get(4)).setLocation(point2.x, point2.y);

        int heightCalculate = 0;
        if (boundsOfCalculate == null) {
            heightCalculate = gc.getClipping().height;
        } else {
            heightCalculate = boundsOfCalculate.height;
        }

        curve.draw(gc, pl, pl, 0, heightCalculate);
    }

}
