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
package org.talend.commons.ui.swt.linking;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.drawing.background.IBgDrawableComposite;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class BgDrawableComposite implements IBgDrawableComposite {

    private Composite commonParent;

    private Point offsetPoint = new Point(0, 0);

    /**
     * DOC amaumont DrawableBackground constructor comment.
     * 
     * @param commonParent
     */
    public BgDrawableComposite(Composite commonParent) {
        this.commonParent = commonParent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.drawing.background.IDrawableComposite#drawBackground(org.eclipse.swt.graphics.GC)
     */
    public abstract void drawBackground(GC gc);

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.drawing.background.IDrawableComposite#getComposite()
     */
    public Composite getBgDrawableComposite() {
        return this.commonParent;
    }

    public void setOffset(Point offsetPoint) {
        this.offsetPoint = offsetPoint;
    }

    public Point getOffset() {
        return this.offsetPoint;
    }
}
