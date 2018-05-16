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
package org.talend.commons.ui.gmf.draw2d;

import org.eclipse.gef.editparts.ZoomListener;


/**
 * Listens to animated zoom changes.
 * @author Steve Shaw
 */
public interface AnimatedZoomListener extends ZoomListener {

    /**
     * Called whenever the ZoomManager's starts an animated
     * zoom.
     */
    void animatedZoomStarted();

    /**
     * Called whenever the ZoomManager's ends an animated
     * zoom.
     */
    void animatedZoomEnded();
}
