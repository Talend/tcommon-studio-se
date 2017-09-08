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
package org.talend.themes.core.elements.stylesettings;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * created by cmeng on Jan 30, 2015 Detailled comment
 *
 */
public class CommonCSSStyleSetting {

    protected Map<Color, Color> needDisposedColors = new HashMap<Color, Color>();;

    protected Date timeStamp = new Date();

    public void updateTimeStamp() {
        this.timeStamp = new Date();
    }

    public Date getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static Color getColorByRGB(RGB rgb) {
        Color color = null;
        if (!JFaceResources.getColorRegistry().hasValueFor(rgb.toString())) {
            JFaceResources.getColorRegistry().put(rgb.toString(), rgb);
        }
        color = JFaceResources.getColorRegistry().get(rgb.toString());
        return color;
    }

    public static void copyBorderSetting(Border from, Border to) {
        Insets toInsets = to.getInsets(null);
        Insets fromInsets = from.getInsets(null);
        toInsets.top = fromInsets.top;
        toInsets.bottom = fromInsets.bottom;
        toInsets.left = fromInsets.left;
        toInsets.right = fromInsets.right;
    }
}
