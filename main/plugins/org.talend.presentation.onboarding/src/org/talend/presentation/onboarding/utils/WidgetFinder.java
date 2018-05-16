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
package org.talend.presentation.onboarding.utils;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.talend.presentation.onboarding.utils.spies.CssSpy;

/**
 * created by cmeng on Sep 8, 2015 Detailled comment
 *
 * @see org.eclipse.e4.tools.css.spy.CssSpyPart
 */
public class WidgetFinder {

    public static Rectangle getBoundsInUIThread(final Widget widget) {
        final ObjectBox<Rectangle> resultBox = new ObjectBox<Rectangle>();
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                resultBox.value = getBounds(widget);
            }
        });
        return resultBox.value;
    }

    public static Rectangle getBounds(Widget widget) {
        if (widget instanceof Shell) {
            // Shell bounds are already in display coordinates
            return ((Shell) widget).getBounds();
        } else if (widget instanceof Control) {
            Control control = (Control) widget;
            Rectangle bounds = control.getBounds();
            return control.getDisplay().map(control.getParent(), control.getShell(), bounds);
        } else if (widget instanceof ToolItem) {
            ToolItem item = (ToolItem) widget;
            Rectangle bounds = item.getBounds();
            return item.getDisplay().map(item.getParent(), item.getControl().getShell(), bounds);
        } else if (widget instanceof CTabItem) {
            CTabItem item = (CTabItem) widget;
            Rectangle bounds = item.getBounds();
            return item.getDisplay().map(item.getParent(), item.getControl().getShell(), bounds);
        }
        return null;
    }

    // TODO need to know whether they are on top/visible
    public static Collection<Rectangle> getBounds(final String cssSelector, final Shell shells[]) {
        final Collection<Rectangle> bounds = new LinkedHashSet<Rectangle>();
        if (cssSelector == null || cssSelector.isEmpty()) {
            return bounds;
        }
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                Collection<Widget> widgets = findWidgetByCSS(cssSelector, shells);
                if (widgets == null || widgets.isEmpty()) {
                    return;
                }
                for (Widget widget : widgets) {
                    Rectangle rect = getBounds(widget);
                    if (rect != null) {
                        bounds.add(rect);
                    }
                }
            }
        });
        return bounds;
    }

    public static Collection<Widget> findWidgetsByCSSInUIThread(final String cssSelector, final Shell shells[]) {
        final ObjectBox<Collection<Widget>> resultBox = new ObjectBox<Collection<Widget>>();
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                resultBox.value = findWidgetByCSS(cssSelector, shells);
            }
        });
        return resultBox.value;
    }

    private static Collection<Widget> findWidgetByCSS(String cssSelector, Shell shells[]) {
        return CssSpy.getWidgetsByCSS(cssSelector, shells);
    }

}
