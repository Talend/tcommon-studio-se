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
package org.talend.commons.ui.swt.geftree.layout;

import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * cli class global comment. Detailled comment
 */

public class TreeAnimatingLayer extends Layer {

    /**
     * @see org.eclipse.draw2d.Figure#setBounds(org.eclipse.draw2d.geometry.Rectangle)
     */
    public void setBounds(Rectangle rect) {

        int x = bounds.x, y = bounds.y;

        boolean resize = (rect.width != bounds.width) || (rect.height != bounds.height), translate = (rect.x != x)
                || (rect.y != y);

        if (isVisible() && (resize || translate))
            erase();
        if (translate) {
            int dx = rect.x - x;
            int dy = rect.y - y;
            primTranslate(dx, dy);
        }
        bounds.width = rect.width;
        bounds.height = rect.height;
        // if (resize) Layouts dont depend on size.
        // invalidate();
        if (resize || translate) {
            fireFigureMoved();
            fireCoordinateSystemChanged();
            repaint();
        }
    }

}
