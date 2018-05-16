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
package org.talend.themes.core.elements.handlers;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.e4.ui.css.core.dom.properties.ICSSPropertyHandler;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.talend.themes.core.elements.adapters.TalendPaletteElement;
import org.talend.themes.core.elements.stylesettings.CommonCSSStyleSetting;
import org.talend.themes.core.elements.stylesettings.TalendPaletteCSSStyleSetting;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.CSSValueList;

/**
 * created by cmeng on Jan 30, 2015 Detailled comment
 *
 */
public class TalendPaletteCSSPropertyHandler implements ICSSPropertyHandler {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.e4.ui.css.core.dom.properties.ICSSPropertyHandler#applyCSSProperty(java.lang.Object,
     * java.lang.String, org.w3c.dom.css.CSSValue, java.lang.String, org.eclipse.e4.ui.css.core.engine.CSSEngine)
     */
    @Override
    public boolean applyCSSProperty(Object element, String property, CSSValue value, String pseudo, CSSEngine engine)
            throws Exception {

        TalendPaletteElement paletteElement = (TalendPaletteElement) element;
        TalendPaletteCSSStyleSetting paletteSetting = paletteElement.getCSSStyleSetting();

        Composite paletteComposite = (Composite) paletteElement.getNativeWidget();

        boolean changed = false;
        if ("tPalette-mouseOver-forground-color1".equalsIgnoreCase(property)) {
            RGB rgb = ((Color) engine.convert(value, Color.class, paletteComposite.getDisplay())).getRGB();
            Color oldColor = paletteSetting.getMouseOverForgroundColor1();
            if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                changed = true;
                paletteSetting.setMouseOverForgroundColor1(CommonCSSStyleSetting.getColorByRGB(rgb));
            }
        } else if ("tPalette-mouseOver-forground-color2".equalsIgnoreCase(property)) {
            RGB rgb = ((Color) engine.convert(value, Color.class, paletteComposite.getDisplay())).getRGB();
            Color oldColor = paletteSetting.getMouseOverForgroundColor2();
            if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                changed = true;
                paletteSetting.setMouseOverForgroundColor2(CommonCSSStyleSetting.getColorByRGB(rgb));
            }
        } else if ("tPalette-mouseOver-forground-color3".equalsIgnoreCase(property)) {
            RGB rgb = ((Color) engine.convert(value, Color.class, paletteComposite.getDisplay())).getRGB();
            Color oldColor = paletteSetting.getMouseOverForgroundColor3();
            if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                changed = true;
                paletteSetting.setMouseOverForgroundColor3(CommonCSSStyleSetting.getColorByRGB(rgb));
            }
        } else if ("tPalette-mouseOver-background-color1".equalsIgnoreCase(property)) {
            RGB rgb = ((Color) engine.convert(value, Color.class, paletteComposite.getDisplay())).getRGB();
            Color oldColor = paletteSetting.getMouseOverBackgroundColor1();
            if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                changed = true;
                paletteSetting.setMouseOverBackgroundColor1(CommonCSSStyleSetting.getColorByRGB(rgb));
            }
        } else if ("tPalette-mouseOver-background-color2".equalsIgnoreCase(property)) {
            RGB rgb = ((Color) engine.convert(value, Color.class, paletteComposite.getDisplay())).getRGB();
            Color oldColor = paletteSetting.getMouseOverBackgroundColor2();
            if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                changed = true;
                paletteSetting.setMouseOverBackgroundColor2(CommonCSSStyleSetting.getColorByRGB(rgb));
            }
        } else if ("tPalette-mouseOver-background-color3".equalsIgnoreCase(property)) {
            RGB rgb = ((Color) engine.convert(value, Color.class, paletteComposite.getDisplay())).getRGB();
            Color oldColor = paletteSetting.getMouseOverBackgroundColor3();
            if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                changed = true;
                paletteSetting.setMouseOverBackgroundColor3(CommonCSSStyleSetting.getColorByRGB(rgb));
            }
        } else if ("tPalette-expanded-background-color".equalsIgnoreCase(property)) {
            RGB rgb = ((Color) engine.convert(value, Color.class, paletteComposite.getDisplay())).getRGB();
            Color oldColor = paletteSetting.getExpandedBackgroundColor();
            if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                changed = true;
                paletteSetting.setExpandedBackgroundColor(CommonCSSStyleSetting.getColorByRGB(rgb));
            }
        } else if ("tPalette-collapsed-forground-color".equalsIgnoreCase(property)) {
            RGB rgb = ((Color) engine.convert(value, Color.class, paletteComposite.getDisplay())).getRGB();
            Color oldColor = paletteSetting.getCollapsedForgroundColor();
            if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                changed = true;
                paletteSetting.setCollapsedForgroundColor(CommonCSSStyleSetting.getColorByRGB(rgb));
            }
        } else if ("tPalette-collapsed-background-color".equalsIgnoreCase(property)) {
            RGB rgb = ((Color) engine.convert(value, Color.class, paletteComposite.getDisplay())).getRGB();
            Color oldColor = paletteSetting.getCollapsedBackgroundColor();
            if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                changed = true;
                paletteSetting.setCollapsedBackgroundColor(CommonCSSStyleSetting.getColorByRGB(rgb));
            }
        } else if ("tPalette-list-background-color".equalsIgnoreCase(property)) {
            RGB rgb = ((Color) engine.convert(value, Color.class, paletteComposite.getDisplay())).getRGB();
            Color oldColor = paletteSetting.getCollapsedBackgroundColor();
            if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                changed = true;
                paletteSetting.setCollapsedBackgroundColor(CommonCSSStyleSetting.getColorByRGB(rgb));
            }
        } else if ("tPalette-color-increment".equalsIgnoreCase(property)) {
            Integer colorIncrement = null;
            try {
                colorIncrement = (int) ((CSSPrimitiveValue) value).getFloatValue(CSSPrimitiveValue.CSS_PX);
            } catch (Exception e) {
                // nothing to do
            }
            if (colorIncrement != null && colorIncrement != paletteSetting.getColorIncrement()) {
                changed = true;
                paletteSetting.setColorIncrement(colorIncrement);
            }
        } else if ("tPalette-x-offset".equalsIgnoreCase(property)) {
            Integer xOffset = null;
            try {
                xOffset = (int) ((CSSPrimitiveValue) value).getFloatValue(CSSPrimitiveValue.CSS_PX);
            } catch (Exception e) {
                // nothing to do
            }
            if (xOffset != null && xOffset != paletteSetting.getxOffset()) {
                changed = true;
                paletteSetting.setxOffset(xOffset);
            }
        } else if ("tPalette-title-margin-border".equalsIgnoreCase(property)) {
            if (value.getCssValueType() == CSSValue.CSS_VALUE_LIST) {
                CSSValueList valueList = (CSSValueList) value;
                boolean succeed = true;
                int top = 0;
                int left = 0;
                int bottom = 0;
                int right = 0;
                try {
                    top = (int) ((CSSPrimitiveValue) valueList.item(0)).getFloatValue(CSSPrimitiveValue.CSS_PX);
                    left = (int) ((CSSPrimitiveValue) valueList.item(1)).getFloatValue(CSSPrimitiveValue.CSS_PX);
                    bottom = (int) ((CSSPrimitiveValue) valueList.item(2)).getFloatValue(CSSPrimitiveValue.CSS_PX);
                    right = (int) ((CSSPrimitiveValue) valueList.item(3)).getFloatValue(CSSPrimitiveValue.CSS_PX);
                } catch (Exception e) {
                    succeed = false;
                }
                Border oldBorder = paletteSetting.getTitleMarginBorder();
                Insets insets = null;
                if (oldBorder != null) {
                    insets = oldBorder.getInsets(null);
                }
                if (succeed
                        && (insets == null || !(top == insets.top && left == insets.left && bottom == insets.bottom && right == insets.right))) {
                    changed = true;
                    MarginBorder border = new MarginBorder(top, left, bottom, right);
                    paletteSetting.setTitleMarginBorder(border);
                }
            }
        } else if ("tPalette-scroll-pane-border".equalsIgnoreCase(property)) {
            if (value.getCssValueType() == CSSValue.CSS_VALUE_LIST) {
                CSSValueList valueList = (CSSValueList) value;
                boolean succeed = true;
                int top = 0;
                int left = 0;
                int bottom = 0;
                int right = 0;
                try {
                    top = (int) ((CSSPrimitiveValue) valueList.item(0)).getFloatValue(CSSPrimitiveValue.CSS_PX);
                    left = (int) ((CSSPrimitiveValue) valueList.item(1)).getFloatValue(CSSPrimitiveValue.CSS_PX);
                    bottom = (int) ((CSSPrimitiveValue) valueList.item(2)).getFloatValue(CSSPrimitiveValue.CSS_PX);
                    right = (int) ((CSSPrimitiveValue) valueList.item(3)).getFloatValue(CSSPrimitiveValue.CSS_PX);
                } catch (Exception e) {
                    succeed = false;
                }
                Border oldBorder = paletteSetting.getScrollPaneBorder();
                Insets insets = null;
                if (oldBorder != null) {
                    insets = oldBorder.getInsets(null);
                }
                if (succeed
                        && (insets == null || !(top == insets.top && left == insets.left && bottom == insets.bottom && right == insets.right))) {
                    changed = true;
                    MarginBorder border = new MarginBorder(top, left, bottom, right);
                    paletteSetting.setScrollPaneBorder(border);
                }
            }
        } else if ("tPalette-scroll-pane-list-border".equalsIgnoreCase(property)) {
            if (value.getCssValueType() == CSSValue.CSS_VALUE_LIST) {
                CSSValueList valueList = (CSSValueList) value;
                boolean succeed = true;
                int top = 0;
                int left = 0;
                int bottom = 0;
                int right = 0;
                try {
                    top = (int) ((CSSPrimitiveValue) valueList.item(0)).getFloatValue(CSSPrimitiveValue.CSS_PX);
                    left = (int) ((CSSPrimitiveValue) valueList.item(1)).getFloatValue(CSSPrimitiveValue.CSS_PX);
                    bottom = (int) ((CSSPrimitiveValue) valueList.item(2)).getFloatValue(CSSPrimitiveValue.CSS_PX);
                    right = (int) ((CSSPrimitiveValue) valueList.item(3)).getFloatValue(CSSPrimitiveValue.CSS_PX);
                } catch (Exception e) {
                    succeed = false;
                }
                Border oldBorder = paletteSetting.getScrollPaneListBorder();
                Insets insets = null;
                if (oldBorder != null) {
                    insets = oldBorder.getInsets(null);
                }
                if (succeed
                        && (insets == null || !(top == insets.top && left == insets.left && bottom == insets.bottom && right == insets.right))) {
                    changed = true;
                    MarginBorder border = new MarginBorder(top, left, bottom, right);
                    paletteSetting.setScrollPaneListBorder(border);
                }
            }
        } else if ("tPalette-slider-palette-forground-color".equalsIgnoreCase(property)) {
            RGB rgb = ((Color) engine.convert(value, Color.class, paletteComposite.getDisplay())).getRGB();
            Color oldColor = paletteSetting.getSliderPaletteForgroundColor();
            if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                changed = true;
                paletteSetting.setSliderPaletteForgroundColor(CommonCSSStyleSetting.getColorByRGB(rgb));
            }
        } else if ("tPalette-slider-palette-background-color".equalsIgnoreCase(property)) {
            RGB rgb = ((Color) engine.convert(value, Color.class, paletteComposite.getDisplay())).getRGB();
            Color oldColor = paletteSetting.getSliderPaletteBackgroundColor();
            if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                changed = true;
                paletteSetting.setSliderPaletteBackgroundColor(CommonCSSStyleSetting.getColorByRGB(rgb));
            }
        } else if ("tPalette-show-folder-image".equalsIgnoreCase(property)) {
            Boolean showFolderImage = null;
            try {
                showFolderImage = (Boolean) engine.convert(value, Boolean.class, paletteComposite.getDisplay());
            } catch (Exception e) {
                // nothing to do
            }
            if (showFolderImage != null && paletteSetting.isShowFolderImage() != showFolderImage) {
                changed = true;
                paletteSetting.setShowFolderImage(showFolderImage);
            }
        } else if ("tPalette-entryEditPart-arrowWidth".equalsIgnoreCase(property)) {
            Integer arrowWidth = null;
            try {
                arrowWidth = (Integer) engine.convert(value, Integer.class, paletteComposite.getDisplay());
            } catch (Exception e) {
                // nothing to do
            }
            if (arrowWidth != null && paletteSetting.getEntryEditPartArrowWidth() != arrowWidth) {
                changed = true;
                paletteSetting.setEntryEditPartArrowWidth(arrowWidth);
            }
        } else if ("tPalette-entryEditPart-listBorder".equalsIgnoreCase(property)) {
            if (value.getCssValueType() == CSSValue.CSS_VALUE_LIST) {
                CSSValueList valueList = (CSSValueList) value;
                boolean succeed = true;
                int top = 0;
                int left = 0;
                int bottom = 0;
                int right = 0;
                try {
                    top = (int) ((CSSPrimitiveValue) valueList.item(0)).getFloatValue(CSSPrimitiveValue.CSS_PX);
                    left = (int) ((CSSPrimitiveValue) valueList.item(1)).getFloatValue(CSSPrimitiveValue.CSS_PX);
                    bottom = (int) ((CSSPrimitiveValue) valueList.item(2)).getFloatValue(CSSPrimitiveValue.CSS_PX);
                    right = (int) ((CSSPrimitiveValue) valueList.item(3)).getFloatValue(CSSPrimitiveValue.CSS_PX);
                } catch (Exception e) {
                    succeed = false;
                }
                Border oldBorder = paletteSetting.getEntryEditPartListBorder();
                Insets insets = null;
                if (oldBorder != null) {
                    insets = oldBorder.getInsets(null);
                }
                if (succeed
                        && (insets == null || !(top == insets.top && left == insets.left && bottom == insets.bottom && right == insets.right))) {
                    changed = true;
                    MarginBorder border = new MarginBorder(top, left, bottom, right);
                    paletteSetting.setEntryEditPartListBorder(border);
                }
            }
        } else if ("tPalette-entryEditPart-iconBorder".equalsIgnoreCase(property)) {
            if (value.getCssValueType() == CSSValue.CSS_VALUE_LIST) {
                CSSValueList valueList = (CSSValueList) value;
                boolean succeed = true;
                int top = 0;
                int left = 0;
                int bottom = 0;
                int right = 0;
                try {
                    top = (int) ((CSSPrimitiveValue) valueList.item(0)).getFloatValue(CSSPrimitiveValue.CSS_PX);
                    left = (int) ((CSSPrimitiveValue) valueList.item(1)).getFloatValue(CSSPrimitiveValue.CSS_PX);
                    bottom = (int) ((CSSPrimitiveValue) valueList.item(2)).getFloatValue(CSSPrimitiveValue.CSS_PX);
                    right = (int) ((CSSPrimitiveValue) valueList.item(3)).getFloatValue(CSSPrimitiveValue.CSS_PX);
                } catch (Exception e) {
                    succeed = false;
                }
                Border oldBorder = paletteSetting.getEntryEditPartIconBorder();
                Insets insets = null;
                if (oldBorder != null) {
                    insets = oldBorder.getInsets(null);
                }
                if (succeed
                        && (insets == null || !(top == insets.top && left == insets.left && bottom == insets.bottom && right == insets.right))) {
                    changed = true;
                    MarginBorder border = new MarginBorder(top, left, bottom, right);
                    paletteSetting.setEntryEditPartIconBorder(border);
                }
            }
        } else if ("tPalette-entryEditPart-forground-disableColor".equalsIgnoreCase(property)) {
            RGB rgb = ((Color) engine.convert(value, Color.class, paletteComposite.getDisplay())).getRGB();
            Color oldColor = paletteSetting.getEntryEditPartToolEntryForgroundDisabledColor();
            if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                changed = true;
                paletteSetting.setEntryEditPartToolEntryForgroundDisabledColor(CommonCSSStyleSetting.getColorByRGB(rgb));
            }
        } else if ("tPalette-entryEditPart-selected-background-color".equalsIgnoreCase(property)) {
            RGB rgb = ((Color) engine.convert(value, Color.class, paletteComposite.getDisplay())).getRGB();
            Color oldColor = paletteSetting.getEntryEditPartToolEntrySelectedBackgroundColor();
            if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                changed = true;
                paletteSetting.setEntryEditPartToolEntrySelectedBackgroundColor(CommonCSSStyleSetting.getColorByRGB(rgb));
            }
        } else if ("tPalette-entryEditPart-hover-background-color".equalsIgnoreCase(property)) {
            RGB rgb = ((Color) engine.convert(value, Color.class, paletteComposite.getDisplay())).getRGB();
            Color oldColor = paletteSetting.getEntryEditPartToolEntryHoverBackgroundColor();
            if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                changed = true;
                paletteSetting.setEntryEditPartToolEntryHoverBackgroundColor(CommonCSSStyleSetting.getColorByRGB(rgb));
            }
        } else if ("tPalette-entryEditPart-borderFocus-forground-color".equalsIgnoreCase(property)) {
            RGB rgb = ((Color) engine.convert(value, Color.class, paletteComposite.getDisplay())).getRGB();
            Color oldColor = paletteSetting.getEntryEditPartToolEntryBorderFocusForgroundColor();
            if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                changed = true;
                paletteSetting.setEntryEditPartToolEntryBorderFocusForgroundColor(CommonCSSStyleSetting.getColorByRGB(rgb));
            }
        } else if ("tPalette-entryEditPart-borderFocus-background-color".equalsIgnoreCase(property)) {
            RGB rgb = ((Color) engine.convert(value, Color.class, paletteComposite.getDisplay())).getRGB();
            Color oldColor = paletteSetting.getEntryEditPartToolEntryBorderFocusBackgroundColor();
            if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                changed = true;
                paletteSetting.setEntryEditPartToolEntryBorderFocusBackgroundColor(CommonCSSStyleSetting.getColorByRGB(rgb));
            }
        } else if ("tPalette-drawerFigure-baseColor".equalsIgnoreCase(property)) {
            RGB rgb = ((Color) engine.convert(value, Color.class, paletteComposite.getDisplay())).getRGB();
            Color oldColor = paletteSetting.getDrawerFigureBaseColor();
            if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                changed = true;
                paletteSetting.setDrawerFigureBaseColor(CommonCSSStyleSetting.getColorByRGB(rgb));
            }
        } else if ("tPalette-collapse-topBorder-forground-lineColor1".equalsIgnoreCase(property)) {
            RGB rgb = ((Color) engine.convert(value, Color.class, paletteComposite.getDisplay())).getRGB();
            Color oldColor = paletteSetting.getCollapseTopBorderForgroundLineColor1();
            if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                changed = true;
                paletteSetting.setCollapseTopBorderForgroundLineColor1(CommonCSSStyleSetting.getColorByRGB(rgb));
            }
        } else if ("tPalette-collapse-topBorder-forground-lineColor2".equalsIgnoreCase(property)) {
            RGB rgb = ((Color) engine.convert(value, Color.class, paletteComposite.getDisplay())).getRGB();
            Color oldColor = paletteSetting.getCollapseTopBorderForgroundLineColor2();
            if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                changed = true;
                paletteSetting.setCollapseTopBorderForgroundLineColor2(CommonCSSStyleSetting.getColorByRGB(rgb));
            }
        } else if ("tPalette-collapse-expanded-forground-lineColor".equalsIgnoreCase(property)) {
            RGB rgb = ((Color) engine.convert(value, Color.class, paletteComposite.getDisplay())).getRGB();
            Color oldColor = paletteSetting.getCollapseExpandedLineForgroundColor();
            if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                changed = true;
                paletteSetting.setCollapseExpandedLineForgroundColor(CommonCSSStyleSetting.getColorByRGB(rgb));
            }
        } else if ("tPalette-collapse-notExpanded-forground-lineColor".equalsIgnoreCase(property)) {
            RGB rgb = ((Color) engine.convert(value, Color.class, paletteComposite.getDisplay())).getRGB();
            Color oldColor = paletteSetting.getCollapseNotExpandedLineForgroundColor();
            if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                changed = true;
                paletteSetting.setCollapseNotExpandedLineForgroundColor(CommonCSSStyleSetting.getColorByRGB(rgb));
            }
        } else if ("tPalette-entryEditPart-entry-topLine-color".equalsIgnoreCase(property)) {
            RGB rgb = ((Color) engine.convert(value, Color.class, paletteComposite.getDisplay())).getRGB();
            Color oldColor = paletteSetting.getEntryEditPartToolEntryTopLineColor();
            if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                changed = true;
                paletteSetting.setEntryEditPartToolEntryTopLineColor(CommonCSSStyleSetting.getColorByRGB(rgb));
            }
        } else if ("tPalette-entryEditPart-entry-bottomLine-color".equalsIgnoreCase(property)) {
            RGB rgb = ((Color) engine.convert(value, Color.class, paletteComposite.getDisplay())).getRGB();
            Color oldColor = paletteSetting.getEntryEditPartToolEntryBottomLineColor();
            if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                changed = true;
                paletteSetting.setEntryEditPartToolEntryBottomLineColor(CommonCSSStyleSetting.getColorByRGB(rgb));
            }
        } else if ("tPalette-entryEditPart-entry-background-color".equalsIgnoreCase(property)) {
            RGB rgb = ((Color) engine.convert(value, Color.class, paletteComposite.getDisplay())).getRGB();
            Color oldColor = paletteSetting.getEntryEditPartToolEntryBackgroundColor();
            if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                changed = true;
                paletteSetting.setEntryEditPartToolEntryBackgroundColor(CommonCSSStyleSetting.getColorByRGB(rgb));
            }
        } else if ("tPalette-entryEditPart-entry-color-inheritFromParent".equalsIgnoreCase(property)) {
            Boolean inheritFromParent = null;
            try {
                inheritFromParent = (Boolean) engine.convert(value, Boolean.class, paletteComposite.getDisplay());
            } catch (Exception e) {
                // nothing to do
            }
            if (inheritFromParent != null
                    && paletteSetting.isEntryEditPartBackgroundColorInheritFromParent() != inheritFromParent) {
                changed = true;
                paletteSetting.setEntryEditPartBackgroundColorInheritFromParent(inheritFromParent);
            }
        } else if ("tPalette-searchButton-background-color".equalsIgnoreCase(property)) {
            Object objColor = engine.convert(value, Color.class, paletteComposite.getDisplay());
            if (objColor instanceof Color) {
                RGB rgb = ((Color) objColor).getRGB();
                Color oldColor = paletteSetting.getSearchButtonBackgroundColor();
                if (oldColor == null || !oldColor.getRGB().equals(rgb)) {
                    changed = true;
                    paletteSetting.setSearchButtonBackgroundColor(CommonCSSStyleSetting.getColorByRGB(rgb));
                }
            }
        } else if ("tPalette-searchButton-image".equalsIgnoreCase(property)) {
            Image image = null;
            try {
                image = (Image) engine.convert(value, Image.class, paletteComposite.getDisplay());
            } catch (Exception e) {
                // nothing to do
            }
            if (image != null && paletteSetting.getSearchButtonImage() != image) {
                changed = true;
                paletteSetting.setSearchButtonImage(image);
            }
        }

        if (changed) {
            paletteSetting.updateTimeStamp();
        }
        return changed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.e4.ui.css.core.dom.properties.ICSSPropertyHandler#retrieveCSSProperty(java.lang.Object,
     * java.lang.String, java.lang.String, org.eclipse.e4.ui.css.core.engine.CSSEngine)
     */
    @Override
    public String retrieveCSSProperty(Object element, String property, String pseudo, CSSEngine engine) throws Exception {
        return null;
    }

}
