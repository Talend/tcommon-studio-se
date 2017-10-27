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
public class ExtremityEastArrow extends AbstractExtremityDrawableLink {

    public static final int WIDTH_IMAGE_ARROW = 5;

    public static final int HEIGHT_IMAGE_ARROW = 10;

    public static final int WIDTH_ARROW = 5;

    public static final int HEIGHT_ARROW = 10;

    public static final int FOREGROUND_INDEX_PALETTE_COLOR = 1;

    /**
     * DOC amaumont TipArrow constructor comment.
     */
    public ExtremityEastArrow(IStyleLink styleLink) {
        super(styleLink);
    }

    /**
     * DOC amaumont TipEastArrow constructor comment.
     * 
     * @param styleLink
     * @param xOffset
     * @param yOffset
     */
    public ExtremityEastArrow(IStyleLink styleLink, int xOffset, int yOffset) {
        super(styleLink, xOffset, yOffset);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.drawing.link.AbstractTipLink#draw(org.eclipse.swt.graphics.GC)
     */
    @Override
    public void draw(GC gc, Point point) {
        Color previousBackground = gc.getBackground();
        gc.setBackground(styleLink.getForegroundColor());
        gc.setLineWidth(1);
        int[] arrow = new int[] { point.x + xOffset, point.y + yOffset - HEIGHT_ARROW / 2 - 1,
                point.x + xOffset + WIDTH_ARROW, point.y + yOffset, point.x + xOffset + WIDTH_ARROW, point.y + yOffset,
                point.x + xOffset, point.y + yOffset + HEIGHT_ARROW / 2 + 1, };
        gc.fillPolygon(arrow);
        gc.setBackground(previousBackground);
    }

    public Point getSize() {
        return new Point(WIDTH_ARROW, HEIGHT_ARROW);
    }
}
