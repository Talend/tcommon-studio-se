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
package org.talend.commons.ui.swt.drawing.background;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public interface IBackgroundRefresher {

    public abstract void refreshBackground();

    /**
     * DOC amaumont Comment method "updateBackgroundWithLimiter".
     */
    public abstract void refreshBackgroundWithLimiter();

    /**
     * Getter for backgroundColor.
     * 
     * @return the backgroundColor
     */
    public Color getBackgroundColor();

    /**
     * Sets the backgroundColor.
     * 
     * @param backgroundColor the backgroundColor to set
     */
    public void setBackgroundColor(Color backgroundColor);

    public Point convertPointToCommonParentOrigin(Point point, Composite child);

    /**
     * Getter for antialiasActivated.
     * 
     * @return the antialiasActivated
     */
    public boolean isAntialiasAllowed();

    public void dispose();
}
