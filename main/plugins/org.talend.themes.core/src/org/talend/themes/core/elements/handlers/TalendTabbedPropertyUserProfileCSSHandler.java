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

import org.apache.commons.lang.StringUtils;
import org.eclipse.e4.ui.css.core.dom.properties.ICSSPropertyHandler;
import org.eclipse.e4.ui.css.core.dom.properties.converters.ICSSValueConverter;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.swt.properties.AbstractCSSPropertySWTHandler;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Control;
import org.talend.themes.core.elements.stylesettings.CommonCSSStyleSetting;
import org.talend.themes.core.elements.stylesettings.TalendTabbedPropertyColorHelper;
import org.talend.themes.core.elements.widgets.ITalendTabbedPropertyListWidget;
import org.talend.themes.core.elements.widgets.ITalendTabbedPropertyTitleWidget;
import org.w3c.dom.css.CSSValue;

/**
 * 
 * created by hcyi on Jan 30, 2015 Detailled comment
 *
 */
public class TalendTabbedPropertyUserProfileCSSHandler extends AbstractCSSPropertySWTHandler implements ICSSPropertyHandler {

    private static final String WIDGET_FOREGROUND_COLOR = "color";

    private static final String WIDGET_BACKGROUND_COLOR = "background-color";

    private static final String WIDGET_NORMAL_SHADOW_COLOR = "_t-widget-normal-shadow-color";

    private static final String WIDGET_IS_SHOW_NORMAL_SHADOW = "_t-widget-is-show-normal-shadow";

    private static final String WIDGET_DARK_SHADOW_COLOR = "_t-widget-dark-shadow-color";

    private static final String WIDGET_VERTICAL_LINE_COLOR = "_t-widget-vertical-line-color";

    private static final String LIST_BACKGROUND_COLOR = "_t-list-background-color";

    private static final String BORDER_VISIBLE = "border-visible";

    private static final String TITLE_FOREGROUND_COLOR = "_t-title-foreground-color";

    private static final String TITLE_BACKGROUND_COLOR = "_t-title-background-color";

    private static final String TITLE_BOTTOM_FOREGROUND_KEYLINE1_COLOR = "_t-title-bottom-foreground-keyline1-color";

    private static final String TITLE_BOTTOM_FOREGROUND_KEYLINE2_COLOR = "_t-title-bottom-foreground-keyline2-color";

