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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.talend.commons.ui.swt.drawing.link.StyleLink.EDirection;

public interface IStyleLink {

    public EDirection getDirection();

    public void setDirection(EDirection direction);

    public Color getBackgroundColor();

    public void setBackgroundColor(Color backgroundColor);

    public Color getForegroundColor();

    public void setForegroundColor(Color foregroundColor);

    public IExtremityDrawableLink getExtremity1();

    public void setExtremity1(IExtremityDrawableLink extremity1);

    public IExtremityDrawableLink getExtremity2();

    public void setExtremity2(IExtremityDrawableLink extremity2);

    public IDrawableLink getDrawableLink();

    public void setDrawableLink(IDrawableLink drawableLink);

    public int getLineCap();

    public void setLineCap(int lineCap);

    public int[] getLineDash();

    public void setLineDash(int[] lineDash);

    public int getLineJoin();

    public void setLineJoin(int lineJoin);

    public int getLineStyle();

    public void setLineStyle(int lineStyle);

    public int getLineWidth();

    public void setLineWidth(int lineWidth);

    public void apply(GC gc);

}
