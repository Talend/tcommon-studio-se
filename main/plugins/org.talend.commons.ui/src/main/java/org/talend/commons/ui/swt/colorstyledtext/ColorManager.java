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
package org.talend.commons.ui.swt.colorstyledtext;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.ui.swt.colorstyledtext.scanner.ColoringScanner;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: ColorManager.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class ColorManager {

    public static final RGB DEFAULT_STRING_COLOR = new RGB(0, 0, 0);

    public static final RGB DEFAULT_KEYWORD1_COLOR = new RGB(50, 32, 160);

    public static final RGB DEFAULT_KEYWORD2_COLOR = new RGB(240, 160, 80);

    public static final RGB DEFAULT_KEYWORD3_COLOR = new RGB(160, 32, 100);

    public static final RGB DEFAULT_COMMENT1_COLOR = new RGB(0, 140, 34);

    public static final RGB DEFAULT_COMMENT2_COLOR = new RGB(34, 140, 0);

    public static final RGB DEFAULT_LITERAL1_COLOR = new RGB(0, 0, 255);

    public static final RGB DEFAULT_LITERAL2_COLOR = new RGB(160, 100, 240);

    public static final RGB DEFAULT_LABEL_COLOR = new RGB(160, 0, 240);

    public static final RGB DEFAULT_FUNCTION_COLOR = new RGB(160, 32, 0);

    public static final RGB DEFAULT_MARKUP_COLOR = new RGB(178, 0, 34);

    public static final RGB DEFAULT_OPERATOR_COLOR = new RGB(178, 34, 0);

    public static final RGB DEFAULT_DIGIT_COLOR = new RGB(160, 32, 0);

    public static final RGB DEFAULT_INVALID_COLOR = new RGB(178, 0, 34);

    public static final String NULL_COLOR = "nullColor"; //$NON-NLS-1$

    public static final String COMMENT1_COLOR = "comment1Color"; //$NON-NLS-1$

    public static final String COMMENT2_COLOR = "comment2Color"; //$NON-NLS-1$

    public static final String LITERAL1_COLOR = "literal1Color"; //$NON-NLS-1$

    public static final String LITERAL2_COLOR = "literal2Color"; //$NON-NLS-1$

    public static final String LABEL_COLOR = "labelColor"; //$NON-NLS-1$

    public static final String KEYWORD1_COLOR = "keyword1Color"; //$NON-NLS-1$

    public static final String KEYWORD2_COLOR = "keyword2Color"; //$NON-NLS-1$

    public static final String KEYWORD3_COLOR = "keyword3Color"; //$NON-NLS-1$

    public static final String FUNCTION_COLOR = "functionColor"; //$NON-NLS-1$

    public static final String MARKUP_COLOR = "markupColor"; //$NON-NLS-1$

    public static final String OPERATOR_COLOR = "operatorColor"; //$NON-NLS-1$

    public static final String DIGIT_COLOR = "digitColor"; //$NON-NLS-1$

    public static final String INVALID_COLOR = "invalidColor"; //$NON-NLS-1$

    public static final String BOLD_SUFFIX = "Bold"; //$NON-NLS-1$

    private Map<String, Color> colorMap;

    private IPreferenceStore store;

    private Map<String, String> typeToColorMap;

    public ColorManager(IPreferenceStore store) {
        colorMap = new HashMap<String, Color>();
        this.store = store;
        initTypeToColorMap();
        initDefaultColors(store);
    }

    public Color getColor(String colorName) {
        RGB prefColor = PreferenceConverter.getColor(store, colorName);
        Color color = null;
        if (colorMap.containsKey(colorName) && (colorMap.get(colorName)).getRGB().equals(prefColor)) {
            color = (Color) colorMap.get(colorName);
        } else {
            color = new Color(Display.getDefault(), prefColor);
            colorMap.put(colorName, color);
        }
        return color;
    }

    public void dispose() {
        Collection colors = colorMap.values();
        for (Iterator iter = colors.iterator(); iter.hasNext();) {
            Color color = (Color) iter.next();
            colorMap.remove(color);
            color.dispose();
        }
        colorMap = null;
    }

    /**
     * Converts the ColoringPartitionScanner's type name to the appropriate preference store color name.
     * 
     * @param type
     * @return String
     */
    public String colorForType(String type) {
        return (String) typeToColorMap.get(type);
    }

    protected void initTypeToColorMap() {
        typeToColorMap = new HashMap<String, String>();
        typeToColorMap.put(ColoringScanner.COMMENT1, COMMENT1_COLOR);
        typeToColorMap.put(ColoringScanner.COMMENT2, COMMENT2_COLOR);
        typeToColorMap.put(ColoringScanner.LITERAL1, LITERAL1_COLOR);
        typeToColorMap.put(ColoringScanner.LITERAL2, LITERAL2_COLOR);
        typeToColorMap.put(ColoringScanner.LABEL, LABEL_COLOR);
        typeToColorMap.put(ColoringScanner.KEYWORD1, KEYWORD1_COLOR);
        typeToColorMap.put(ColoringScanner.KEYWORD2, KEYWORD2_COLOR);
        typeToColorMap.put(ColoringScanner.KEYWORD3, KEYWORD3_COLOR);
        typeToColorMap.put(ColoringScanner.FUNCTION, FUNCTION_COLOR);
        typeToColorMap.put(ColoringScanner.MARKUP, MARKUP_COLOR);
        typeToColorMap.put(ColoringScanner.OPERATOR, OPERATOR_COLOR);
        typeToColorMap.put(ColoringScanner.DIGIT, DIGIT_COLOR);
        typeToColorMap.put(ColoringScanner.INVALID, INVALID_COLOR);
        typeToColorMap.put(ColoringScanner.NULL, NULL_COLOR);
    }

    public Color getColorForType(String type) {
        String colorName = colorForType(type);
        if (colorName == null) {
            colorName = NULL_COLOR;
        }
        return getColor(colorName);
    }

    public static void initDefaultColors(IPreferenceStore store) {
        store.setDefault(NULL_COLOR, StringConverter.asString(DEFAULT_STRING_COLOR));
        store.setDefault(COMMENT1_COLOR, StringConverter.asString(DEFAULT_COMMENT1_COLOR));
        store.setDefault(COMMENT2_COLOR, StringConverter.asString(DEFAULT_COMMENT2_COLOR));
        store.setDefault(LITERAL1_COLOR, StringConverter.asString(DEFAULT_LITERAL1_COLOR));
        store.setDefault(LITERAL2_COLOR, StringConverter.asString(DEFAULT_LITERAL2_COLOR));
        store.setDefault(LABEL_COLOR, StringConverter.asString(DEFAULT_LABEL_COLOR));
        store.setDefault(KEYWORD1_COLOR, StringConverter.asString(DEFAULT_KEYWORD1_COLOR));
        store.setDefault(KEYWORD2_COLOR, StringConverter.asString(DEFAULT_KEYWORD2_COLOR));
        store.setDefault(KEYWORD3_COLOR, StringConverter.asString(DEFAULT_KEYWORD3_COLOR));
        store.setDefault(FUNCTION_COLOR, StringConverter.asString(DEFAULT_FUNCTION_COLOR));
        store.setDefault(MARKUP_COLOR, StringConverter.asString(DEFAULT_MARKUP_COLOR));
        store.setDefault(OPERATOR_COLOR, StringConverter.asString(DEFAULT_OPERATOR_COLOR));
        store.setDefault(DIGIT_COLOR, StringConverter.asString(DEFAULT_DIGIT_COLOR));
        store.setDefault(INVALID_COLOR, StringConverter.asString(DEFAULT_INVALID_COLOR));

        String bold = BOLD_SUFFIX;
        store.setDefault(COMMENT1_COLOR + bold, false);
        store.setDefault(COMMENT2_COLOR + bold, true);
        store.setDefault(DIGIT_COLOR + bold, false);
        store.setDefault(FUNCTION_COLOR + bold, false);
        store.setDefault(INVALID_COLOR + bold, false);
        store.setDefault(KEYWORD1_COLOR + bold, false);
        store.setDefault(KEYWORD2_COLOR + bold, false);
        store.setDefault(KEYWORD3_COLOR + bold, false);
        store.setDefault(LABEL_COLOR + bold, false);
        store.setDefault(LITERAL1_COLOR + bold, false);
        store.setDefault(LITERAL2_COLOR + bold, false);
        store.setDefault(MARKUP_COLOR + bold, false);
        store.setDefault(OPERATOR_COLOR + bold, false);
        store.setDefault(NULL_COLOR + bold, false);
    }

    public int getStyleFor(String colorName) {
        String boldSuffix = BOLD_SUFFIX;
        if (colorName == null) {
            colorName = NULL_COLOR;
        }
        String boldName = colorName + boldSuffix;
        boolean isBold = store.getBoolean(boldName);
        return isBold ? SWT.BOLD : SWT.NORMAL;
    }

    public int getStyleForType(String type) {
        return getStyleFor(colorForType(type));
    }

}