    @Override
    protected void applyCSSProperty(Control control, String property, CSSValue value, String pseudo, CSSEngine engine)
            throws Exception {
        if (control instanceof ITalendTabbedPropertyListWidget) {
            ITalendTabbedPropertyListWidget userProfileWidget = (ITalendTabbedPropertyListWidget) control;
            TalendTabbedPropertyColorHelper colorHelper = userProfileWidget.getColorHelper();
            if (value != null && CSSValue.CSS_PRIMITIVE_VALUE == value.getCssValueType()) {
                RGB newColor = ((Color) engine.convert(value, Color.class, control.getDisplay())).getRGB();
                if (WIDGET_FOREGROUND_COLOR.equalsIgnoreCase(property)) {
                    colorHelper.setWidgetForeground(CommonCSSStyleSetting.getColorByRGB(newColor));
                }
                if (WIDGET_BACKGROUND_COLOR.equalsIgnoreCase(property)) {
                    colorHelper.setWidgetBackground(CommonCSSStyleSetting.getColorByRGB(newColor));
                }
                if (WIDGET_NORMAL_SHADOW_COLOR.equalsIgnoreCase(property)) {
                    colorHelper.setWidgetNormalShadow(CommonCSSStyleSetting.getColorByRGB(newColor));
                }
                if (WIDGET_DARK_SHADOW_COLOR.equalsIgnoreCase(property)) {
                    colorHelper.setWidgetDarkShadow(CommonCSSStyleSetting.getColorByRGB(newColor));
                }
                if (WIDGET_VERTICAL_LINE_COLOR.equalsIgnoreCase(property)) {
                    colorHelper.setWidgetVerticalLineColor(CommonCSSStyleSetting.getColorByRGB(newColor));
                }
                if (LIST_BACKGROUND_COLOR.equalsIgnoreCase(property)) {
                    colorHelper.setListBackground(CommonCSSStyleSetting.getColorByRGB(newColor));
                }
                if (BORDER_VISIBLE.equalsIgnoreCase(property)) {
                    String isVisibleBorderValue = value.getCssText();
                    if (StringUtils.isNotEmpty(isVisibleBorderValue)) {
                        boolean isVisibleBorder = Boolean.parseBoolean(isVisibleBorderValue);
                        if (colorHelper.isVisibleBorder() != isVisibleBorder) {
                            colorHelper.setVisibleBorder(isVisibleBorder);
                        }
                    }
                }
                if (WIDGET_IS_SHOW_NORMAL_SHADOW.equalsIgnoreCase(property)) {
                    String isShowNormalShadowValue = value.getCssText();
                    if (StringUtils.isNotEmpty(isShowNormalShadowValue)) {
                        boolean isShowNormalShadow = Boolean.parseBoolean(isShowNormalShadowValue);
                        if (colorHelper.isShowNormalShadow() != isShowNormalShadow) {
                            colorHelper.setShowNormalShadow(isShowNormalShadow);
                        }
                    }
                }
            }
        }

        if (control instanceof ITalendTabbedPropertyTitleWidget) {
            ITalendTabbedPropertyTitleWidget tabbedPropertyTitle = (ITalendTabbedPropertyTitleWidget) control;
            TalendTabbedPropertyColorHelper colorHelper = tabbedPropertyTitle.getColorHelper();
            if (value != null && CSSValue.CSS_PRIMITIVE_VALUE == value.getCssValueType()) {
                RGB newColor = ((Color) engine.convert(value, Color.class, control.getDisplay())).getRGB();
                if (TITLE_FOREGROUND_COLOR.equalsIgnoreCase(property)) {
                    colorHelper.setTitleForeground(CommonCSSStyleSetting.getColorByRGB(newColor));
                }
                if (TITLE_BACKGROUND_COLOR.equalsIgnoreCase(property)) {
                    colorHelper.setTitleBackground(CommonCSSStyleSetting.getColorByRGB(newColor));
                }
                if (TITLE_BOTTOM_FOREGROUND_KEYLINE1_COLOR.equalsIgnoreCase(property)) {
                    colorHelper.setTitleBottomForegroundKeyline1(CommonCSSStyleSetting.getColorByRGB(newColor));
                }
                if (TITLE_BOTTOM_FOREGROUND_KEYLINE2_COLOR.equalsIgnoreCase(property)) {
                    colorHelper.setTitleBottomForegroundKeyline2(CommonCSSStyleSetting.getColorByRGB(newColor));
                }
                if (BORDER_VISIBLE.equalsIgnoreCase(property)) {
                    String isVisibleBorderValue = value.getCssText();
                    if (isVisibleBorderValue != null) {
                        boolean isVisibleBorder = Boolean.parseBoolean(isVisibleBorderValue);
                        if (!isVisibleBorder) {
                            colorHelper.setVisibleBorder(isVisibleBorder);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected String retrieveCSSProperty(Control control, String property, String pseudo, CSSEngine engine) throws Exception {
        if (control instanceof ITalendTabbedPropertyListWidget) {
            ITalendTabbedPropertyListWidget userProfileWidget = (ITalendTabbedPropertyListWidget) control;
            TalendTabbedPropertyColorHelper colorHelper = userProfileWidget.getColorHelper();
            ICSSValueConverter cssValueConverter = engine.getCSSValueConverter(String.class);
            if (WIDGET_FOREGROUND_COLOR.equalsIgnoreCase(property)) {
                return cssValueConverter.convert(colorHelper.getWidgetForeground(), engine, null);
            }
            if (WIDGET_BACKGROUND_COLOR.equalsIgnoreCase(property)) {
                return cssValueConverter.convert(colorHelper.getWidgetBackground(), engine, null);
            }
            if (WIDGET_NORMAL_SHADOW_COLOR.equalsIgnoreCase(property)) {
                return cssValueConverter.convert(colorHelper.getWidgetNormalShadow(), engine, null);
            }
            if (LIST_BACKGROUND_COLOR.equalsIgnoreCase(property)) {
                return cssValueConverter.convert(colorHelper.getListBackground(), engine, null);
            }
        }
        return null;
    }
}
