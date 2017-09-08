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

/**
 * Draw a vertical link with horizontal connector and rounded corners between connectors and vertical line. <br/>
 * 
 * $Id$
 * 
 */
public class VerticalRoundedCornerLink extends AbstractDrawableLink {

    private static final int CONNECTOR_WIDTH_DEFAULT = 8;

    private static final int RADIUS_DEFAULT = 6;

    private int radius;

    public VerticalRoundedCornerLink(IStyleLink style) {
        super(style);
        connectorWidth = CONNECTOR_WIDTH_DEFAULT;
        this.radius = RADIUS_DEFAULT;
    }

    public VerticalRoundedCornerLink(IStyleLink style, int radius) {
        super(style);
        connectorWidth = CONNECTOR_WIDTH_DEFAULT;
        this.radius = radius;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.drawing.link.AbstractDrawableLink#drawBody(org.eclipse.swt.graphics.GC)
     */
    @Override
    protected void drawBody(GC gc) {
        boolean point1Above = point1.y > point2.y;

        int mult = point1Above ? 1 : -1;

        int keyLinksCounter = 0;

        int xOffset = 4 * keyLinksCounter + 1;

        gc.drawLine(point1.x, point1.y, point1.x - connectorWidth - xOffset, point1.y);

        gc.drawArc(point1.x - connectorWidth - radius - xOffset, point1.y - (point1Above ? 2 * radius : 0), 2 * radius,
                2 * radius, point1Above ? 180 : 90, 90);

        // // in1 pr
        gc.drawLine(point1.x - connectorWidth - radius - xOffset, point1.y - mult * radius, point2.x - connectorWidth
                - radius - xOffset, point2.y + mult * radius);

        gc.drawArc(point2.x - connectorWidth - radius - xOffset, point2.y - (point1Above ? 0 : 2 * radius), 2 * radius,
                2 * radius, point1Above ? 90 : 180, 90);

        // // connector pr (in)
        gc.drawLine(point2.x - connectorWidth - xOffset, point2.y, point2.x, point2.y);

        keyLinksCounter++;

    }

}
