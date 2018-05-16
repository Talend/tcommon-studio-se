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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Priority;
import org.eclipse.e4.ui.css.core.dom.properties.ICSSPropertyHandler;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.swt.dom.CTabItemElement;
import org.eclipse.e4.ui.css.swt.dom.ToolItemElement;
import org.eclipse.e4.ui.model.application.ui.basic.impl.PartImpl;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.ToolItem;
import org.talend.commons.exception.CommonExceptionHandler;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.CSSValueList;

/**
 * created by cmeng on Mar 24, 2015 Detailled comment
 *
 */
public class CSSSWTElementPropertyHandler implements ICSSPropertyHandler {

    protected static Map<String, Image> toolItemMap;

    @Override
    public boolean applyCSSProperty(Object element, String property, CSSValue value, String pseudo, CSSEngine engine)
            throws Exception {
        boolean applied = false;
        try {
            if ("tabitem-icon".equals(property)) { //$NON-NLS-1$
                if (element instanceof CTabItemElement) {
                    CTabItemElement tabItemElement = (CTabItemElement) element;
                    CTabItem item = (CTabItem) tabItemElement.getNativeWidget();
                    Object imageObj = engine.convert(value, Image.class, item.getDisplay());
                    if (imageObj != null && imageObj != item.getImage() && !item.isDisposed() && !item.getParent().isDisposed()) {
                        Image image = ((Image) imageObj);
                        item.setImage(image);
                        item.getParent().redraw();
                    }
                    applied = true;
                }
            }
            if ("toolitem-icon".equals(property)) {
                if (element instanceof ToolItemElement) {
                    ToolItemElement toolItemElement = (ToolItemElement) element;
                    ToolItem item = (ToolItem) toolItemElement.getNativeWidget();
                    Object data = item.getData();
                    if (data instanceof PartImpl && value instanceof CSSValueList) {

                        String widgetElementId = ((PartImpl) data).getElementId();
                        if (StringUtils.isNotEmpty(widgetElementId)) {
                            CSSValueList cssValueList = (CSSValueList) value;
                            if (toolItemMap == null) {
                                initToolItemMap(engine, item, cssValueList);
                            }
                            Image image = toolItemMap.get(widgetElementId);

                            if (image != null && image != item.getImage() && !item.isDisposed()) {
                                item.setImage(image);
                            }
                        }
                    }
                    applied = true;
                }
            }
        } catch (Throwable t) {
            CommonExceptionHandler.process(t, Priority.WARN);
            applied = false;
        }
        return applied;
    }

    protected static void initToolItemMap(CSSEngine engine, ToolItem item, CSSValueList cssValueList) throws Exception {
        int length = cssValueList.getLength() - 1;
        toolItemMap = new HashMap<String, Image>();
        int i = 0;
        while (i < length) {
            String elementId = cssValueList.item(i++).getCssText();
            CSSValue cssValue = cssValueList.item(i++);
            if (cssValue == null) {
                break;
            }
            Object imageObj = engine.convert(cssValue, Image.class, item.getDisplay());
            if (StringUtils.isNotEmpty(elementId) && imageObj != null) {
                toolItemMap.put(elementId, (Image) imageObj);
            }
        }
    }

    @Override
    public String retrieveCSSProperty(Object element, String property, String pseudo, CSSEngine engine) throws Exception {
        return null;
    }

}
