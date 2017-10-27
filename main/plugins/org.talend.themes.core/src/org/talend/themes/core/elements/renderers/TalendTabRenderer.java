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
package org.talend.themes.core.elements.renderers;

import java.lang.reflect.Field;

import net.jeeeyul.eclipse.themes.rendering.JeeeyulsTabRenderer;
import net.jeeeyul.swtend.SWTExtensions;
import net.jeeeyul.swtend.ui.HSB;

import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolderRenderer;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.themes.core.elements.stylesettings.TalendTabSettings;

/**
 * created by cmeng on Feb 9, 2015 Detailled comment
 *
 */
public class TalendTabRenderer extends JeeeyulsTabRenderer {

    protected static SWTExtensions swtExtensions;

    protected TalendTabSettings settings;

    protected static Field settingsField;

    protected CTabFolder tTabFolder;

    protected static final String SWT_MODEL_ELEMENT = "modelElement"; //$NON-NLS-1$

    /**
     * DOC cmeng TalendTabRenderer constructor comment.
     * 
     * @param parent
     */
    public TalendTabRenderer(CTabFolder parent) {
        super(parent);
        tTabFolder = parent;
        try {
            if (swtExtensions == null) {
                Field swtExtensionsField = JeeeyulsTabRenderer.class.getDeclaredField("_sWTExtensions");
                swtExtensionsField.setAccessible(true);
                swtExtensions = (SWTExtensions) swtExtensionsField.get(this);
            }
            if (settingsField == null) {
                settingsField = JeeeyulsTabRenderer.class.getDeclaredField("settings");
                settingsField.setAccessible(true);
            }
            settings = new TalendTabSettings(this);
            settingsField.set(this, settings);
        } catch (Exception e) {
            CommonExceptionHandler.process(e);
        }
    }

    @Override
    protected void draw(final int part, final int state, final Rectangle bounds, final GC gc) {
        if (CTabFolderRenderer.PART_MAX_BUTTON == part) {
            this.drawMaximize(gc, bounds, state, part);
        } else if (CTabFolderRenderer.PART_MIN_BUTTON == part) {
            this.drawMinimize(gc, bounds, state, part);
        } else {
            super.draw(part, state, bounds, gc);
            if (CTabFolderRenderer.PART_HEADER == part && !this.settings.getCTabItemImageMap().isEmpty()
                    && !tTabFolder.getSingle()) {
                CTabItem items[] = tTabFolder.getItems();
                for (int i = 0; i < items.length; i++) {
                    Object viewPart = items[i].getData(SWT_MODEL_ELEMENT);
                    if (viewPart != null && viewPart instanceof MPlaceholder && items[i] != null) {
                        Image image = this.settings.getCTabItemImage(((MPlaceholder) viewPart).getElementId());
                        if (image != null && image != items[i].getImage() && !items[i].isDisposed()) {
                            items[i].setImage(image);
                            // Rectangle itemBounds = items[i].getBounds();
                            // this.draw(i, SWT.NONE, itemBounds, gc);
                        }
                    }
                }
            }
        }
    }

    // @Override
    // protected int drawTabItem(int part, int state, Rectangle bounds, GC gc) {
    //
    // if (!this.settings.getCTabItemImageMap().isEmpty() && !tTabFolder.getSingle()) {
    // CTabItem item = tTabFolder.getItem(part);
    // Object viewPart = item.getData(SWT_MODEL_ELEMENT);
    // if (viewPart != null && viewPart instanceof MPlaceholder && item != null) {
    // Image image = this.settings.getCTabItemImage(((MPlaceholder) viewPart).getElementId());
    // if (image != null && image != item.getImage() && !item.isDisposed()) {
    // item.setImage(image);
    // }
    // }
    // }
    //
    // return super.drawTabItem(part, state, bounds, gc);
    // }

    @Override
    public TalendTabSettings getSettings() {
        return this.settings;
    }

