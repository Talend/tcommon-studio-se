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
public class ExtremityWestArrow extends AbstractExtremityDrawableLink {

    /**
     * DOC amaumont TipWestArrow constructor comment.
     * 
     * @param styleLink
     */
    public ExtremityWestArrow(IStyleLink styleLink) {
        super(styleLink);
    }

    /**
     * DOC amaumont TipWestArrow constructor comment.
     * 
     * @param styleLink
     * @param xOffset
     * @param yOffset
     */
    public ExtremityWestArrow(IStyleLink styleLink, int xOffset, int yOffset) {
        super(styleLink, xOffset, yOffset);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.drawing.link.ITipLink#draw(org.eclipse.swt.graphics.GC,
     * org.eclipse.swt.graphics.Point)
     */
    public void draw(GC gc, Point point) {
        Color previousBackground = gc.getBackground();
        gc.setBackground(styleLink.getForegroundColor());
        gc.fillPolygon(new int[] { point.x + xOffset, point.y + yOffset - 1 - ExtremityEastArrow.HEIGHT_ARROW / 2,
                point.x + xOffset - ExtremityEastArrow.WIDTH_ARROW, point.y + yOffset - 1,
                point.x + xOffset - ExtremityEastArrow.WIDTH_ARROW, point.y + yOffset, point.x + xOffset,
                point.y + yOffset + ExtremityEastArrow.HEIGHT_ARROW / 2, });
        gc.setBackground(previousBackground);
    }

    public Point getSize() {
        return new Point(ExtremityEastArrow.WIDTH_ARROW, ExtremityEastArrow.HEIGHT_ARROW);
    }

}
