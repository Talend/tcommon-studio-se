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
package org.talend.presentation.onboarding.utils.spies;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.eclipse.e4.ui.css.core.dom.CSSStylableElement;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.swt.dom.WidgetElement;
import org.eclipse.e4.ui.css.swt.engine.CSSSWTEngineImpl;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.talend.presentation.onboarding.exceptions.OnBoardingExceptionHandler;
import org.w3c.css.sac.SelectorList;
import org.w3c.dom.NodeList;

/**
 * created by cmeng on Sep 8, 2015 Detailled comment
 * 
 * @see org.eclipse.e4.tools.css.spy.CssSpyPart
 */
@SuppressWarnings("restriction")
public class CssSpy {

    /**
     * 
     * Get related widgets based on the css selector
     * 
     * @param css must be a css selector, Example: <b>#org-eclipse-ui-editorss>CTabFolder</b>
     * @return
     */
    public static Collection<Widget> getWidgetsByCSS(String css, Shell shells[]) {
        Collection<Widget> results = new LinkedHashSet<Widget>();
        if (!PlatformUI.isWorkbenchRunning()) {
            return results;
        }
        IWorkbench workbench = PlatformUI.getWorkbench();
        if (workbench == null) {
            return results;
        }
        Display display = workbench.getDisplay();
        if (display == null) {
            return results;
        }
        // Shell shells[] = display.getShells();
        if (shells == null || shells.length <= 0) {
            return results;
        }

        for (Shell shell : shells) {
            CSSStylableElement element = getCSSElement(shell);
            if (element == null) {
                continue;
            }

            CSSEngine engine = getCSSEngine(shell);

            try {
                SelectorList selectors = engine.parseSelectors(css);
                if (selectors == null || selectors.getLength() <= 0) {
                    continue;
                }
                getWidgetsByCSS(engine, selectors, element, null, results);
            } catch (Throwable e) {
                OnBoardingExceptionHandler.process(e);
            }
        }

        return results;
    }

    private static void getWidgetsByCSS(CSSEngine engine, SelectorList selectors, CSSStylableElement element, String pseudo,
            Collection<Widget> results) {
        NodeList children = element.getChildNodes();
        boolean matched = false;
        for (int i = 0; i < selectors.getLength(); i++) {
            matched = engine.matches(selectors.item(i), element, pseudo);
            if (matched) {
                break;
            }
        }
        if (matched) {
            results.add((Widget) element.getNativeWidget());
        }
        for (int i = 0; i < children.getLength(); i++) {
            getWidgetsByCSS(engine, selectors, (CSSStylableElement) children.item(i), pseudo, results);
        }
    }

    private static CSSStylableElement getCSSElement(Object o) {
        if (o instanceof CSSStylableElement) {
            return (CSSStylableElement) o;
        } else {
            CSSEngine engine = getCSSEngine(o);
            if (engine != null) {
                return (CSSStylableElement) engine.getElement(o);
            }
        }
        return null;
    }

    private static CSSEngine getCSSEngine(Object o) {
        CSSEngine engine = null;
        if (o instanceof CSSStylableElement) {
            CSSStylableElement element = (CSSStylableElement) o;
            engine = WidgetElement.getEngine((Widget) element.getNativeWidget());
        }
        if (engine == null && o instanceof Widget) {
            if (((Widget) o).isDisposed()) {
                return null;
            }
            engine = WidgetElement.getEngine((Widget) o);
        }
        if (engine == null && Display.getCurrent() != null) {
            engine = new CSSSWTEngineImpl(Display.getCurrent());
        }
        return engine;
    }

}
