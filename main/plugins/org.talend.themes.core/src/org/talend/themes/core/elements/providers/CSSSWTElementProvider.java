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
import org.eclipse.e4.ui.css.swt.dom.CTabItemElement;
import org.eclipse.e4.ui.css.swt.dom.SWTElementProvider;
import org.eclipse.e4.ui.css.swt.dom.ToolItemElement;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.ToolItem;
import org.talend.themes.core.elements.adapters.CSSUserProfileElementAdapter;
import org.talend.themes.core.elements.widgets.ITalendTabbedPropertyListWidget;
import org.talend.themes.core.elements.widgets.ITalendTabbedPropertyTitleWidget;
import org.w3c.dom.Element;

/**
 * Returns the CSS class which is responsible for styling a SWT widget
 *
 * Registered via the "org.eclipse.e4.ui.css.core.elementProvider" extension point for the SWT widgets
 *
 *
 *
 * {@link IElementProvider} SWT implementation to retrieve w3c Element {@link SWTElement} linked to SWT widget.
 *
 */

public class CSSSWTElementProvider implements IElementProvider {

    public static final IElementProvider INSTANCE = new SWTElementProvider();

    @Override
    public Element getElement(Object element, CSSEngine engine) {
        if (element instanceof ITalendTabbedPropertyListWidget) {
            return new CSSUserProfileElementAdapter((Composite) element, engine);
        }
        if (element instanceof ITalendTabbedPropertyTitleWidget) {
            return new CSSUserProfileElementAdapter((Composite) element, engine);
        }
        if (element instanceof CTabItem) {
            return new CTabItemElement((Item) element, engine);
        }
        if (element instanceof ToolItem) {
            return new ToolItemElement((ToolItem) element, engine);
        }
        return null;
    }
}
