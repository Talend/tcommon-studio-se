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
package org.talend.presentation.onboarding.ui.runtimedata;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * created by cmeng on Sep 11, 2015 Detailled comment
 *
 */
public class OnBoardingPresentationData {

    public static final String DIRECTION_LEFT_SIDE = "LEFT_SIDE"; //$NON-NLS-1$

    public static final String DIRECTION_RIGHT_SIDE = "RIGHT_SIDE"; //$NON-NLS-1$

    public static final String DIRECTION_TOP_SIDE = "TOP_SIDE"; //$NON-NLS-1$

    public static final String DIRECTION_BOTTOM_SIDE = "BOTTOM_SIDE"; //$NON-NLS-1$

    private Point pointer;

    private Rectangle contentArea;

    private String pointerDirrection;

    private OnBoardingPageBean pageBean;

    public Rectangle getContentArea() {
        return this.contentArea;
    }

    public void setContentArea(Rectangle contentArea) {
        this.contentArea = contentArea;
    }

    public Point getPointer() {
        return this.pointer;
    }

    public void setPointer(Point pointer) {
        this.pointer = pointer;
    }

    public String getPointerDirrection() {
        return this.pointerDirrection;
    }

    public void setPointerDirrection(String pointerDirrection) {
        this.pointerDirrection = pointerDirrection;
    }

    public OnBoardingPageBean getPageBean() {
        return this.pageBean;
    }

    public void setPageBean(OnBoardingPageBean pageBean) {
        this.pageBean = pageBean;
        if (this.pageBean != null) {
            Point size = this.pageBean.getSize();
            if (size != null) {
                if (contentArea == null) {
                    contentArea = new Rectangle(0, 0, 0, 0);
                }
                contentArea.width = size.x;
                contentArea.height = size.y;
            }
        }
    }

}
