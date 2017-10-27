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
package org.talend.themes.core.elements.adapters;

import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.swt.dom.CompositeElement;
import org.eclipse.swt.widgets.Composite;
import org.talend.themes.core.elements.stylesettings.TalendPaletteCSSStyleSetting;
import org.talend.themes.core.elements.widgets.ITalendPaletteWidget;

/**
 * created by cmeng on Jan 30, 2015 Detailled comment
 *
 */
public class TalendPaletteElement extends CompositeElement {

    /**
     * DOC cmeng TalendPaletteElement constructor comment.
     * 
     * @param composite
     * @param engine
     */
    public TalendPaletteElement(ITalendPaletteWidget composite, CSSEngine engine) {
        super((Composite) composite, engine);
    }

    public TalendPaletteCSSStyleSetting getCSSStyleSetting() {
        Object nativeWidget = this.getNativeWidget();
        if (nativeWidget != null) {
            return ((ITalendPaletteWidget) nativeWidget).getCSSStyleSetting();
        }
        return null;
    }

}
