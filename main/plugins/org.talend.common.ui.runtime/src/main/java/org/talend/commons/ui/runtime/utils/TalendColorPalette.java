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
package org.talend.commons.ui.runtime.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.widgets.Display;

/**
 * This class contains the corporate colors from specification chapter of Talend Branding Guide document. Correct usage
 * of the color palette will ensure development of graphic communications that are clear, engaging and recognizable.
 * 
 * @author sizhaoliu
 */
public class TalendColorPalette {

    private static Device device = Display.getCurrent();

    /**
     * PRIMARY COLOR PALETTE
     * <p/>
     * The Talend primary palette should dominate the graphic design of all Talend communications, but work in concert
     * with the secondary palette.
     */

    public static final Color PRIMARY_BLUE = new Color(device, 0, 86, 149);

    public static final Color PRIMARY_GREEN = new Color(device, 193, 216, 47);

    public static final Color PRIMARY_GRAY = new Color(device, 113, 112, 115);

    public static final Color PRIMARY_WHITE = device.getSystemColor(SWT.COLOR_WHITE);

    public static final java.awt.Color PRIMARY_BLUE_AWT = new java.awt.Color(0, 86, 149);

    public static final java.awt.Color PRIMARY_GREEN_AWT = new java.awt.Color(193, 216, 47);

    public static final java.awt.Color PRIMARY_GRAY_AWT = new java.awt.Color(113, 112, 115);

    public static final java.awt.Color PRIMARY_WHITE_AWT = java.awt.Color.WHITE;

    /**
     * SECONDARY COLOR PALETTE
     * <p/>
     * Our secondary color palette includes a more vivid spectrum of colors for use as bold accents. These colors should
     * be used together when possible.
     */
    public static final Color SECONDARY_BLUE = new Color(device, 0, 175, 219);

    public static final Color SECONDARY_GREEN = new Color(device, 129, 124, 0);

    public static final Color SECONDARY_GRAY = new Color(device, 186, 188, 190);

    public static final Color SECONDARY_ORANGE = new Color(device, 232, 109, 31);

    public static final Color SECONDARY_YELLOW = new Color(device, 253, 185, 19);

    public static final java.awt.Color SECONDARY_BLUE_AWT = new java.awt.Color(0, 175, 219);

    public static final java.awt.Color SECONDARY_GREEN_AWT = new java.awt.Color(129, 124, 0);

    public static final java.awt.Color SECONDARY_GRAY_AWT = new java.awt.Color(186, 188, 190);

    public static final java.awt.Color SECONDARY_ORANGE_AWT = new java.awt.Color(232, 109, 31);

    public static final java.awt.Color SECONDARY_YELLOW_AWT = new java.awt.Color(253, 185, 19);

    /**
     * TERTIARY COLOR PALETTE
     * <p/>
     * The tertiary color palette can distinguish types of information in charts, graphs, data sheets and multimedia
     * applications.
     */

    public static final Color TERTIARY_BLUE = new Color(device, 119, 208, 234);

    public static final Color TERTIARY_GREEN = new Color(device, 190, 185, 117);

    public static final Color TERTIARY_GRAY = new Color(device, 219, 220, 221);

    public static final Color TERTIARY_ORANGE = new Color(device, 244, 175, 128);

    public static final Color TERTIARY_YELLOW = new Color(device, 255, 217, 143);

    public static final java.awt.Color TERTIARY_BLUE_AWT = new java.awt.Color(119, 208, 234);

    public static final java.awt.Color TERTIARY_GREEN_AWT = new java.awt.Color(190, 185, 117);

    public static final java.awt.Color TERTIARY_GRAY_AWT = new java.awt.Color(219, 220, 221);

    public static final java.awt.Color TERTIARY_ORANGE_AWT = new java.awt.Color(244, 175, 128);

    public static final java.awt.Color TERTIARY_YELLOW_AWT = new java.awt.Color(255, 217, 143);
}
