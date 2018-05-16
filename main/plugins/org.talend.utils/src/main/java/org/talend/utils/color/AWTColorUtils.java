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
package org.talend.utils.color;

import java.awt.Color;

/**
 * @author scorreia
 * 
 * Utility class to handle colors.
 */
public final class AWTColorUtils {

    public static final String[] COLOR_STRS = { "#236192", "#C4D600", "#DB662A", "#F7A800", "#787121", "#00A9CE", "#ECAB7C",
            "#B8B370", "#D4D3D3", "#83D3E6", "#FFD38B" };

    private static final Color[] COLORS = initializeColors();

    /**
     * Method "getColor".
     * 
     * @param idx a positive index
     * @return a color among a list of predefined colors (White is not returned).
     */
    public static Color getColor(int idx) {
        assert idx >= 0;
        idx = idx % COLORS.length;
        return COLORS[idx];
    }

    private static Color[] initializeColors() {
        Color[] colors = new Color[COLOR_STRS.length];
        int i = 0;
        for (String str : COLOR_STRS) {
            colors[i] = Color.decode(str);
            i++;
        }
        return colors;
    }

    private AWTColorUtils() {
    }
}
