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
package org.talend.themes.core.elements.providers;

import org.eclipse.e4.ui.css.core.dom.IElementProvider;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.talend.themes.core.elements.adapters.TalendPaletteElement;
import org.talend.themes.core.elements.widgets.ITalendPaletteWidget;
import org.w3c.dom.Element;

/**
 * created by cmeng on Jan 30, 2015 Detailled comment
 *
 */
public class TalendPaletteElementProvider implements IElementProvider {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.e4.ui.css.core.dom.IElementProvider#getElement(java.lang.Object,
     * org.eclipse.e4.ui.css.core.engine.CSSEngine)
     */
    @Override
    public Element getElement(Object element, CSSEngine engine) {
        Element result = null;

        if (element instanceof ITalendPaletteWidget) {
            result = new TalendPaletteElement((ITalendPaletteWidget) element, engine);
        }

        return result;
    }

}
