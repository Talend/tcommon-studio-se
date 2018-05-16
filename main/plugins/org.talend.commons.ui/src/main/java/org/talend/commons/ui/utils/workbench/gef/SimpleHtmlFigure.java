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
package org.talend.commons.ui.utils.workbench.gef;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Figure managing some simple HTML styles. <br/>
 * 
 * $Id: SimpleHtmlFigure.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class SimpleHtmlFigure extends Figure {

    private static final int A_7 = 7;

    private static final int A_5 = 5;

    private static final int A_16 = 16;

    private static final String TAG_BOLD_BEG = "<b>"; //$NON-NLS-1$

    private static final String TAG_BOLD_END = "</b>"; //$NON-NLS-1$

    private static final String TAG_ITALIC_BEG = "<i>"; //$NON-NLS-1$

    private static final String TAG_ITALIC_END = "</i>"; //$NON-NLS-1$

    private static final String TAG_FONT_BEG_1 = "<font"; //$NON-NLS-1$

    private static final String TAG_FONT_COLOR_BEG_1 = "color='"; //$NON-NLS-1$

    private static final String TAG_FONT_BEG_2 = "'>"; //$NON-NLS-1$

    private static final String TAG_FONT_END = "</font>"; //$NON-NLS-1$

    private static final String TAG_BR = "<br>"; //$NON-NLS-1$

    private int alpha = -1;

    private IFigure horizContainer;

    private String text = ""; //$NON-NLS-1$

    private static Font normalFont = null;

    private static Font italicFont = null;

    private static Font boldFont = null;

    private static Font boldItalicFont = null;

    /**
     * Constructs a new SimpleHtmlFigure.
     */
    public SimpleHtmlFigure() {
        super();

        setLayoutManager(new ToolbarLayout(ToolbarLayout.VERTICAL));

        if (italicFont == null) {
            normalFont = getUsedFont();
            FontData[] fds = normalFont.getFontData();
            for (int i = 0; i < fds.length; i++) {
                fds[i].setStyle(fds[i].getStyle() | SWT.ITALIC);
            }
            italicFont = new Font(normalFont.getDevice(), fds);

            for (int i = 0; i < fds.length; i++) {
                fds[i].setStyle(fds[i].getStyle() | SWT.BOLD);
            }
            boldItalicFont = new Font(normalFont.getDevice(), fds);

            fds = normalFont.getFontData();
            for (int i = 0; i < fds.length; i++) {
                fds[i].setStyle(fds[i].getStyle() | SWT.BOLD);
            }
            boldFont = new Font(normalFont.getDevice(), fds);
        }
    }

    private void newHorizContainer() {
        horizContainer = new Figure();
        horizContainer.setLayoutManager(new ToolbarLayout(ToolbarLayout.HORIZONTAL));
        add(horizContainer);
    }

    /**
     * Display some HTML text..
     * 
     * @param text Text to rendered.
     */
    @SuppressWarnings("unchecked")
    public void setText(final String text) {

        if (this.text.equals(text)) {
            // if the text is the same, there's nothing to change, so return.
            return;
        }

        // Clean old figures
        List children = getChildren();
        List childrenClone = new ArrayList();
        childrenClone.addAll(children);
        for (Iterator<? extends IFigure> i = childrenClone.iterator(); i.hasNext();) {
            remove(i.next());
        }

        newHorizContainer();

        List<Color> colorStack = new ArrayList<Color>();
        colorStack.add(ColorConstants.black);
        buildFigures(text, SWT.None, colorStack);

        setPreferredSize(computePreferedSize());

        this.text = text;
    }

    private void buildFigures(final String newText, final int fontCode, final List<Color> colorStack) {
        // Optimize
        if (newText == null || newText.length() == 0) {
            return;
        }

        // Find first tag
        int boldIndex = newText.indexOf(TAG_BOLD_BEG);
        int italicIndex = newText.indexOf(TAG_ITALIC_BEG);
        int fontIndex = newText.indexOf(TAG_FONT_BEG_1);
        int brIndex = newText.indexOf(TAG_BR);
        int newFontCode = fontCode;

        if (isFirstIndex(boldIndex, italicIndex, fontIndex, brIndex)) {
            if (boldIndex > 0) {
                String begText = newText.substring(0, boldIndex);
                buildFigures(begText, newFontCode, colorStack);
            }
            newFontCode = newFontCode | SWT.BOLD;

            String endText;
            int endBoldIndex = newText.indexOf(TAG_BOLD_END);
            if (endBoldIndex != -1) {
                String boldText = newText.substring(boldIndex + TAG_BOLD_BEG.length(), endBoldIndex);
                endText = newText.substring(endBoldIndex + TAG_BOLD_END.length());
                buildFigures(boldText, newFontCode, colorStack);
            } else {
                endText = newText.substring(boldIndex + TAG_BOLD_BEG.length());
            }

            newFontCode = newFontCode ^ SWT.BOLD;
            buildFigures(endText, newFontCode, colorStack);
        } else if (isFirstIndex(italicIndex, boldIndex, fontIndex, brIndex)) {
            if (italicIndex > 0) {
                String begText = newText.substring(0, italicIndex);
                buildFigures(begText, newFontCode, colorStack);
            }
            newFontCode = newFontCode | SWT.ITALIC;

            String endText;
            int endItalicIndex = newText.indexOf(TAG_ITALIC_END);
            if (endItalicIndex != -1) {
                String italicText = newText.substring(italicIndex + TAG_ITALIC_BEG.length(), endItalicIndex);
                endText = newText.substring(endItalicIndex + TAG_ITALIC_END.length());
                buildFigures(italicText, newFontCode, colorStack);
            } else {
                endText = newText.substring(italicIndex + TAG_ITALIC_BEG.length());
            }
            newFontCode = newFontCode ^ SWT.ITALIC;

            buildFigures(endText, newFontCode, colorStack);
        } else if (isFirstIndex(fontIndex, boldIndex, italicIndex, brIndex)) {
            if (fontIndex > 0) {
                String begText = newText.substring(0, fontIndex);
                buildFigures(begText, newFontCode, colorStack);
            }
            int colorIndex = newText.indexOf(TAG_FONT_COLOR_BEG_1);

            Color color;
            int colorIndex2 = newText.indexOf(TAG_FONT_BEG_2);
            if (colorIndex2 != -1) {
                String colorCode = newText.substring(colorIndex + TAG_FONT_COLOR_BEG_1.length(), colorIndex2);
                color = getColor(colorCode);
            } else {
                color = colorStack.get(colorStack.size() - 1);
            }
            colorStack.add(color);

            String endText;
            int endColorIndex = newText.indexOf(TAG_FONT_END);
            if (endColorIndex != -1) {
                String colorText = newText.substring(colorIndex2 + TAG_FONT_BEG_2.length(), endColorIndex);
                endText = newText.substring(endColorIndex + TAG_FONT_END.length());
                buildFigures(colorText, newFontCode, colorStack);
            } else {
                endText = newText.substring(colorIndex2 + TAG_FONT_BEG_2.length());
            }

            colorStack.remove(colorStack.size() - 1);
            buildFigures(endText, newFontCode, colorStack);
        } else if (isFirstIndex(brIndex, boldIndex, italicIndex, fontIndex)) {
            if (brIndex > 0) {
                String begText = newText.substring(0, brIndex);
                buildFigures(begText, newFontCode, colorStack);
            }

            newHorizContainer();

            String endText = newText.substring(brIndex + TAG_BR.length());
            buildFigures(endText, newFontCode, colorStack);
        } else {
            Font fontToUse;
            Label label = new Label();
            label.setText(newText);
            if ((fontCode & SWT.BOLD) != 0) {
                if ((fontCode & SWT.ITALIC) != 0) {
                    fontToUse = boldItalicFont;
                } else {
                    fontToUse = boldFont;
                }
            } else {
                if ((fontCode & SWT.ITALIC) != 0) {
                    fontToUse = italicFont;
                } else {
                    fontToUse = normalFont;
                }
            }
            label.setFont(fontToUse);
            label.setForegroundColor(colorStack.get(colorStack.size() - 1));
            horizContainer.add(label);
        }
    }

    private Font getUsedFont() {
        Font font = null;
        IFigure f = this;
        do {
            font = f.getFont();
            f = f.getParent();
        } while (font == null && f != null);

        if (font == null) {
            GC gc = new GC(new Shell());
            font = gc.getFont();
            gc.dispose();
        }
        return font;
    }

    private Color getColor(final String colorCode) {
        if (colorCode.startsWith("#") && (colorCode.length() == 7)) { // hexa code. //$NON-NLS-1$
            Color color;
            try {
                int r = Integer.parseInt(colorCode.substring(1, 3), A_16);
                int g = Integer.parseInt(colorCode.substring(3, A_5), A_16);
                int b = Integer.parseInt(colorCode.substring(A_5, A_7), A_16);
                RGB rgb = new RGB(r, g, b);
                color = new Color(Display.getDefault(), rgb);
            } catch (NumberFormatException nfe) {
                color = ColorConstants.black;
            }
            return color;
        } else { // color name
            String[] colors = { "white", "black", "red", "dark_red", "green", "dark_green", "yellow", "dark_yellow", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
                    "blue", "dark_blue", "magenta", "dark_magenta", "cyan", "dark_cyan", "gray", "dark_gray" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
            int choosedColor = 0;
            for (int i = 0; i < colors.length; i++) {
                if (colors[i].equalsIgnoreCase(colorCode)) {
                    choosedColor = i + 1;
                }
            }
            if (choosedColor == 0) {
                choosedColor = SWT.COLOR_BLACK;
            }
            return Display.getDefault().getSystemColor(choosedColor);
        }
    }

    @SuppressWarnings("unchecked")
    private Dimension computePreferedSize() {
        Dimension size = new Dimension();

        // Vertical path
        List<IFigure> children = getChildren();
        for (IFigure fv : children) {

            // Horizontal path
            Dimension sizeH = new Dimension();
            List<IFigure> childrenH = fv.getChildren();
            for (IFigure fh : childrenH) {
                sizeH.width += fh.getPreferredSize().width;
                sizeH.height = Math.max(sizeH.height, fh.getPreferredSize().height);
            }

            size.width = Math.max(size.width, sizeH.width);
            size.height += sizeH.height;
        }
        return size;
    }

    private static boolean isFirstIndex(final int testedIndex, final int... otherIndex) {
        boolean isFirst = testedIndex != -1;
        for (int i = 0; isFirst && i < otherIndex.length; i++) {
            isFirst = otherIndex[i] == -1 || testedIndex < otherIndex[i];
        }
        return isFirst;
    }

    @Override
    public void paint(Graphics graphics) {

        if (alpha != -1) {
            graphics.setAlpha(alpha);
        } else {
            graphics.setAlpha(255);
        }
        super.paint(graphics);
    }

    public int getAlpha() {
        return this.alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public String getText() {
        return this.text;
    }

}
