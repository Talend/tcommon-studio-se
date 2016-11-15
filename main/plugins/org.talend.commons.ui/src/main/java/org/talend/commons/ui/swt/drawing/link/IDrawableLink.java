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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public interface IDrawableLink {

    public abstract void draw(GC gc);

    public Point getPoint1();

    public void setPoint1(Point point1);

    public Point getPoint2();

    public void setPoint2(Point point2);

    public Rectangle getBoundsOfCalculate();

    public void setBoundsOfCalculate(Rectangle calculateBounds);

    public Integer getConnectorWidth();

    public void setConnectorWidth(Integer connectorWidth);

    public IStyleLink getStyle();

    public void setStyle(IStyleLink style);

}
