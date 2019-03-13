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
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.swt.dom.CTabFolderElement;
import org.eclipse.e4.ui.css.swt.helpers.CSSSWTColorHelper;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolderRenderer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.talend.themes.core.elements.renderers.TalendTabRenderer;
import org.talend.themes.core.elements.stylesettings.TalendTabSettings;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.CSSValueList;

import net.jeeeyul.swtend.ui.HSB;

/**
 * created by cmeng on Feb 9, 2015 Detailled comment
 *
 */
public class TalendTabCSSPropertyHandler implements ICSSPropertyHandler {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.e4.ui.css.core.dom.properties.ICSSPropertyHandler#applyCSSProperty(java.lang.Object,
     * java.lang.String, org.w3c.dom.css.CSSValue, java.lang.String, org.eclipse.e4.ui.css.core.engine.CSSEngine)
     */
    @Override
    public boolean applyCSSProperty(Object element, String property, CSSValue value, String pseudo, CSSEngine engine)
            throws Exception {
        CTabFolderElement tabFolderElement = ((CTabFolderElement) element);
        Object _nativeWidget = tabFolderElement.getNativeWidget();
        CTabFolder tabFolder = ((CTabFolder) _nativeWidget);
        CTabFolderRenderer _renderer = tabFolder.getRenderer();
        if ((!(_renderer instanceof TalendTabRenderer))) {
            return false;
        }
        CTabFolderRenderer _renderer_1 = tabFolder.getRenderer();
        TalendTabRenderer renderer = ((TalendTabRenderer) _renderer_1);
        TalendTabSettings settings = renderer.getSettings();
        boolean _switchResult = false;
        boolean _matched = false;
        if (!_matched) {
            if ("tab-minimize-button-forground-color".equals(property)) { //$NON-NLS-1$
                _matched = true;
                boolean _xblockexpression_48 = false;
                {
                    RGB rgb = CSSSWTColorHelper.getRGBA(value).rgb;
                    if (rgb != null) {
                        HSB hsb = new HSB(rgb.red, rgb.green, rgb.blue);
                        settings.setMinimizeButtonForgroundColor(hsb);
                    } else {
                        settings.setMinimizeButtonForgroundColor(null);
                    }
                    _xblockexpression_48 = true;
                }
                _switchResult = _xblockexpression_48;
            }
        }
        if (!_matched) {
            if ("tab-minimize-button-background-color".equals(property)) { //$NON-NLS-1$
                _matched = true;
                boolean _xblockexpression_49 = false;
                {
                    RGB rgb = CSSSWTColorHelper.getRGBA(value).rgb;
                    if (rgb != null) {
                        HSB hsb = new HSB(rgb.red, rgb.green, rgb.blue);
                        settings.setMinimizeButtonBackgroundColor(hsb);
                    } else {
                        settings.setMinimizeButtonBackgroundColor(null);
                    }
                    _xblockexpression_49 = true;
                }
                _switchResult = _xblockexpression_49;
            }
        }
        if (!_matched) {
            if ("tab-maximize-button-forground-color".equals(property)) { //$NON-NLS-1$
                _matched = true;
                boolean _xblockexpression_50 = false;
                {
                    RGB rgb = CSSSWTColorHelper.getRGBA(value).rgb;
                    if (rgb != null) {
                        HSB hsb = new HSB(rgb.red, rgb.green, rgb.blue);
                        settings.setMaximizeButtonForgroundColor(hsb);
                    } else {
                        settings.setMaximizeButtonForgroundColor(null);
                    }
                    _xblockexpression_50 = true;
                }
                _switchResult = _xblockexpression_50;
            }
        }
        if (!_matched) {
            if ("tab-maximize-button-background-color".equals(property)) { //$NON-NLS-1$
                _matched = true;
                boolean _xblockexpression_51 = false;
                {
                    RGB rgb = CSSSWTColorHelper.getRGBA(value).rgb;
                    if (rgb != null) {
                        HSB hsb = new HSB(rgb.red, rgb.green, rgb.blue);
                        settings.setMaximizeButtonBackgroundColor(hsb);
                    } else {
                        settings.setMaximizeButtonBackgroundColor(null);
                    }
                    _xblockexpression_51 = true;
                }
                _switchResult = _xblockexpression_51;
            }
        }
        if (!_matched) {
            if ("tab-minimize-button-none-image".equals(property)) { //$NON-NLS-1$
                _matched = true;
                boolean _xblockexpression_52 = false;
                {
                    Display _display = tabFolder.getDisplay();
                    Object _convert = engine.convert(value, Image.class, _display);
                    Image image = ((Image) _convert);
                    if (image != null) {
                        settings.setMinimizeButtonNoneStatueImage(image);
                    } else {
                        image = null;
                    }
                    _xblockexpression_52 = true;
                }
                _switchResult = _xblockexpression_52;
            }
        }
        if (!_matched) {
            if ("tab-minimize-button-hot-image".equals(property)) { //$NON-NLS-1$
                _matched = true;
                boolean _xblockexpression_53 = false;
                {
                    Display _display = tabFolder.getDisplay();
                    Object _convert = engine.convert(value, Image.class, _display);
                    Image image = ((Image) _convert);
                    if (image != null) {
                        settings.setMinimizeButtonHotStatueImage(image);
                    } else {
                        image = null;
                    }
                    _xblockexpression_53 = true;
                }
                _switchResult = _xblockexpression_53;
            }
        }
        if (!_matched) {
            if ("tab-minimize-button-selected-image".equals(property)) { //$NON-NLS-1$
                _matched = true;
                boolean _xblockexpression_54 = false;
                {
                    Display _display = tabFolder.getDisplay();
                    Object _convert = engine.convert(value, Image.class, _display);
                    Image image = ((Image) _convert);
                    if (image != null) {
                        settings.setMinimizeButtonSelectedStatueImage(image);
                    } else {
                        image = null;
                    }
                    _xblockexpression_54 = true;
                }
                _switchResult = _xblockexpression_54;
            }
        }
        if (!_matched) {
            if ("tab-maximize-button-none-image".equals(property)) { //$NON-NLS-1$
                _matched = true;
                boolean _xblockexpression_55 = false;
                {
                    Display _display = tabFolder.getDisplay();
                    Object _convert = engine.convert(value, Image.class, _display);
                    Image image = ((Image) _convert);
                    if (image != null) {
                        settings.setMaximizeButtonNoneStatueImage(image);
                    } else {
                        image = null;
                    }
                    _xblockexpression_55 = true;
                }
                _switchResult = _xblockexpression_55;
            }
        }
        if (!_matched) {
            if ("tab-maximize-button-hot-image".equals(property)) { //$NON-NLS-1$
                _matched = true;
                boolean _xblockexpression_56 = false;
                {
                    Display _display = tabFolder.getDisplay();
                    Object _convert = engine.convert(value, Image.class, _display);
                    Image image = ((Image) _convert);
                    if (image != null) {
                        settings.setMaximizeButtonHotStatueImage(image);
                    } else {
                        image = null;
                    }
                    _xblockexpression_56 = true;
                }
                _switchResult = _xblockexpression_56;
            }
        }
        if (!_matched) {
            if ("tab-maximize-button-selected-image".equals(property)) { //$NON-NLS-1$
                _matched = true;
                boolean _xblockexpression_57 = false;
                {
                    Display _display = tabFolder.getDisplay();
                    Object _convert = engine.convert(value, Image.class, _display);
                    Image image = ((Image) _convert);
                    if (image != null) {
                        settings.setMaximizeButtonSelectedStatueImage(image);
                    } else {
                        image = null;
                    }
                    _xblockexpression_57 = true;
                }
                _switchResult = _xblockexpression_57;
            }
        }
        if (!_matched) {
            if ("tab-item-images".equals(property)) { //$NON-NLS-1$
                _matched = true;
                boolean _xblockexpression_58 = false;
                Display _display = tabFolder.getDisplay();
                if (value instanceof CSSValueList) {
                    CSSValueList cssValueList = (CSSValueList) value;
                    int length = cssValueList.getLength() - 1;
                    int i = 0;
                    while (i < length) {
                        String elementId = cssValueList.item(i++).getCssText();
                        CSSValue cssValue = cssValueList.item(i++);
                        if (cssValue == null) {
                            break;
                        }
                        Object imageObj = engine.convert(cssValue, Image.class, _display);
                        if (StringUtils.isNotEmpty(elementId) && imageObj != null) {
                            settings.addCTabItemImage(elementId, (Image) imageObj);
                        }
                    }
                    _xblockexpression_58 = true;
                }

                _switchResult = _xblockexpression_58;
            }
        }
        if (!_matched) {
            _switchResult = false;
        }
        boolean applied = _switchResult;
        return applied;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.e4.ui.css.core.dom.properties.ICSSPropertyHandler#retrieveCSSProperty(java.lang.Object,
     * java.lang.String, java.lang.String, org.eclipse.e4.ui.css.core.engine.CSSEngine)
     */
    @Override
    public String retrieveCSSProperty(Object element, String property, String pseudo, CSSEngine engine) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

}
