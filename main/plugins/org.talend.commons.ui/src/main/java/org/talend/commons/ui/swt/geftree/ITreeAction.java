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
package org.talend.commons.ui.swt.geftree;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.Image;

/**
 * cli class global comment. Detailled comment
 */
public interface ITreeAction {

    public void setSelected(boolean selected);

    public boolean enableSelect();

    public boolean enableDoubleClick();

    public boolean enableHover();

    public void setHoverColor(IFigure fig, boolean enter);

    public Image getElementImage();

    public void dispose();

}
