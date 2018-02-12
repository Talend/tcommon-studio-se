// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
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

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public interface IExtremityDrawableLink {

    /**
     * DOC amaumont Comment method "draw".
     * 
     * @param gc
     * @param point
     */
    public void draw(GC gc, Point point);

    /**
     * Getter for xOffset.
     * 
     * @return the xOffset
     */
    public int getXOffset();

    /**
     * Sets the xOffset.
     * 
     * @param offset the xOffset to set
     */
    public void setXOffset(int offset);

    /**
     * Getter for yOffset.
     * 
     * @return the yOffset
     */
    public int getYOffset();

    /**
     * Sets the yOffset.
     * 
     * @param offset the yOffset to set
     */
    public void setYOffset(int offset);

    public Point getSize();
}
