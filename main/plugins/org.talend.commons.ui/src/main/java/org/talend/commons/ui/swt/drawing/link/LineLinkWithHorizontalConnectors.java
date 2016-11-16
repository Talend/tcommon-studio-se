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

/**
 * Draw an horizontal Bezier link.
 */
public class LineLinkWithHorizontalConnectors extends AbstractDrawableLink {

    private static final int CONNECTOR_WIDTH_DEFAULT = 20;

    public LineLinkWithHorizontalConnectors(StyleLink style) {
        super(style);
        connectorWidth = CONNECTOR_WIDTH_DEFAULT;
    }

    @Override
    public void drawBody(GC gc) {

        gc.drawPolyline(new int[] { point1.x, point1.y, point1.x + connectorWidth, point1.y, point2.x - connectorWidth,
                point2.y, point2.x, point2.y });
    }

}
