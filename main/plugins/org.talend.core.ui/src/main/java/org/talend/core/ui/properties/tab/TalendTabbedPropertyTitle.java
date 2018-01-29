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
package org.talend.core.ui.properties.tab;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.themes.core.elements.stylesettings.TalendTabbedPropertyColorHelper;
import org.talend.themes.core.elements.widgets.ITalendTabbedPropertyTitleWidget;

/**
 * created by hcyi on Feb 2, 2015 Detailled comment
 *
 */
public class TalendTabbedPropertyTitle extends Composite implements ITalendTabbedPropertyTitleWidget {

    private CLabel label;

    private Image image = null;

    private String text = null;

    private static final String BLANK = ""; //$NON-NLS-1$

    private static final String TITLE_FONT = "org.eclipse.ui.internal.views.properties.tabbed.view.TabbedPropertyTitle"; //$NON-NLS-1$

    private TabbedPropertySheetWidgetFactory factory;

    private TalendTabbedPropertyColorHelper colorHelper;

    /**
     * Constructor for TabbedPropertyTitle.
     * 
     * @param parent the parent composite.
     * @param factory the widget factory for the tabbed property sheet
     */
    public TalendTabbedPropertyTitle(Composite parent, TabbedPropertySheetWidgetFactory factory) {
        super(parent, SWT.NO_FOCUS);
        this.factory = factory;
        colorHelper = new TalendTabbedPropertyColorHelper(factory);
        // CSS
        CoreUIPlugin.setCSSClass(this, this.getClass().getSimpleName());
        this.addPaintListener(new PaintListener() {

            @Override
            public void paintControl(PaintEvent e) {
                if (image == null && (text == null || text.equals(BLANK))) {
                    label.setVisible(false);
                } else {
                    label.setVisible(true);
                    drawTitleBackground(e);
                }
            }
        });

        factory.getColors().initializeSectionToolBarColors();
        setBackground(factory.getColors().getBackground());
        setForeground(factory.getColors().getForeground());

        FormLayout layout = new FormLayout();
        layout.marginWidth = 1;
        layout.marginHeight = 2;
        setLayout(layout);

        Font font;
        if (!JFaceResources.getFontRegistry().hasValueFor(TITLE_FONT)) {
            FontData[] fontData = JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT).getFontData();
            /* title font is 2pt larger than that used in the tabs. */
            fontData[0].setHeight(fontData[0].getHeight() + 2);
            JFaceResources.getFontRegistry().put(TITLE_FONT, fontData);
        }
        font = JFaceResources.getFont(TITLE_FONT);

        label = factory.createCLabel(this, BLANK);
        if (colorHelper.getTitleBackground() == null) {
            label.setBackground(new Color[] { factory.getColors().getColor(IFormColors.H_GRADIENT_END),
                    factory.getColors().getColor(IFormColors.H_GRADIENT_START) }, new int[] { 100 }, true);
        } else {
            label.setBackground(colorHelper.getTitleBackground());
        }
        label.setFont(font);
        label.setForeground(colorHelper.getTitleForeground());
        FormData data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.top = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.bottom = new FormAttachment(100, 0);
        label.setLayoutData(data);

        /*
         * setImage(PlatformUI.getWorkbench().getSharedImages().getImage( ISharedImages.IMG_OBJ_ELEMENT));
         */
    }

    /**
     * @param e
     */
    protected void drawTitleBackground(PaintEvent e) {
        Rectangle bounds = getClientArea();
        if (colorHelper.getTitleBackground() == null) {
            label.setBackground(new Color[] { factory.getColors().getColor(IFormColors.H_GRADIENT_END),
                    factory.getColors().getColor(IFormColors.H_GRADIENT_START) }, new int[] { 100 }, true);
        } else {
            label.setBackground(colorHelper.getTitleBackground());
        }
        Color bg = factory.getColors().getColor(IFormColors.H_GRADIENT_END);
        Color gbg = factory.getColors().getColor(IFormColors.H_GRADIENT_START);
        GC gc = e.gc;
        gc.setForeground(bg);
        gc.setBackground(gbg);
        gc.fillGradientRectangle(bounds.x, bounds.y, bounds.width, bounds.height, true);
        // background bottom separator
        gc.setForeground(colorHelper.getTitleBottomForegroundKeyline1());
        if (colorHelper.isVisibleBorder()) {
            gc.drawLine(bounds.x, bounds.height - 2, bounds.x + bounds.width - 1, bounds.height - 2);
        }
        gc.setForeground(colorHelper.getTitleBottomForegroundKeyline2());
        if (colorHelper.isVisibleBorder()) {
            gc.drawLine(bounds.x, bounds.height - 1, bounds.x + bounds.width - 1, bounds.height - 1);
        }
    }

    /**
     * Set the text label.
     * 
     * @param text the text label.
     * @param image the image for the label.
     */
    public void setTitle(String text, Image image) {
        this.text = text;
        this.image = image;
        if (text != null) {
            label.setText(text);
        } else {
            label.setText(BLANK);
        }
        label.setImage(image);
        redraw();
    }

    public void setTitleImage(Image image) {
        this.image = image;
        label.setImage(image);
        redraw();
    }

    @Override
    public TalendTabbedPropertyColorHelper getColorHelper() {
        return this.colorHelper;
    }
}