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
package org.talend.commons.ui.swt.drawing.background;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

public interface IBackgroundRefresher {

    public abstract void refreshBackground();

    public abstract void refreshBackgroundWithLimiter();

    public Color getBackgroundColor();

    public void setBackgroundColor(Color backgroundColor);

    public Point convertPointToCommonParentOrigin(Point point, Composite child);

    public boolean isAntialiasAllowed();

    public void dispose();
}