    protected void drawMinimize(final GC gc, final Rectangle minRect, final int minImageState, final int part) {
        if (((minRect.width == 0) || (minRect.height == 0))) {
            super.draw(part, minImageState, minRect, gc);
            return;
        }
        boolean complete = false;
        Display display = this.parent.getDisplay();
        HSB foreground = this.settings.getMinimizeButtonForgroundColor();
        HSB background = this.settings.getMinimizeButtonBackgroundColor();
        Color foregroundColor = (null);
        if (foreground != null) {
            Color _autoDisposeColor = swtExtensions.toAutoDisposeColor(foreground);
            foregroundColor = _autoDisposeColor;
        } else {
            Color _systemColor = display.getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW);
            foregroundColor = _systemColor;
        }
        gc.setForeground(foregroundColor);
        Color backgroundColor = (null);
        if (background != null) {
            Color _autoDisposeColor_1 = swtExtensions.toAutoDisposeColor(background);
            backgroundColor = _autoDisposeColor_1;
        } else {
            Color _systemColor_1 = display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
            backgroundColor = _systemColor_1;
        }
        gc.setBackground(backgroundColor);
        gc.setAdvanced(true);
        gc.setAntialias(SWT.ON);
        Image image = null;
        switch (minImageState & (SWT.HOT | SWT.SELECTED)) {
        case SWT.NONE:
            image = this.settings.getMinimizeButtonNoneStatueImage();
            if (image != null) {
                Rectangle bounds = image.getBounds();
                gc.drawImage(image, bounds.x, bounds.y, bounds.width, bounds.height, minRect.x, minRect.y, minRect.width,
                        minRect.height);
                complete = true;
            }
            break;
        case SWT.HOT:
            image = this.settings.getMinimizeButtonHotStatueImage();
            if (image != null) {
                Rectangle bounds_1 = image.getBounds();
                gc.drawImage(image, bounds_1.x, bounds_1.y, bounds_1.width, bounds_1.height, minRect.x, minRect.y, minRect.width,
                        minRect.height);
                complete = true;
            }
            break;
        case SWT.SELECTED:
            image = this.settings.getMinimizeButtonSelectedStatueImage();
            if (image != null) {
                Rectangle bounds_2 = image.getBounds();
                gc.drawImage(image, bounds_2.x, bounds_2.y, bounds_2.width, bounds_2.height, minRect.x, minRect.y, minRect.width,
                        minRect.height);
                complete = true;
            }
            break;
        }
        if ((complete == false)) {
            super.draw(part, minImageState, minRect, gc);
        }
    }

    protected void drawMaximize(final GC gc, final Rectangle maxRect, final int maxImageState, final int part) {
        if (((maxRect.width == 0) || (maxRect.height == 0))) {
            super.draw(part, maxImageState, maxRect, gc);
            return;
        }
        boolean complete = false;
        Display display = this.parent.getDisplay();
        HSB foreground = this.settings.getMaximizeButtonForgroundColor();
        HSB background = this.settings.getMaximizeButtonBackgroundColor();
        Color foregroundColor = (null);
        if (foreground != null) {
            Color _autoDisposeColor = swtExtensions.toAutoDisposeColor(foreground);
            foregroundColor = _autoDisposeColor;
        } else {
            Color _systemColor = display.getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW);
            foregroundColor = _systemColor;
        }
        gc.setForeground(foregroundColor);
        Color backgroundColor = (null);
        if (background != null) {
            Color _autoDisposeColor_1 = swtExtensions.toAutoDisposeColor(background);
            backgroundColor = _autoDisposeColor_1;
        } else {
            Color _systemColor_1 = display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
            backgroundColor = _systemColor_1;
        }
        gc.setBackground(backgroundColor);
        gc.setAdvanced(true);
        gc.setAntialias(SWT.ON);
        Image image = null;
        switch (maxImageState & (SWT.HOT | SWT.SELECTED)) {
        case SWT.NONE:
            image = this.settings.getMaximizeButtonNoneStatueImage();
            if (image != null) {
                Rectangle bounds = image.getBounds();
                gc.drawImage(image, bounds.x, bounds.y, bounds.width, bounds.height, maxRect.x, maxRect.y, maxRect.width,
                        maxRect.height);
                complete = true;
            }
            break;
        case SWT.HOT:
            image = this.settings.getMaximizeButtonHotStatueImage();
            if (image != null) {
                Rectangle bounds_1 = image.getBounds();
                gc.drawImage(image, bounds_1.x, bounds_1.y, bounds_1.width, bounds_1.height, maxRect.x, maxRect.y, maxRect.width,
                        maxRect.height);
                complete = true;
            }
            break;
        case SWT.SELECTED:
            image = this.settings.getMinimizeButtonSelectedStatueImage();
            if (image != null) {
                Rectangle bounds_2 = image.getBounds();
                gc.drawImage(image, bounds_2.x, bounds_2.y, bounds_2.width, bounds_2.height, maxRect.x, maxRect.y, maxRect.width,
                        maxRect.height);
                complete = true;
            }
            break;
        }
        if ((complete == false)) {
            super.draw(part, maxImageState, maxRect, gc);
        }
    }
}
