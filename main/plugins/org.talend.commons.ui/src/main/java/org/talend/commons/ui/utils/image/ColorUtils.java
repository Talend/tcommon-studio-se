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
package org.talend.commons.ui.utils.image;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * ggu class global comment. Detailled comment
 */
public class ColorUtils {

    private static RGB DEFAULT_COLOR = new RGB(255, 255, 255);// white

    private final static String SEMICOLON = ";"; //$NON-NLS-1$

    private final static String COMMA = ","; //$NON-NLS-1$

    /*
     * for tPostjob/tPrejob components
     */
    // public static final Color SPECIAL_SUBJOB_TITLE_COLOR = new Color(null, 230, 100, 0);
    //
    // public final static Color SPECIAL_SUBJOB_COLOR = new Color(null, 255, 220, 180);
    /**
     * return such as "255;255;255"
     */
    public static String getColorValue(Color color) {
        RGB value = color.getRGB();
        if (color == null) {
            value = DEFAULT_COLOR;
        }
        StringBuffer sb = new StringBuffer();

        sb.append(value.red);
        sb.append(SEMICOLON);
        sb.append(value.green);
        sb.append(SEMICOLON);
        sb.append(value.blue);

        return sb.toString();
    }

    /**
     * return such as "255;255;255"
     */
    public static String getRGBValue(RGB rgb) {
        if (rgb == null) {
            rgb = DEFAULT_COLOR;
        }
        StringBuffer sb = new StringBuffer();

        sb.append(rgb.red);
        sb.append(SEMICOLON);
        sb.append(rgb.green);
        sb.append(SEMICOLON);
        sb.append(rgb.blue);

        return sb.toString();
    }

    public static RGB parseStringToRGBNoDefault(String color) {
        return parseStringToRGB(color, null, false);
    }

    public static RGB parseStringToRGB(String color) {
        return parseStringToRGB(color, null);
    }

    public static RGB parseStringToRGB(String color, RGB defaultColor) {
        return parseStringToRGB(color, defaultColor, true);
    }

    /**
     * 
     * ggu Comment method "parseStringToColor".
     * 
     * can parse the "255,255,255" and "255;255;255".
     */
    private static RGB parseStringToRGB(String color, RGB defaultColor, boolean defaultEnable) {
        if (defaultColor == null && defaultEnable) {
            defaultColor = DEFAULT_COLOR;
        }
        if (color == null) {
            return defaultColor;
        }
        if (color.contains(COMMA)) {
            RGB rgb = StringConverter.asRGB(color, defaultColor);
            return rgb;
        }
        if (color.contains(SEMICOLON)) {
            try {
                StringTokenizer stok = new StringTokenizer(color, SEMICOLON);
                String red = stok.nextToken().trim();
                String green = stok.nextToken().trim();
                String blue = stok.nextToken().trim();

                int rval = 0, gval = 0, bval = 0;

                rval = Integer.parseInt(red);
                gval = Integer.parseInt(green);
                bval = Integer.parseInt(blue);

                return new RGB(rval, gval, bval);
            } catch (NumberFormatException e) {
                return defaultColor;
            } catch (NoSuchElementException e) {
                return defaultColor;
            }
        }
        return defaultColor;
    }

    /**
     * 
     * ggu Comment method "transform".
     * 
     * "255,255,255" (such as preference value) transform to "255;255;255"
     */
    public static String transform(String color) {
        if (color == null) {
            return null;
        }
        return color.replaceAll(COMMA, SEMICOLON);
    }

    private static Map<RGB, Color> colorsMap = new HashMap<RGB, Color>();

    public static Color getCacheColor(RGB rgb) {
        if (rgb != null) {
            Color color = colorsMap.get(rgb);
            if (color == null || color.isDisposed()) {
                color = new Color(null, rgb);
                colorsMap.put(rgb, color);
            }
            return color;
        }
        return null;
    }

    public static RGB stringToRGB(String string) {
        RGB rgb;
        int r = 0;
        int g = 0;
        int b = 0;
        StringTokenizer token = new StringTokenizer(string, ";"); //$NON-NLS-1$
        if (token.hasMoreTokens()) {
            r = new Integer(token.nextToken());
        }
        if (token.hasMoreTokens()) {
            g = new Integer(token.nextToken());
        }
        if (token.hasMoreTokens()) {
            b = new Integer(token.nextToken());
        }
        rgb = new RGB(r, g, b);
        return rgb;
    }
}
